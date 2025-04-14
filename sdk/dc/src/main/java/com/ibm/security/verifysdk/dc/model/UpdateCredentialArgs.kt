/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Data class representing the arguments required to update a credential.
 *
 * The `UpdateCredentialArgs` class encapsulates all the necessary information for updating a credential,
 * including its state, associated schema, definition, attributes, and other optional properties.
 * This class is used to provide the required fields for modifying or creating a credential update request.
 *
 * @property state The [CredentialState] representing the current state of the credential. This is a mandatory field.
 * @property ledgerName An optional name of the ledger associated with the credential. This field may be null.
 * @property schemaName An optional name of the schema associated with the credential. This field may be null.
 * @property schemaVersion An optional version of the schema associated with the credential. This field may be null.
 * @property schemaId An optional identifier for the schema associated with the credential. This field may be null.
 * @property credDefId An optional identifier for the credential definition. This field may be null.
 * @property credentialDefinitionId An optional identifier for the credential definition. This field may be null.
 * @property comment An optional comment or note related to the credential. This field may be null.
 * @property attributes A map of attributes related to the credential, where the key is the attribute name
 *                      and the value is the attribute value. This field may be null.
 * @property credentialPreview A JSON representation of the credential preview. This field may be null.
 * @property properties A map of key-value pairs representing additional properties associated with the credential.
 *                      This field may be null.
 * @property credentialProposal A JSON representation of the credential proposal. This field may be null.
 *
 * @since 3.0.4
 */
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
