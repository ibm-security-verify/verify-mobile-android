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
 * Represents an Indy-based verifiable credential.
 *
 * This class models a credential issued using the Indy framework, containing metadata such as
 * the credential definition, schema details, issuer information, and additional properties.
 *
 * @property credentialDefinitionId The identifier of the credential definition used for issuance.
 * @property schemaName The name of the schema associated with this credential.
 * @property schemaVersion The version of the schema used.
 * @property connection The connection information related to the credential issuance.
 * @property properties A map of additional properties related to the credential.
 *
 * @since 3.0.7
 */
@Serializable
data class IndyCredential(
    override val id: String,
    override val role: CredentialRole,
    override val state: CredentialState,
    override val issuerDid: DID,
    override val format: CredentialFormat,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    @SerialName("cred_def_id")
    val credentialDefinitionId: String,
    @SerialName("schema_name")
    val schemaName: String,
    @SerialName("schema_version")
    val schemaVersion: String,
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
        fun from(jsonObject: JsonObject): IndyCredential {
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
            val credDefId = jsonObject["cred_def_id"]?.jsonPrimitive?.content ?: ""
            val schemaName = jsonObject["schema_name"]?.jsonPrimitive?.content ?: ""
            val schemaVersion = jsonObject["schema_version"]?.jsonPrimitive?.content ?: ""
            val connection = DefaultJson.decodeFromJsonElement<ConnectionInfo>(
                jsonObject["connection"]
                    ?: throw SerializationException("Missing connection")
            )
            val properties = DefaultJson.decodeFromJsonElement<Map<String, JsonElement>>(
                jsonObject["properties"]
                    ?: throw SerializationException("Missing properties")
            )

            return IndyCredential(
                id = id,
                format = format,
                issuerDid = issuerDid,
                role = role,
                state = state,
                jsonRepresentation = credJson,
                credentialDefinitionId = credDefId,
                schemaName = schemaName,
                schemaVersion = schemaVersion,
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

        put("cred_def_id", credentialDefinitionId.toJsonElement())
        put("schema_name", schemaName.toJsonElement())
        put("schema_version", schemaVersion.toJsonElement())
        put("connection", json.encodeToJsonElement(ConnectionInfo.serializer(), connection))
        put("properties", json.encodeToJsonElement(properties))
    }
}