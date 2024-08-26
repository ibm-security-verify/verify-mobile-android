/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class UpdateCredentialArgs (

    @SerialName("state")
    val state: CredentialState,

    @SerialName("ledger_name")
    val ledgerName: String? = null,

    @SerialName("schema_name")
    val schemaName: String? = null,

    @SerialName("schema_version")
    val schemaVersion: String? = null,

    @SerialName("schema_id")
    val schemaId: String? = null,

    @SerialName("cred_def_id")
    val credDefId: String? = null,

    @SerialName("credential_definition_id")
    val credentialDefinitionId: String? = null,

    @SerialName("comment")
    val comment: String? = null,

    @SerialName("attributes")
    val attributes: Map<String, JsonElement>? = null,

    @SerialName("credential_preview")
    val credentialPreview: JsonElement? = null,

    @SerialName("properties")
    val properties: Map<String, JsonElement>? = null,

    @SerialName("credential_proposal")
    val credentialProposal: JsonElement? = null
)
