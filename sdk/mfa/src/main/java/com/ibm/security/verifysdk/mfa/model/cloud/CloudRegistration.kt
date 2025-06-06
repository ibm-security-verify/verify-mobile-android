/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

@file:UseSerializers(URLSerializer::class)
package com.ibm.security.verifysdk.mfa.model.cloud

import com.ibm.security.verifysdk.core.serializer.URLSerializer
import com.ibm.security.verifysdk.mfa.EnrollableFactor
import com.ibm.security.verifysdk.mfa.EnrollableType
import com.ibm.security.verifysdk.mfa.HashAlgorithmType
import com.ibm.security.verifysdk.mfa.HashAlgorithmTypeSerializer
import com.ibm.security.verifysdk.mfa.SignatureEnrollableFactor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

@Serializable
internal data class CloudRegistration @OptIn(ExperimentalSerializationApi::class) constructor(
    val accessToken: String,
    val expiresIn: Int,
    val id: String,
    @SerialName("metadata")
    val metadataContainer: MetadataContainer,
    val refreshToken: String,
    val version: Version
) {
    val transactionUri: URL =
        URL(metadataContainer.registrationUri.toString().replace("registration", "$id/verifications"))

    var availableFactors: ArrayList<EnrollableFactor> = java.util.ArrayList()

    init {
        metadataContainer.authenticationMethods.userPresence?.let {
            availableFactors.add(
                SignatureEnrollableFactor(
                    it.enrollmentUri,
                    EnrollableType.USER_PRESENCE,
                    it.attributes.algorithm.toString()
                )
            )
        }

        metadataContainer.authenticationMethods.face?.let {
            availableFactors.add(
                SignatureEnrollableFactor(
                    it.enrollmentUri,
                    EnrollableType.FACE,
                    it.attributes.algorithm.toString()
                )
            )

        }

        metadataContainer.authenticationMethods.fingerprint?.let {
            availableFactors.add(
                SignatureEnrollableFactor(
                    it.enrollmentUri,
                    EnrollableType.FINGERPRINT,
                    it.attributes.algorithm.toString()
                )
            )

        }

        metadataContainer.authenticationMethods.totp?.let {totp ->
            totp.attributes?.let {
                availableFactors.add(
                    CloudTOTPEnrollableFactor(
                        totp.enrollmentUri,
                        EnrollableType.TOTP,
                        id,
                        it.algorithm.toString(),
                        it.secret,
                        it.digits,
                        it.period
                    )
                )
            }
        }
    }
}

@Serializable
internal data class MetadataContainer(
    val authenticationMethods: AuthenticationMethods,
    val registrationUri: URL,
    val serviceName: String,
    @SerialName("featureFlags") val features: ArrayList<String> = ArrayList(),
    @SerialName("customAttributes") val custom: Map<String, String> = HashMap(),
    @SerialName("themeAttributes") val theme: Map<String, String> = HashMap(),
)

@Serializable
internal data class Metadata(
    val id : String = "",
    val registrationUri: URL,
    val serviceName: String,
    val transactionUri: URL,
    @SerialName("featureFlags") val features: ArrayList<String> = ArrayList(),
    @SerialName("customAttributes") val custom: Map<String, String> = HashMap(),
    @SerialName("themeAttributes") val theme: Map<String, String> = HashMap(),
    val availableFactors: ArrayList<EnrollableFactor> = ArrayList()
)

@Serializable
internal data class AuthenticationMethods(
    @SerialName("signature_face") val face: Signature?,
    @SerialName("signature_fingerprint") val fingerprint: Signature?,
    @SerialName("signature_userPresence") val userPresence: Signature?,
    val totp: Totp?
)

@Serializable
internal data class Version(
    val number: String,
    val platform: String
)

@Serializable
internal data class Signature(
    val enabled: Boolean,
    val enrollmentUri: URL,
    val attributes: SignatureAttributes
)

@Serializable
internal data class Totp(
    val enabled: Boolean,
    val enrollmentUri: URL,
    val id: String? = null,
    val attributes: TotpAttributes? = null
)

@Serializable
internal data class TotpAttributes(
    @Serializable(with = HashAlgorithmTypeSerializer::class)
    val algorithm: HashAlgorithmType,
    val secret: String,
    val digits: Int,
    val period: Int
)

@Serializable
internal data class SignatureAttributes(
    @Serializable(with = HashAlgorithmTypeSerializer::class)
    val algorithm: HashAlgorithmType,
)