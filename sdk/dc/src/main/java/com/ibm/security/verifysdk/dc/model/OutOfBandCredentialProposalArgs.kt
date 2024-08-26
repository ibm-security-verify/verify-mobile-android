/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class OutOfBandCredentialProposalArgs (

    @SerialName("attributes")
    val attributes: Map<String, String>,

    @SerialName("comment")
    val comment: String? = null,

    @SerialName("credential_proposal")
    val credentialProposal: JsonElement? = null,

    @SerialName("cred_def_id")
    val credDefId: String? = null,

    @SerialName("schema_id")
    val schemaId: String? = null,

    @SerialName("schema_name")
    val schemaName: String? = null,

    @SerialName("schema_version")
    val schemaVersion: String? = null
)