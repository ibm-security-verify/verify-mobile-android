/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class CreateVerificationArgs(

    @SerialName( "to")
    val to: ToConnection? = null,

    @SerialName( "comment")
    val comment: String? = null,

    @SerialName( "state")
    val state: VerificationState? = null,

    @SerialName( "proof_schema_id")
    val proofSchemaId: String? = null,

    @SerialName( "proof_request")
    val proofRequest: ProofRequestArgs? = null,

    @SerialName( "properties")
    val properties: Map<String, String>? = null,

    @SerialName( "direct_route")
    val directRoute: Boolean? = null,

    @SerialName( "aries_version")
    val ariesVersion: String? = null,

    @SerialName( "allow_proof_request_override")
    val allowProofRequestOverride: Boolean? = null,

    @SerialName( "non_revocation_times")
    val nonRevocationTimes: Map<String, String>? = null
)