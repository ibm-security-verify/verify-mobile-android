package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.SerialName


@ExperimentalSerializationApi
@Serializable
data class CredentialInfo (

    @SerialName("id")
    val id: String,

    @SerialName("role")
    val role: CredentialRole,

    @SerialName("state")
    val state: CredentialState,

    @SerialName("issuer_did")
    val issuerDid: String,

    @SerialName("format")
    val format: String,

    @SerialName("connection")
    val connection: ConnectionInfo,

    @SerialName("proposal")
    val proposal: JsonElement? = null,

    @SerialName("preview")
    val preview: JsonElement? = null,

    @SerialName("offer")
    val offer: CredentialInfoOffer? = null,

    @SerialName("schema_name")
    val schemaName: String? = null,

    @SerialName("schema_version")
    val schemaVersion: String? = null,

    @SerialName("schema_id")
    val schemaId: String? = null,

    @SerialName("cred_def_id")
    val credDefId: String? = null,

    @SerialName("cred_json")
    val credJson: JsonElement? = null,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null,

    @SerialName("aries_message")
    val ariesMessage: JsonElement? = null,

    @SerialName("properties")
    val properties: Map<String, JsonElement>? = null

)