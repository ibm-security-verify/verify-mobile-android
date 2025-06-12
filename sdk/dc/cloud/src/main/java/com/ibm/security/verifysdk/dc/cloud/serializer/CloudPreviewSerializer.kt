/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.serializer

import com.ibm.security.verifysdk.core.serializer.DefaultJson
import com.ibm.security.verifysdk.dc.cloud.model.CloudPreviewDescriptor
import com.ibm.security.verifysdk.dc.cloud.model.CredentialPreviewInfo
import com.ibm.security.verifysdk.dc.cloud.model.VerificationPreviewInfo
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

object CloudPreviewSerializer : KSerializer<CloudPreviewDescriptor> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CloudPreviewDescriptor") {
        element<String>("id")
        element<String>("url")
        element<String?>("label", isOptional = true)
        element<String?>("comment", isOptional = true)
        element<JsonElement?>("jsonRepresentation", isOptional = true)
    }

    private fun getFormat(jsonObject: JsonObject): String {
        if (jsonObject.toString().contains("request_presentations~attach"))
            return "verification"

        if (jsonObject.toString().contains("requests~attach"))
            return "invitation"

        return "unknown"
    }

    override fun deserialize(decoder: Decoder): CloudPreviewDescriptor {

        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This deserializer only works with JSON format.")

        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        return when (val format = getFormat(jsonObject)) {
            "invitation" -> {
                jsonDecoder.json.decodeFromJsonElement<CredentialPreviewInfo>(jsonObject)
            }
            "verification" -> {
                jsonDecoder.json.decodeFromJsonElement<VerificationPreviewInfo>(jsonObject)
            }
            else -> {
                throw IllegalArgumentException("Unknown format: $format")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: CloudPreviewDescriptor) {

        encoder as? JsonEncoder
            ?: throw SerializationException("This serializer only works with Json")

        when (value) {
            is CredentialPreviewInfo -> {
                val json = DefaultJson.encodeToJsonElement(CredentialPreviewInfo.serializer(), value)
                encoder.encodeJsonElement(json)
            }

            is VerificationPreviewInfo -> {
                val json = DefaultJson.encodeToJsonElement(VerificationPreviewInfo.serializer(), value)
                encoder.encodeJsonElement(json)
            }
        }
    }
}