/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.serializer

import com.ibm.security.verifysdk.core.extension.toJsonElement
import com.ibm.security.verifysdk.dc.cloud.model.ConnectionInfo
import com.ibm.security.verifysdk.dc.cloud.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.cloud.model.CredentialFormat
import com.ibm.security.verifysdk.dc.cloud.model.CredentialFormat.Companion.serialName
import com.ibm.security.verifysdk.dc.cloud.model.CredentialRole
import com.ibm.security.verifysdk.dc.cloud.model.CredentialState
import com.ibm.security.verifysdk.dc.cloud.model.DID
import com.ibm.security.verifysdk.dc.cloud.model.IndyCredential
import com.ibm.security.verifysdk.dc.cloud.model.JsonLdCredential
import com.ibm.security.verifysdk.dc.cloud.model.MDocCredential
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Custom serializer for handling credential serialization and deserialization.
 *
 * This serializer supports multiple credential formats, including:
 * - [IndyCredential] (Indy-based credentials)
 * - [JsonLdCredential] (JSON-LD based credentials)
 * - [MDocCredential] (Mobile Document credentials)
 *
 * It ensures proper encoding and decoding of credential data while handling different credential structures.
 *
 * @since 3.0.7
 */
@OptIn(ExperimentalSerializationApi::class)
object CredentialSerializer : KSerializer<CredentialDescriptor> {

    val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Credential") {
        element("format", CredentialFormat.serializer().descriptor)
        element<String>("id")
        element<CredentialRole>("role")
        element<CredentialState>("state")
        element<DID>("issuerDid")
        element<JsonElement>("jsonRepresentation")
    }

    /**
     * Deserializes a [CredentialDescriptor] from JSON.
     *
     * The method identifies the credential format and constructs the appropriate credential type.
     *
     * @param decoder The decoder used to parse JSON input.
     * @return A deserialized [CredentialDescriptor] object.
     * @throws SerializationException If required fields are missing or have invalid values.
     *
     * @since 3.0.7
     */
    override fun deserialize(decoder: Decoder): CredentialDescriptor {
        require(decoder is JsonDecoder)
        val jsonObject = decoder.decodeJsonElement().jsonObject

        val format = jsonObject["format"]?.jsonPrimitive?.content?.let {
            CredentialFormat.fromSerialName(it)
        } ?: throw SerializationException("Missing or invalid format")

        val id = jsonObject["id"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing id")

        val role = jsonObject["role"]?.jsonPrimitive?.content?.let {
            CredentialRole.fromSerialName(it)
        } ?: throw SerializationException("Missing role")
        val state = jsonObject["state"]?.jsonPrimitive?.content?.let {
            CredentialState.fromSerialName(it)
        } ?: throw SerializationException("Missing state")

        val issuerDid: DID = jsonObject["issuer_did"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing issuer_did")

        val jsonRepresentation =
            jsonObject["cred_json"] ?: throw SerializationException("Missing cred_json")

        val credentialDefinitionId = jsonObject["cred_def_id"]?.jsonPrimitive?.content ?: ""
        val schemaName = jsonObject["schema_name"]?.jsonPrimitive?.content ?: ""
        val schemaVersion = jsonObject["schema_version"]?.jsonPrimitive?.content ?: ""

        val connection = jsonObject["connection"]?.let {
            json.decodeFromJsonElement<ConnectionInfo>(it)
        } ?: throw SerializationException("Missing connection")
        val properties = jsonObject["properties"]?.let {
            json.decodeFromJsonElement<Map<String, JsonElement>>(it)
        } ?: throw SerializationException("Missing properties")

        return when (format) {
            CredentialFormat.INDY ->
                IndyCredential(
                    id = id,
                    format = format,
                    issuerDid = issuerDid,
                    role = role,
                    state = state,
                    jsonRepresentation = jsonRepresentation,
                    credentialDefinitionId = credentialDefinitionId,
                    schemaName = schemaName,
                    schemaVersion = schemaVersion,
                    connection = connection,
                    properties = properties
                )

            CredentialFormat.JSON_LD -> JsonLdCredential(
                id = id,
                format = format,
                issuerDid = issuerDid,
                jsonRepresentation = jsonRepresentation,
                role = role,
                state = state,
                connection = connection,
                properties = properties
            )

            CredentialFormat.MDOC -> MDocCredential(
                id = id,
                format = format,
                issuerDid = issuerDid,
                jsonRepresentation = jsonRepresentation,
                role = role,
                state = state,
                connection = connection,
                properties = properties
            )
        }
    }

    /**
     * Serializes a [CredentialDescriptor] into JSON.
     *
     * The method encodes the credential properties and structures them appropriately for each credential type.
     *
     * @param encoder The encoder used for JSON output.
     * @param value The [CredentialDescriptor] to serialize.
     * @throws SerializationException If serialization fails.
     *
     * @since 3.0.7
     */
    override fun serialize(encoder: Encoder, value: CredentialDescriptor) {
        require(encoder is JsonEncoder)
        val jsonObject = buildJsonObject {
            put("format", value.format.serialName.toJsonElement())
            put("id", value.id.toJsonElement())
            put("role", value.role.value.toJsonElement())
            put("issuer_did", value.issuerDid.toJsonElement())
            put("state", value.state.value.toJsonElement())
            put("cred_json", value.jsonRepresentation.toJsonElement())

            when (value) {
                is IndyCredential -> {
                    put("cred_def_id", value.credentialDefinitionId.toJsonElement())
                    put("schema_name", value.schemaName.toJsonElement())
                    put("schema_version", value.schemaVersion.toJsonElement())
                    put(
                        "connection",
                        json.encodeToJsonElement(ConnectionInfo.serializer(), value.connection)
                    )
                    put("properties", json.encodeToJsonElement(value.properties))
                }

                is JsonLdCredential -> {
                    put(
                        "connection",
                        json.encodeToJsonElement(ConnectionInfo.serializer(), value.connection)
                    )
                    put("properties", json.encodeToJsonElement(value.properties))
                }

                is MDocCredential -> {
                    put(
                        "connection",
                        json.encodeToJsonElement(ConnectionInfo.serializer(), value.connection)
                    )
                    put("properties", json.encodeToJsonElement(value.properties))
                }
            }
        }
        encoder.encodeJsonElement(jsonObject)
    }
}