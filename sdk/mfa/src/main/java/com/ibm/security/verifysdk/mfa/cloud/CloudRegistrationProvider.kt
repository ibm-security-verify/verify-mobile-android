/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.ContextHelper
import com.ibm.security.verifysdk.core.KeystoreHelper
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.core.camelToSnakeCase
import com.ibm.security.verifysdk.core.entering
import com.ibm.security.verifysdk.core.exiting
import com.ibm.security.verifysdk.core.replace
import com.ibm.security.verifysdk.core.snakeToCamelCase
import com.ibm.security.verifysdk.mfa.EnrollableSignature
import com.ibm.security.verifysdk.mfa.EnrollableType
import com.ibm.security.verifysdk.mfa.FaceFactorInfo
import com.ibm.security.verifysdk.mfa.FactorType
import com.ibm.security.verifysdk.mfa.FingerprintFactorInfo
import com.ibm.security.verifysdk.mfa.HashAlgorithmError
import com.ibm.security.verifysdk.mfa.HashAlgorithmType
import com.ibm.security.verifysdk.mfa.InitializationInfo
import com.ibm.security.verifysdk.mfa.MFAAttributeInfo
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationError
import com.ibm.security.verifysdk.mfa.SignatureEnrollableFactor
import com.ibm.security.verifysdk.mfa.TOTPFactorInfo
import com.ibm.security.verifysdk.mfa.UserPresenceFactorInfo
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.UUID

class CloudRegistrationProvider(data: String) :
    MFARegistrationDescriptor<MFAAuthenticatorDescriptor> {

    private val log = LoggerFactory.getLogger(javaClass)

    private val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val initializationInfo: InitializationInfo

    private lateinit var tokenInfo: TokenInfo
    private lateinit var metaData: Metadata
    private var currentFactor: SignatureEnrollableFactor? = null
    private var factors: MutableList<FactorType> = mutableListOf()

    override var pushToken: String = ""
    override var accountName: String = ""
    override val countOfAvailableEnrollments: Int
        get() {
            return metaData.availableFactors.size
        }
    override var authenticationRequired: Boolean = false
    override var invalidatedByBiometricEnrollment: Boolean = false

    init {
        initializationInfo = decoder.decodeFromString(data)
        accountName = initializationInfo.accountName
        pushToken = ""
    }

    internal suspend fun initiate(
        accountName: String,
        skipTotpEnrollment: Boolean = true,
        pushToken: String?
    ): Result<CloudRegistrationProviderResultData> {
        log.entering()
        this.accountName = accountName
        this.pushToken = pushToken.orEmpty()

        return try {
            val registrationUrl =
                URL("${initializationInfo.uri}?skipTotpEnrollment=${skipTotpEnrollment}")

            val response = NetworkHelper.getInstance.post {
                url(registrationUrl.toString())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                setBody(constructRequestBody("code"))
            }

            if (response.status.isSuccess()) {
                response.bodyAsText().let { responseBodyData ->
                    tokenInfo = decoder.decodeFromString(responseBodyData)
                    val registration: Registration = decoder.decodeFromString(responseBodyData)

                    registration.metadataContainer.let {
                        metaData = Metadata(
                            id = registration.id,
                            registrationUri = it.registrationUri,
                            serviceName = it.serviceName,
                            transactionUri = it.registrationUri.replace(
                                "registration",
                                "${registration.id}/verifications"
                            ),
                            features = it.features,
                            custom = it.custom,
                            theme = it.theme,
                            availableFactors = registration.availableFactor
                        )
                    }

                    registration.availableFactor.stream()
                        .filter { factor -> factor.type == EnrollableType.TOTP }.findFirst()
                        .ifPresent { factor ->
                            (factor as? CloudTOTPEnrollableFactor)?.let { cloudTotpEnrollableFactor ->
                                if (skipTotpEnrollment.not()) {
                                    factors.add(
                                        FactorType.Totp(
                                            TOTPFactorInfo(
                                                secret = cloudTotpEnrollableFactor.secret,
                                                algorithm = HashAlgorithmType.fromString(
                                                    cloudTotpEnrollableFactor.algorithm
                                                ),
                                                digits = cloudTotpEnrollableFactor.digits,
                                                period = cloudTotpEnrollableFactor.period
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    registration.availableFactor.removeAll { factor -> factor.type == EnrollableType.TOTP }

                    return@initiate Result.success(
                        CloudRegistrationProviderResultData(
                            tokenInfo,
                            metaData
                        )
                    )
                }
            } else {
                return Result.failure(MFARegistrationError.UnderlyingError(Error(response.bodyAsText())))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        } finally {
            log.exiting()
        }
    }

    override fun nextEnrollment(): EnrollableSignature? {

        try {
            log.entering()

            if (metaData.availableFactors.isEmpty()) {
                return null
            }

            metaData.availableFactors.first().let { enrollableFactor ->
                (enrollableFactor as SignatureEnrollableFactor).let { signatureEnrollableFactor ->
                    currentFactor = signatureEnrollableFactor
                    metaData.availableFactors.removeAll { enrollableFactor.type == it.type }

                    val algorithm =
                        currentFactor?.algorithm?.let { HashAlgorithmType.fromString(it) }
                    val biometricAuthentication =
                        (currentFactor?.type == EnrollableType.USER_PRESENCE).not()

                    return@nextEnrollment algorithm?.let {
                        EnrollableSignature(
                            biometricAuthentication,
                            it,
                            metaData.id
                        )
                    }
                }
            }
        } finally {
            log.exiting()
        }
    }

    override suspend fun enroll() {

        currentFactor?.let {

            val keyName = "${metaData.id}.${currentFactor?.type?.name}"
            KeystoreHelper.createKeyPair(
                keyName,
                currentFactor?.algorithm ?: "",
                authenticationRequired,
                invalidatedByBiometricEnrollment
            )

            KeystoreHelper.exportPublicKey(keyName, android.util.Base64.NO_WRAP)?.let { publicKey ->
                KeystoreHelper.signData(
                    keyName,
                    currentFactor?.algorithm ?: "",
                    metaData.id,
                    android.util.Base64.NO_WRAP
                )
                    ?.let { signedData ->
                        enroll(keyName, publicKey, signedData)
                    }
            }
        }

        return
    }

    override suspend fun enroll(name: String, publicKey: String, signedData: String) {

        val algorithm = currentFactor?.algorithm?.let { HashAlgorithmType.fromString(it) }
            ?: run {
                throw HashAlgorithmError.InvalidHash
            }

        val requestBody: RequestBody = buildJsonArray {
            addJsonObject {
                put("subType", currentFactor?.type?.name?.lowercase()?.snakeToCamelCase())
                put("enabled", true)
                put("attributes", buildJsonObject {
                    put("signedData", signedData)
                    put("publicKey", publicKey)
                    put(
                        "deviceSecurity",
                        currentFactor?.type == EnrollableType.FACE || currentFactor?.type == EnrollableType.FINGERPRINT
                    )
                    put("algorithm", HashAlgorithmType.toIsvFormat(algorithm))
                    put("additionalData", buildJsonArray {
                        addJsonObject {
                            put("name", "name")
                            put("value", name)
                        }
                    })
                })
            }
        }.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val response = NetworkHelper.getInstance.post {
            url(currentFactor?.uri.toString())
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            bearerAuth(tokenInfo.authorizationHeader())
            setBody(requestBody)
        }

        if (response.status.isSuccess()) {
            response.bodyAsText().let { responseBodyData ->
                val enrollments = JSONArray(responseBodyData)

                for (i in 0 until enrollments.length()) {
                    (enrollments[i] as JSONObject).let { enrollment ->
                        val subType = enrollment["subType"] as String
                        val type =
                            EnrollableType.valueOf(subType.camelToSnakeCase().uppercase())
                        val id = enrollment["id"] as String
                        val uuid = UUID.fromString(id)

                        if (type == currentFactor?.type) {
                            when (currentFactor?.type) {
                                EnrollableType.FACE -> factors.add(
                                    FactorType.Face(
                                        FaceFactorInfo(
                                            id = uuid,
                                            displayName = name,
                                            algorithm = algorithm
                                        )
                                    )
                                )

                                EnrollableType.FINGERPRINT -> factors.add(
                                    FactorType.Fingerprint(
                                        FingerprintFactorInfo(
                                            id = uuid,
                                            displayName = name,
                                            algorithm = algorithm
                                        )
                                    )
                                )

                                else -> factors.add(
                                    FactorType.UserPresence(
                                        UserPresenceFactorInfo(
                                            id = uuid,
                                            displayName = name,
                                            algorithm = algorithm
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            throw MFARegistrationError.UnderlyingError(Error(response.bodyAsText()))
        }
    }

    override suspend fun finalize(): Result<MFAAuthenticatorDescriptor> {

        return try {

            val registrationUrl =
                URL("${initializationInfo.uri}?metadataInResponse=false")

            // Refresh the token, which sets the authenticator state from ENROLLING to ACTIVE.
            val response = NetworkHelper.getInstance.post {
                url(registrationUrl.toString())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                setBody(constructRequestBody("refreshToken"))
            }

            if (response.status.isSuccess()) {
                response.bodyAsText().let { responseBodyData ->
                    tokenInfo = decoder.decodeFromString(responseBodyData)
                }
            }
            Result.success(
                CloudAuthenticator(
                    refreshUri = metaData.registrationUri,
                    transactionUri =  metaData.registrationUri.replace(
                        "registration",
                        "${metaData.id}/verifications"
                    ),
                    theme = metaData.theme,
                    token = tokenInfo,
                    id = metaData.id,
                    serviceName = metaData.serviceName,
                    accountName = accountName,
                    allowedFactors = factors,
                    customAttributes = metaData.custom
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private fun constructRequestBody(additionalData: String): RequestBody {

        val attributes =
            MFAAttributeInfo.init(ContextHelper.context).dictionary().toMutableMap()
        attributes["accountName"] = this.accountName
        attributes["pushToken"] = this.pushToken
        attributes.remove("applicationName")

        val data: MutableMap<String, Any> = HashMap()
        data["attributes"] = attributes

        when (additionalData) {
            "refreshToken" -> data["refreshToken"] = tokenInfo.refreshToken
            "code" -> data["code"] = initializationInfo.code
        }

        return (data as Map<*, *>).let {
            JSONObject(it).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        }
    }
}