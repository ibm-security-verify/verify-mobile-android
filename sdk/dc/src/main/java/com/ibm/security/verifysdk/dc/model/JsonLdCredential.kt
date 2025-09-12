/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ibm.security.verifysdk.core.extension.toJsonElement
import com.ibm.security.verifysdk.core.serializer.DefaultJson
import com.ibm.security.verifysdk.dc.model.CredentialFormat.Companion.serialName
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
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.Base64

/**
 * Represents a JSON-LD formatted credential used in decentralized identity systems.
 *
 * This class contains details about a credential issued in JSON-LD format, including its
 * metadata, issuer information, connection details, and properties.
 *
 * @property id The unique identifier of the credential.
 * @property role The role of the credential in the exchange process (e.g., issuer or holder).
 * @property state The current state of the credential (e.g., issued, revoked).
 * @property issuerDid The Decentralized Identifier (DID) of the credential issuer.
 * @property format The format of the credential, which in this case is JSON-LD.
 * @property jsonRepresentation The raw JSON representation of the credential.
 * @property connection Information about the connection associated with the credential exchange.
 * @property properties A map containing additional metadata and attributes related to the credential.
 *
 * @sine 3.0.4
 */
@Serializable
data class JsonLdCredential(
    override val id: String,
    override val role: CredentialRole,
    override val state: CredentialState,
    @SerialName("issuer_did")
    override val issuerDid: DID,
    override val format: CredentialFormat,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    override val connection: ConnectionInfo,
    val properties: Map<String, JsonElement>
) : CredentialDescriptor {

    override fun getAgentName(): String = connection.remote.name
    override fun getAgentUrl(): String = connection.remote.url
    override fun getFriendlyName(): String = properties["name"]?.jsonPrimitive.toString()

    val offerTime: Instant
        get() {
            val time = properties["time"]?.jsonPrimitive?.toString()
            return if (time != null) {
                try {
                    Instant.parse(time)
                } catch (e: DateTimeParseException) {
                    Instant.now()
                }
            } else {
                Instant.now()
            }
        }

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
        fun from(jsonObject: JsonObject): JsonLdCredential {
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
            val connection = DefaultJson.decodeFromJsonElement<ConnectionInfo>(
                jsonObject["connection"]
                    ?: throw SerializationException("Missing connection")
            )
            val properties = DefaultJson.decodeFromJsonElement<Map<String, JsonElement>>(
                jsonObject["properties"]
                    ?: throw SerializationException("Missing properties")
            )

            return JsonLdCredential(
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