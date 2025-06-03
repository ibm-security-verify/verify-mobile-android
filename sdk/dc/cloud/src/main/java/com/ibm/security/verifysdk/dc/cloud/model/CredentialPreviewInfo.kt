/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Represents a preview of a credential, including metadata such as its ID, URL, label, comment,
 * and JSON representation. This class is used as a descriptor for credential previews.
 *
 * @property id The unique identifier of the credential preview.
 * @property url The URL associated with the credential preview.
 * @property label An optional label for the credential preview.
 * @property comment An optional comment related to the credential preview.
 * @property jsonRepresentation The JSON representation of the credential preview.
 * @property documentTypes A list of document types associated with the credential preview.
 *
 * @since 3.0.7
 */
@Serializable(CredentialPreviewInfo.Companion::class)
data class CredentialPreviewInfo(
    override val id: String,
    override val url: String,
    override val label: String? = null,
    override val comment: String? = null,
    override val jsonRepresentation: JsonElement?,
    val documentTypes: List<String> = emptyList()
) : CloudPreviewDescriptor() {

    companion object : KSerializer<CredentialPreviewInfo> {

        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("CredentialPreviewInfo") {
                element("id", String.serializer().descriptor)
                element("url", String.serializer().descriptor)
                element("label", String.serializer().descriptor, isOptional = true)
                element("comment", String.serializer().descriptor, isOptional = true)
                element(
                    "jsonRepresentation",
                    JsonElement.serializer().descriptor,
                    isOptional = true
                )
                element(
                    "documentTypes",
                    ListSerializer(String.serializer()).descriptor,
                    isOptional = true
                )
            }

        override fun deserialize(decoder: Decoder): CredentialPreviewInfo {
            val jsonDecoder = decoder as? JsonDecoder
                ?: throw SerializationException("This deserializer only works with JSON format.")

            val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
            val invitationDecoder = jsonDecoder.json
            val invitation = invitationDecoder.decodeFromJsonElement(
                InvitationPreviewInfo.serializer(),
                jsonObject
            )

            return CredentialPreviewInfo(
                id = invitation.id,
                url = invitation.url,
                label = invitation.label,
                comment = invitation.comment,
                jsonRepresentation = invitation.jsonRepresentation,
                documentTypes = invitation.formats

            )
        }

        override fun serialize(encoder: Encoder, value: CredentialPreviewInfo) {
            val composite = encoder.beginStructure(descriptor)
            composite.encodeStringElement(descriptor, 0, value.id)
            composite.encodeStringElement(descriptor, 1, value.url)
            value.label?.let { composite.encodeStringElement(descriptor, 2, it) }
            value.comment?.let { composite.encodeStringElement(descriptor, 3, it) }
            value.jsonRepresentation?.let { composite.encodeSerializableElement(descriptor, 4, JsonElement.serializer(), it) }
            composite.encodeSerializableElement(descriptor, 5, ListSerializer(String.serializer()), value.documentTypes)
            composite.endStructure(descriptor)
        }
    }
}

