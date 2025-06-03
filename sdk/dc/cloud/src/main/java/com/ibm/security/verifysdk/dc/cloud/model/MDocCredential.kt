/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ibm.security.verifysdk.core.extension.toJsonElement
import com.ibm.security.verifysdk.dc.cloud.serializer.CloudCredentialSerializer.json
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
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64

/**
 * Represents a Credential in the MDoc format (mobile document format) with associated details and
 * metadata.
 *
 * This class provides information about the credential, including its ID, role, state, issuer, format,
 * connection details, and additional properties such as name and icon.
 *
 * @property id The unique identifier of the credential.
 * @property role The role of the credential (e.g., holder, issuer).
 * @property state The current state of the credential.
 * @property issuerDid The Decentralized Identifier (DID) of the credential issuer.
 * @property format The format of the credential.
 * @property jsonRepresentation The JSON representation of the credential, if available.
 * @property connection Information about the connection used for issuing the credential.
 * @property properties A map of additional properties associated with the credential, such as name and icon.
 *
 * @since 3.0.7
 */
@Serializable
data class MDocCredential(
    override val id: String,
    override val role: CredentialRole,
    override val state: CredentialState,
    override val issuerDid: DID,
    override val format: CredentialFormat,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    override val connection: ConnectionInfo,
    val properties: Map<String, JsonElement>
) : CloudCredentialDescriptor() {

    override fun getAgentName(): String = connection.remote.name
    override fun getAgentUrl(): String = connection.remote.url
    override fun getFriendlyName(): String = properties["name"]?.jsonPrimitive.toString()

    val icon: Bitmap?
        get() {
            val icon = properties["icon"]
            val value = icon?.jsonPrimitive?.toString()
            if (!value.isNullOrEmpty()) {
                val components = value.split("base64,")
                if (components.size == 2) {
                    val data = Base64.getDecoder().decode(components[1])
                    return BitmapFactory.decodeByteArray(data, 0, data.size)
                }
            }
            return null
        }

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
            val connection = json.decodeFromJsonElement<ConnectionInfo>(
                jsonObject["connection"]
                    ?: throw SerializationException("Missing connection")
            )
            val properties = json.decodeFromJsonElement<Map<String, JsonElement>>(
                jsonObject["properties"]
                    ?: throw SerializationException("Missing properties")
            )

            return MDocCredential(
                id = id,
                role = role,
                state = state,
                issuerDid = issuerDid,
                format = format,
                jsonRepresentation = credJson,
                connection = connection,
                properties = properties
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
        put("connection", json.encodeToJsonElement(ConnectionInfo.serializer(), connection))
        put("properties", json.encodeToJsonElement(properties))
    }
}