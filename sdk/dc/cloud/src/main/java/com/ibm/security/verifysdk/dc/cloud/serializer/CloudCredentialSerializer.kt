/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.serializer

import com.ibm.security.verifysdk.dc.cloud.model.CloudCredentialDescriptor
import com.ibm.security.verifysdk.dc.cloud.model.IndyCredential
import com.ibm.security.verifysdk.dc.cloud.model.JsonLdCredential
import com.ibm.security.verifysdk.dc.cloud.model.MDocCredential
import com.ibm.security.verifysdk.dc.core.CredentialDescriptor
import com.ibm.security.verifysdk.dc.core.CredentialFormat
import com.ibm.security.verifysdk.dc.core.CredentialRole
import com.ibm.security.verifysdk.dc.core.CredentialState
import com.ibm.security.verifysdk.dc.core.DID
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
object CloudCredentialSerializer : KSerializer<CloudCredentialDescriptor> {

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
     * Deserializes a [CloudCredentialSerializer] from JSON.
     *
     * The method identifies the credential format and constructs the appropriate credential type.
     *
     * @param decoder The decoder used to parse JSON input.
     * @return A deserialized [CredentialDescriptor] object.
     * @throws SerializationException If required fields are missing or have invalid values.
     *
     * @since 3.0.7
     */
    override fun deserialize(decoder: Decoder): CloudCredentialDescriptor {
        require(decoder is JsonDecoder)
        val jsonObject = decoder.decodeJsonElement().jsonObject

        val format = jsonObject["format"]?.jsonPrimitive?.content?.let {
            CredentialFormat.fromSerialName(it)
        } ?: throw SerializationException("Missing or invalid format")

        return when (format) {
            CredentialFormat.INDY -> IndyCredential.from(jsonObject)
            CredentialFormat.JSON_LD -> JsonLdCredential.from(jsonObject)
            CredentialFormat.MDOC -> MDocCredential.from(jsonObject)

            else -> throw NotImplementedError("CredentialFormat $format not supported")
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
    override fun serialize(encoder: Encoder, value: CloudCredentialDescriptor) {
        require(encoder is JsonEncoder)

        val jsonObject = when (value) {
            is IndyCredential -> value.toJsonObject(json)
            is JsonLdCredential -> value.toJsonObject(json)
            is MDocCredential -> value.toJsonObject(json)
            else -> throw NotImplementedError("Unsupported CloudCredentialDescriptor type")
        }
        encoder.encodeJsonElement(jsonObject)
    }
}