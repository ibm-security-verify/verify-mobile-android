/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
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
    val proofRequest: ProofRequestArgs? = null,

    @SerialName( "allow_proof_request_override")
    val allowProofRequestOverride: Boolean? = null,

    @SerialName( "return_proof_display")
    val returnProofDisplay: Boolean? = null
)
