/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem.model

import com.ibm.security.verifysdk.mfa.EnrollableFactor
import com.ibm.security.verifysdk.mfa.EnrollableType
import com.ibm.security.verifysdk.mfa.HashAlgorithmType
import com.ibm.security.verifysdk.mfa.SignatureEnrollableFactor
import com.ibm.security.verifysdk.core.serializer.URLSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.net.URL

@Serializable
internal data class Metadata(
    @Serializable(with = URLSerializer::class)
    val registrationUri: URL,
    @Serializable(with = URLSerializer::class)
    val transactionUri: URL,
    @Serializable(with = URLSerializer::class)
    val signatureUri: URL,
    @Serializable(with = URLSerializer::class)
    val totpUri: URL,
    @Serializable(with = URLSerializer::class)
    val qrloginUri: URL,
    val serviceName: String,
    val availableFactors: ArrayList<EnrollableFactor>,
    val theme: Map<String, String>,
    val features: ArrayList<String>
)


@Serializable
internal data class DetailsData @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("authntrxn_endpoint")
    @Serializable(with = URLSerializer::class)
    val authntrxnEndpoint: URL,
    @JsonNames("metadata")
    val metadataService: MetadataService,
    @JsonNames("discovery_mechanisms")
    val discoveryMechanisms: ArrayList<DiscoveredMechanisms> = arrayListOf(),
    @JsonNames("enrollment_endpoint")
    @Serializable(with = URLSerializer::class)
    val enrollmentEndpoint: URL,
    @JsonNames("qrlogin_endpoint")
    @Serializable(with = URLSerializer::class)
    var qrloginEndpoint: URL,
    @JsonNames("hotp_shared_secret_endpoint")
    @Serializable(with = URLSerializer::class)
    val hotpSharedSecretEndpoint: URL,
    @JsonNames("totp_shared_secret_endpoint")
    @Serializable(with = URLSerializer::class)
    val totpSharedSecretEndpoint: URL,
    @JsonNames("version")
    val version: String,
    @JsonNames("token_endpoint")
    @Serializable(with = URLSerializer::class)
    val tokenEndpoint: URL,
    val theme: Map<String, String>? = null
) {

    val serviceName: String = metadataService.serviceName ?: enrollmentEndpoint.host
    var availableFactors: ArrayList<EnrollableFactor> = java.util.ArrayList()

    init {
        if (discoveryMechanisms.contains(DiscoveredMechanisms.USERPRESENCE)) {
            availableFactors.add(
                SignatureEnrollableFactor(
                    enrollmentEndpoint,
                    EnrollableType.USER_PRESENCE,
                    HashAlgorithmType.SHA512.toString()
                )
            )
        }

        if (discoveryMechanisms.contains(DiscoveredMechanisms.FINGERPRINT)) {
            availableFactors.add(
                SignatureEnrollableFactor(
                    enrollmentEndpoint,
                    EnrollableType.FINGERPRINT,
                    HashAlgorithmType.SHA512.toString()
                )
            )
        }

        if (discoveryMechanisms.contains(DiscoveredMechanisms.TOTP)) {
            availableFactors.add(
                OnPremiseTOTPEnrollableFactor(
                    totpSharedSecretEndpoint
                )
            )
        }
    }
}

@Serializable
internal enum class DiscoveredMechanisms(val value: String) {
    @SerialName("urn:ibm:security:authentication:asf:mechanism:totp")
    TOTP("urn:ibm:security:authentication:asf:mechanism:totp"),
    @SerialName("urn:ibm:security:authentication:asf:mechanism:mobile_user_approval:fingerprint")
    FINGERPRINT("urn:ibm:security:authentication:asf:mechanism:mobile_user_approval:fingerprint"),
    @SerialName("urn:ibm:security:authentication:asf:mechanism:mobile_user_approval:user_presence")
    USERPRESENCE("urn:ibm:security:authentication:asf:mechanism:mobile_user_approval:user_presence")
}

@Serializable
@OptIn(ExperimentalSerializationApi::class)
internal data class MetadataService (
    @JsonNames("service_name" )
    val serviceName: String?
)