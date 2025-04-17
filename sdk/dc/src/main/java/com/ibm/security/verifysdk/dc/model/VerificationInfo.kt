/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Data class representing the information required for a verification process.
 *
 * This class contains details related to the verification process, including the role, state,
 * associated credentials, proof request and response, connection details, and other relevant
 * metadata.
 *
 * @property id The unique identifier for the verification process.
 * @property role The role of the entity in the verification process (e.g., verifier, prover).
 * @property state The current state of the verification process (e.g., pending, completed).
 * @property verifierDid The DID (Decentralized Identifier) of the verifier.
 * @property connection Information about the connection related to the verification process.
 * @property allowProofRequestOverride Flag indicating whether proof request overrides are allowed. This field may be null.
 * @property proofSchemaId The ID of the proof schema associated with the verification. This field may be null.
 * @property proofRequest The proof request for the verification, if applicable. This field may be null.
 * @property proofResponse The proof response received during the verification process, if applicable. This field may be null.
 * @property info Additional information about the verification, represented as a JSON element. This field may be null.
 * @property proofDisplay A string representing the display format of the proof. This field may be null.
 * @property ariesMessage The Aries message related to the verification process. This field may be null.
 * @property properties A map of additional properties associated with the verification process. This field may be null.
 * @property timestamps A map of timestamps related to various stages of the verification process. This field may be null.
 *
 * @since 3.0.7
 */
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
    val info: JsonElement? = null,

    @SerialName("proof_display")
    val proofDisplay: String? = null,

    @SerialName("aries_message")
    val ariesMessage: String? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null
)