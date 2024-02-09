/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2

import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo.Builder
import androidx.fragment.app.FragmentActivity
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper
import com.ibm.security.verify.fido2.model.AssertionOptions
import com.ibm.security.verify.fido2.model.AssertionResultResponse
import com.ibm.security.verify.fido2.model.AttestationOptions
import com.ibm.security.verify.fido2.model.AttestationResultResponse
import com.ibm.security.verify.fido2.model.AuthenticatorAssertionResponse
import com.ibm.security.verify.fido2.model.AuthenticatorAttestationResponse
import com.ibm.security.verify.fido2.model.ClientDataJsonAssertion
import com.ibm.security.verify.fido2.model.ClientDataJsonAttestation
import com.ibm.security.verify.fido2.model.PublicKeyCredentialCreationOptions
import com.ibm.security.verify.fido2.model.PublicKeyCredentialRequestOptions
import com.ibm.security.verify.fido2.model.ResponseAssertion
import com.ibm.security.verify.fido2.model.ResponseAttestation
import com.ibm.security.verifysdk.core.KeystoreHelper
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.core.base64UrlEncode
import com.ibm.security.verifysdk.core.sha256
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.PublicKey
import java.security.Signature
import java.security.interfaces.ECPublicKey
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.max
import kotlin.math.min

@OptIn(
    ExperimentalUnsignedTypes::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalStdlibApi::class
)
class Fido2Api {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    fun createKeyPair(
        keyName: String,
        authenticationRequired: Boolean = false,
        invalidatedByBiometricEnrollment: Boolean = false
    ): PublicKey {

        return KeystoreHelper.createKeyPair(
            keyName,
            algorithm = KeyProperties.KEY_ALGORITHM_EC,
            purpose = KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY,
            authenticationRequired = authenticationRequired,
            invalidatedByBiometricEnrollment = invalidatedByBiometricEnrollment
        )
    }

    internal fun exportPublicKey(
        keyName: String,
        base64EncodingOption: Int = Base64.URL_SAFE
    ): String? {
        return KeystoreHelper.exportPublicKey(keyName, base64EncodingOption)
    }

    suspend fun initiateAttestation(
        attestationOptionsUrl: String,
        authorization: String,
        attestationOptions: AttestationOptions
    ): Result<PublicKeyCredentialCreationOptions> {
        return try {
            NetworkHelper.handleApi<PublicKeyCredentialCreationOptions>(
                NetworkHelper.networkApi.postRequest(
                    HashMap(),
                    attestationOptionsUrl,
                    authorization,
                    Json.encodeToString(attestationOptions)
                        .toRequestBody("application/json".toMediaType())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun sendAttestation(
        attestationResultUrl: String,
        authorization: String,
        authenticatorAttestationResponse: AuthenticatorAttestationResponse
    ): Result<AttestationResultResponse> {
        return try {
            NetworkHelper.handleApi<AttestationResultResponse>(
                NetworkHelper.networkApi.postRequest(
                    HashMap(),
                    attestationResultUrl,
                    authorization,
                    Json.encodeToString(authenticatorAttestationResponse)
                        .toRequestBody("application/json".toMediaType())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun sendAssertion(
        assertionResultUrl: String,
        authorization: String,
        authenticatorAssertionResponse: AuthenticatorAssertionResponse
    ): Result<AssertionResultResponse> {
        return try {
            NetworkHelper.handleApi<AssertionResultResponse>(
                NetworkHelper.networkApi.postRequest(
                    HashMap(),
                    assertionResultUrl,
                    authorization,
                    Json.encodeToString(authenticatorAssertionResponse)
                        .toRequestBody("application/json".toMediaType())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun initiateAssertion(
        assertionOptionsUrl: String,
        authorization: String,
        assertionOptions: AssertionOptions
    ): Result<PublicKeyCredentialRequestOptions> {
        return try {
            NetworkHelper.handleApi<PublicKeyCredentialRequestOptions>(
                NetworkHelper.networkApi.postRequest(
                    HashMap(),
                    assertionOptionsUrl,
                    authorization,
                    Json.encodeToString(assertionOptions)
                        .toRequestBody("application/json".toMediaType())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private fun toUnsignedFixedLength(arr: ByteArray, fixedLength: Int): ByteArray {
        val fixed = ByteArray(fixedLength)
        val offset = fixedLength - arr.size
        val copyLength = min(arr.size, fixedLength)
        System.arraycopy(arr, max(-offset, 0), fixed, max(offset, 0), copyLength)
        return fixed
    }

    private fun getCoseKey(keyName: String): COSEKey {
        val ecPublicKey = KeystoreHelper.getPublicKey(keyName) as ECPublicKey
        val point = ecPublicKey.w
        val x: ByteArray = toUnsignedFixedLength(point.affineX.toByteArray(), 32)
        val y: ByteArray = toUnsignedFixedLength(point.affineY.toByteArray(), 32)
        return COSEKey(2, -7, 1, x, y)
    }

    suspend fun buildAuthenticatorAttestationResponse(
        activity: FragmentActivity,
        executor: Executor,
        promptInfoBuilder: Builder,
        aaGuid: String,
        keyName: String,
        flags: Byte,
        options: PublicKeyCredentialCreationOptions,
        nickName: String
    ): AuthenticatorAttestationResponse {

        val clientDataJson = ClientDataJsonAttestation(
            "webauthn.create",
            options.challenge,
            "https://${options.rp.id!!}",
            false,
        )

        val id = keyName.sha256().hexToByteArray()
        val clientDataString = json.encodeToString(clientDataJson)

        // Build authenticatorData
        val authenticatorDataParams = mutableListOf<Byte>()
        authenticatorDataParams.addAll(options.rp.id!!.sha256().hexToByteArray().toList())
        authenticatorDataParams.add(flags)

        val now = Clock.System.now().epochSeconds
        val counter = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(now.toInt()).array()
        authenticatorDataParams.addAll(counter.toList())

        // Build attestedCredentialData
        val attestedCredentialData = mutableListOf<Byte>()
        attestedCredentialData.addAll(aaGuid.replace("-", "").hexToByteArray().toList())

        val length = byteArrayOf(
            ((id.size shr 8) and 0xFF).toByte(),
            (id.size and 0xFF).toByte()
        )

        attestedCredentialData.addAll(length.toList())
        // Add the credentialId
        attestedCredentialData.addAll(id.toList())

        val coseKey = getCoseKey(keyName)
        attestedCredentialData.addAll(coseKey.toByteArray.toList())

        // Add attestedCredential to authenticatorData
        authenticatorDataParams.addAll(attestedCredentialData)

        val attestationStatement = buildAttestationStatement(
            activity = activity,
            executor = executor,
            promptInfoBuilder = promptInfoBuilder,
            keyName = keyName,
            clientDataString = clientDataString,
            authenticatorData = authenticatorDataParams.toByteArray(),
        )

        val attestedObject = HashMap<String, Any>()
        attestedObject["fmt"] = "packed"
        attestedObject["attStmt"] = attestationStatement
        attestedObject["authData"] = authenticatorDataParams.toByteArray()

        return AuthenticatorAttestationResponse(
            id.base64UrlEncode(),
            id.base64UrlEncode(),
            ResponseAttestation(
                clientDataString.base64UrlEncode(),
                CBORMapper().writeValueAsBytes(attestedObject).toUByteArray().base64UrlEncode()
            ),
            "public-key",
            nickName
        )
    }

    private suspend fun buildAttestationStatement(
        activity: FragmentActivity,
        executor: Executor,
        promptInfoBuilder: Builder,
        keyName: String,
        clientDataString: String,
        authenticatorData: ByteArray
    ): Map<String, Any> {

        val result = HashMap<String, Any>()
        result["alg"] = -7

        val dataToSign = mutableListOf<Byte>()
        dataToSign.addAll(authenticatorData.toList())
        dataToSign.addAll(clientDataString.sha256().hexToByteArray().toList())

        invokeBiometricAuthentication(
            keyName = keyName,
            activity = activity,
            executor = executor,
            promptInfoBuilder = promptInfoBuilder
        ).let { authenticationResult ->
            when (authenticationResult) {
                is BiometricPromptResult.Success -> {
                    authenticationResult.data.cryptoObject?.let { cryptoObject ->
                        KeystoreHelper.signData(cryptoObject, dataToSign.toByteArray())
                            ?.let { sig ->
                                result["sig"] = sig
                            }
                    }
                }

                is BiometricPromptResult.Failure -> {
                    println("Error: ${authenticationResult.errorCode} - ${authenticationResult.errorMessage}")
                }
            }
        }
        return result
    }

    private suspend fun invokeBiometricAuthentication(
        activity: FragmentActivity,
        executor: Executor,
        promptInfoBuilder: Builder,
        keyName: String
    ): BiometricPromptResult<BiometricPrompt.AuthenticationResult> {
        return suspendCancellableCoroutine { continuation ->
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        continuation.resume(BiometricPromptResult.Success(result), null)
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        continuation.resume(
                            BiometricPromptResult.Failure(
                                errString.toString(),
                                errorCode
                            )
                        )
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        continuation.resumeWithException(BiometricAuthenticationException("Authentication failed"))

                    }
                })

            val signature = Signature.getInstance("SHA256withECDSA")
            val secretKey = KeystoreHelper.getPrivateKey(keyName = keyName)
            signature.initSign(secretKey)
            biometricPrompt.authenticate(
                promptInfoBuilder.build(),
                BiometricPrompt.CryptoObject(signature)
            )
        }
    }


    suspend fun buildAuthenticatorAssertionResponse(
        activity: FragmentActivity,
        executor: Executor,
        promptInfoBuilder: Builder,
        keyName: String,
        flags: Byte,
        options: PublicKeyCredentialRequestOptions,
        message: String?
    ): AuthenticatorAssertionResponse {

        val clientDataJson = ClientDataJsonAssertion(
            "webauthn.get",
            options.challenge,
            "https://${options.rpId}"
        )

        val id = keyName.sha256().hexToByteArray()
        val clientDataString = json.encodeToString(clientDataJson)

        val authenticatorDataParams = mutableListOf<Byte>()
        authenticatorDataParams.addAll(options.rpId.sha256().hexToByteArray().toList())
        authenticatorDataParams.add(flags)

        val now = Clock.System.now().epochSeconds
        val counter = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(now.toInt()).array()
        authenticatorDataParams.addAll(counter.toList())

        message?.let {
            val extension = HashMap<String, Any>()
            extension["txAuthSimple"] = message
            authenticatorDataParams.addAll(CBORMapper().writeValueAsBytes(extension).toList())
        }

        val dataToSign = mutableListOf<Byte>()
        dataToSign.addAll(authenticatorDataParams.toList())
        dataToSign.addAll(clientDataString.sha256().hexToByteArray().toList())

        var sig = "".toByteArray()
        invokeBiometricAuthentication(
            keyName = keyName,
            activity = activity,
            executor = executor,
            promptInfoBuilder = promptInfoBuilder
        ).let { authenticationResult ->
            when (authenticationResult) {
                is BiometricPromptResult.Success -> {
                    authenticationResult.data.cryptoObject?.let { cryptoObject ->
                        KeystoreHelper.signData(cryptoObject, dataToSign.toByteArray())?.let {
                            sig = it
                        }
                    }
                }

                is BiometricPromptResult.Failure -> {
                    println("Error: ${authenticationResult.errorCode} - ${authenticationResult.errorMessage}")
                }
            }
        }

        return AuthenticatorAssertionResponse(
            id.base64UrlEncode(),
            id.base64UrlEncode(),
            ResponseAssertion(
                clientDataString.base64UrlEncode(),
                authenticatorDataParams.toByteArray().base64UrlEncode(),
                sig.base64UrlEncode()
            ),
            "public-key"
        )
    }

    internal sealed class BiometricPromptResult<out T> {
        data class Success<out T>(val data: T) : BiometricPromptResult<T>()
        data class Failure(val errorMessage: String, val errorCode: Int? = null) :
            BiometricPromptResult<Nothing>()
    }
}