/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the arguments required to update a verification request.
 *
 * @property state The new state of the verification process.
 * @property properties Optional additional properties related to the verification.
 * @property choices Optional user credential choices for the verification.
 * @property selfAttestedAttributes Optional self-attested attributes provided by the user.
 * @property comment Optional comment or note related to the verification update.
 * @property proofSchemaId Optional schema ID for the proof request.
 * @property proofRequest Optional proof request data associated with the verification.
 * @property allowProofRequestOverride Flag indicating whether the proof request can be overridden.
 * @property returnProofDisplay Flag indicating whether the proof display should be returned.
 *
 * @since 3.0.7
 */
@Serializable
data class UpdateVerificationArgs(

    @SerialName( "state")
    val state: VerificationState,

    @SerialName( "properties")
    val properties: Map<String, String>? = null,

    @SerialName( "choices")
    val choices: UserCredChoices? = null,

    @SerialName( "self_attested_attributes")
    val selfAttestedAttributes: Map<String, String>? = null,

    @SerialName( "comment")
    val comment: String? = null,

    @SerialName( "proof_schema_id")
    val proofSchemaId: String? = null,

    @SerialName( "proof_request")
    val proofRequest: ProofRequest? = null,

    @SerialName( "allow_proof_request_override")
    val allowProofRequestOverride: Boolean? = null,

    @SerialName( "return_proof_display")
    val returnProofDisplay: Boolean? = null
)
