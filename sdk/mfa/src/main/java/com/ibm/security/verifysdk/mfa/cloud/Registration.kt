/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.mfa.EnrollableFactor
import com.ibm.security.verifysdk.mfa.EnrollableType
import com.ibm.security.verifysdk.mfa.HashAlgorithmType
import com.ibm.security.verifysdk.mfa.SignatureEnrollableFactor
import com.ibm.security.verifysdk.mfa.URLSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
internal data class Registration(
    val accessToken: String,
    val expiresIn: Int,
    val id: String,
    val metadata: Metadata,
    val refreshToken: String,
    val version: Version
) {
    @Serializable(with = URLSerializer::class)
    val transactionUri: URL =
        URL(metadata.registrationUri.toString().replace("registration", "$id/verifications"))

    var availableFactor: ArrayList<EnrollableFactor> = java.util.ArrayList()

    init {
        for (authenticationMethod in metadata.authenticationMethods) {
            authenticationMethod.userPresence?.let {
                availableFactor.add(
                    SignatureEnrollableFactor(
                        it.enrollmentUri,
                        EnrollableType.USER_PRESENCE,
                        it.attributes.algorithm.toString()
                    )
                )
            }

            authenticationMethod.face?.let {
                availableFactor.add(
                    SignatureEnrollableFactor(
                        it.enrollmentUri,
                        EnrollableType.FACE,
                        it.attributes.algorithm.toString()
                    )
                )

            }

            authenticationMethod.fingerprint?.let {
                availableFactor.add(
                    SignatureEnrollableFactor(
                        it.enrollmentUri,
                        EnrollableType.FINGERPRINT,
                        it.attributes.algorithm.toString()
                    )
                )

            }

            authenticationMethod.totp?.let {
                availableFactor.add(
                    CloudTOTPEnrollableFactor(
                        it.enrollmentUri,
                        EnrollableType.TOTP,
                        id,
                        it.attributes.algorithm.toString(),
                        it.attributes.secret,
                        it.attributes.digits,
                        it.attributes.period
                    )
                )

            }
        }

    }
}

@Serializable
internal data class Metadata(
    val authenticationMethods: ArrayList<AuthenticationMethods>,
    @Serializable(with = URLSerializer::class)
    val registrationUri: URL,
    val serviceName: String,
    @SerialName("featureFlags") val features: ArrayList<String> = ArrayList(),
    @SerialName("customAttributes") val custom: Map<String, String> = HashMap(),
    @SerialName("themeAttributes") val theme: Map<String, String> = HashMap(),
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
    @Serializable(with = URLSerializer::class)
    val enrollmentUri: URL,
    val attributes: Attributes
)

@Serializable
internal data class Attributes(
    val algorithm: HashAlgorithmType,
    val secret: String,
    val digits: Int,
    val period: Int
)

@Serializable
internal data class Totp(
    val enabled: Boolean,
    @Serializable(with = URLSerializer::class)
    val enrollmentUri: URL,
    val id: String,
    val attributes: Attributes
)