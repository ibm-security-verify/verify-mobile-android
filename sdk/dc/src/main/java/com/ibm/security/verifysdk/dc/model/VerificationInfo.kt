/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationInfo (

    @SerialName("id")
    val id: String,

    @SerialName("role")
    val role: VerificationRole,

    @SerialName("state")
    val state: VerificationState,

    @SerialName("verifier_did")
    val verifierDid: String,

    @SerialName("connection")
    val connection: ConnectionInfo,

    @SerialName("allow_proof_request_override")
    val allowProofRequestOverride: Boolean? = null,

    @SerialName("proof_schema_id")
    val proofSchemaId: String? = null,

    @SerialName("proof_request")
    val proofRequest: ProofRequest? = null,

    @SerialName("proof_response")
    val proofResponse: ProofResponse? = null,

    @SerialName("info")
    val info: ProofView? = null,

    @SerialName("proof_display")
    val proofDisplay: String? = null,

    @SerialName("aries_message")
    val ariesMessage: String? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,

    @SerialName("choices")
    val choices: ProofChoices? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null

)