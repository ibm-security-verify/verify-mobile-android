/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.dc.core.WalletError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.util.Base64

/**
 * Represents a preview of an invitation containing metadata and associated data for credential issuance or proof presentation.
 *
 * This class is used to encapsulate the details of an invitation, such as its identifier, label, URL, and other related metadata.
 * It is particularly useful for displaying a summary of the invitation before it is fully processed.
 *
 * @property id The unique identifier for the invitation.
 * @property label The optional label associated with the invitation.
 * @property url The URL that can be used to access the invitation.
 * @property comment The optional comment that can be added to the invitation.
 * @property jsonRepresentation A base64-encoded JSON representation of the invitation (optional).
 * @property type The type of the invitation, which could either be an offer for credential issuance or a request for proof presentation.
 * @property formats A list of formats that the invitation supports (e.g., "jwt", "ldp").
 * @property documentTypes A list of document types related to the invitation, used for filtering or categorization.
 *
 * @since 3.0.7
 */
@Serializable(InvitationPreviewInfo.Companion::class)
internal data class InvitationPreviewInfo(
    override val id: String,
    override val label: String? = null,
    override val url: String,
    override val comment: String? = null,
    override val jsonRepresentation: JsonElement? = null,
    val type: InvitationType,
    val formats: List<String>,
    internal val documentTypes: List<String> = emptyList()
) : PreviewDescriptor {

    @Serializable
    enum class InvitationType {
        OFFER_CREDENTIAL,
        REQUEST_PRESENTATION
    }

    companion object : KSerializer<InvitationPreviewInfo> {

        // Define the SerialDescriptor for InvitationPreviewInfo
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("InvitationPreviewInfo") {
                element<String>("id")
                element<String>("label", isOptional = true)
                element<String>("url")
                element<String>("comment", isOptional = true)
                element<JsonElement>("jsonRepresentation", isOptional = true)
                element<InvitationType>("type")
                element<List<String>>("formats")
                element<List<String>>("documentTypes", isOptional = true)
            }

        // Deserialize the JSON into an InvitationPreviewInfo object
        override fun deserialize(decoder: Decoder): InvitationPreviewInfo {
            val jsonDecoder = decoder as? JsonDecoder
                ?: throw SerializationException("This deserializer only works with JSON format.")

            val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
            val invitation = jsonObject["invitation"]?.jsonObject
                ?: throw SerializationException("Missing 'invitation' field")

            val id = invitation["requests~attach"]?.jsonArray
                ?.firstNotNullOfOrNull {
                    it.jsonObject["@id"]
                        ?.jsonPrimitive?.content
                } ?: throw WalletError.MissingField("id")

            val url = invitation["url"]?.jsonPrimitive?.content
                ?: throw WalletError.MissingField("url")
            val label = invitation["label"]?.jsonPrimitive?.content
            val type = invitation["requests~attach"]?.jsonArray?.firstNotNullOfOrNull {
                it.jsonObject["data"]?.jsonObject?.get(
                    "json"
                )?.jsonObject?.get("@type")?.jsonPrimitive?.content
            }
                ?.let {
                    when (it) {
                        "https://didcomm.org/issue-credential/2.0/offer-credential" -> InvitationType.OFFER_CREDENTIAL
                        "https://didcomm.org/present-proof/2.0/request-presentation" -> InvitationType.REQUEST_PRESENTATION
                        else -> throw SerializationException("Unknown invitation type")
                    }
                }
                ?: throw SerializationException("Missing or invalid '@type' under 'requests~attach'")


            val formats = invitation["requests~attach"]?.jsonArray
                ?.mapNotNull {
                    it.jsonObject["data"]?.jsonObject?.get("json")?.jsonObject?.get(
                        "formats"
                    )?.jsonArray
                }
                ?.flatten()?.mapNotNull { it.jsonObject["format"]?.jsonPrimitive?.content }
                ?: emptyList()

            val base64Content = invitation["requests~attach"]?.jsonArray
                ?.mapNotNull {
                    it.jsonObject["data"]
                        ?.jsonObject?.get("json")
                        ?.jsonObject?.let { json ->
                            json["offers~attach"]?.jsonArray
                                ?: json["request_presentations~attach"]?.jsonArray
                        }
                }
                ?.flatten()
                ?.firstNotNullOfOrNull {
                    it.jsonObject["data"]
                        ?.jsonObject?.get("base64")
                        ?.jsonPrimitive?.content }

            val jsonRepresentation = if (base64Content != null) {
                try {
                    val decodedBytes = Base64.getDecoder().decode(base64Content)
                    Json.parseToJsonElement(String(decodedBytes))
                } catch (e: IllegalArgumentException) {
                    throw SerializationException("Invalid base64 content in 'base64' field.")
                }
            } else {
                null
            }

            return InvitationPreviewInfo(
                id = id,
                url = url,
                label = label,
                comment = invitation["comment"]?.jsonPrimitive?.content,
                jsonRepresentation = jsonRepresentation,
                type = type,
                formats = formats
            )
        }

        override fun serialize(encoder: Encoder, value: InvitationPreviewInfo) {
            val jsonEncoder = encoder as? JsonEncoder
                ?: throw SerializationException("This serializer only works with JSON format.")

            // Start building the JSON object
            val jsonObject = buildJsonObject {
                // Add the fields from InvitationPreviewInfo
                put("id", value.id)
                put("url", value.url)
                value.label?.let { put("label", it) }
                value.comment?.let { put("comment", it) }

                // Serialize the 'jsonRepresentation' as a JsonElement (if not null)
                value.jsonRepresentation?.let { put("jsonRepresentation", it) }

                // Serialize the 'formats' as a JSON array
                putJsonArray("formats") {
                    value.formats.forEach { format ->
                        add(format)
                    }
                }

                // Add 'documentTypes' as a JSON array
                putJsonArray("documentTypes") {
                    value.documentTypes.forEach { type ->
                        add(type)
                    }
                }

                // Add the 'invitation' object
                put("invitation", buildJsonObject {
                    put("@id", value.id)
                    put("url", value.url)
                    value.label?.let { put("label", it) }
                    value.comment?.let { put("comment", it) }

                    // Add 'formats' field (this is an example, adjust according to your actual logic)
                    putJsonArray("formats") {
                        value.formats.forEach { format ->
                            add(format)
                        }
                    }
                })
            }

            // Encode the JSON object
            jsonEncoder.encodeJsonElement(jsonObject)
        }
    }
}
