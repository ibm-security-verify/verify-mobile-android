/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.device.model

import com.ibm.security.verifysdk.core.extension.toJsonElement
import com.ibm.security.verifysdk.dc.core.CredentialFormat
import com.ibm.security.verifysdk.dc.core.CredentialFormat.Companion.serialName
import com.ibm.security.verifysdk.dc.core.CredentialRole
import com.ibm.security.verifysdk.dc.core.CredentialState
import com.ibm.security.verifysdk.dc.core.DID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class SDJwtCredential(
    override val id: String,
    override val role: CredentialRole = CredentialRole.HOLDER,
    override val state: CredentialState = CredentialState.ISSUED,
    override val issuerDid: DID,
    override val format: CredentialFormat = CredentialFormat.SDJWT,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    val keyName: String?
) : DeviceCredentialDescriptor() {

    companion object {
        fun from(jsonObject: JsonObject): MDocCredential {
            val id = jsonObject["id"]?.jsonPrimitive?.content
                ?: throw SerializationException("Missing id")
            val role = jsonObject["role"]?.jsonPrimitive?.content
                ?.let { CredentialRole.fromSerialName(it) }
                ?: throw SerializationException("Missing role")
            val state = jsonObject["state"]?.jsonPrimitive?.content
                ?.let { CredentialState.fromSerialName(it) }
                ?: throw SerializationException("Missing state")
            val issuerDid = jsonObject["issuer_did"]?.jsonPrimitive?.content
                ?: throw SerializationException("Missing issuer_did")
            val format = jsonObject["format"]?.jsonPrimitive?.content
                ?.let { CredentialFormat.fromSerialName(it) }
                ?: throw SerializationException("Missing format")
            val credJson = jsonObject["cred_json"]
                ?: throw SerializationException("Missing cred_json")
            val keyName = jsonObject["keyName"]?.jsonPrimitive?.content
                ?: throw SerializationException("Missing keyName")

            return MDocCredential(
                id = id,
                role = role,
                state = state,
                issuerDid = issuerDid,
                format = format,
                jsonRepresentation = credJson,
                keyName = keyName
            )
        }
    }

    fun toJsonObject(json: Json): JsonObject = buildJsonObject {
        put("format", format.serialName.toJsonElement())
        put("id", id.toJsonElement())
        put("role", role.value.toJsonElement())
        put("issuer_did", issuerDid.toJsonElement())
        put("state", state.value.toJsonElement())
        put("cred_json", jsonRepresentation.toJsonElement())
        put("keyName", keyName.toJsonElement())
    }
}