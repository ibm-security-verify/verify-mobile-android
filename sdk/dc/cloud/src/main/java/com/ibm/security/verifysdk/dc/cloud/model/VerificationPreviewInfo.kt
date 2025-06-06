/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.core.extension.getStringList
import com.ibm.security.verifysdk.core.extension.getStringOrNull
import com.ibm.security.verifysdk.core.extension.getStringOrThrow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
* Represents preview information for a verification process.
*
* This class extends [CloudPreviewDescriptor] and provides additional attributes such as document types,
* name, and purpose. It includes custom serialization logic to handle multiple input formats.
*
* @property id The unique identifier of the verification preview.
* @property url The URL associated with the verification preview.
* @property label An optional label describing the preview.
* @property comment An optional comment providing additional information.
* @property jsonRepresentation The JSON representation of the preview.
* @property documentTypes A list of document types associated with the preview.
* @property name The name of the verification process.
* @property purpose The purpose of the verification.
*/
@Serializable(VerificationPreviewInfo.Companion::class)
data class VerificationPreviewInfo(
    override val id: String,
    override val url: String,
    override val label: String? = null,
    override val comment: String? = null,
    override val jsonRepresentation: JsonElement?,
    val documentTypes: List<String> = emptyList(),
    val name: String,
    val purpose: String
) : CloudPreviewDescriptor() {

    companion object : KSerializer<VerificationPreviewInfo> {

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("VerificationPreviewInfo") {
            element<String>("id")
            element<String>("url")
            element<String>("label", isOptional = true)
            element<String>("comment", isOptional = true)
            element<JsonElement>("jsonRepresentation", isOptional = true)
            element<List<String>>("documentTypes", isOptional = true)
            element<String>("name")
            element<String>("purpose")
        }

        private fun deserializeFromJsonRepresentation(jsonObject: JsonObject) = VerificationPreviewInfo(
            id = jsonObject.getStringOrThrow("id"),
            url = jsonObject.getStringOrThrow("url"),
            label = jsonObject.getStringOrNull("label"),
            comment = jsonObject.getStringOrNull("comment"),
            jsonRepresentation = jsonObject["jsonRepresentation"],
            documentTypes = jsonObject.getStringList("documentTypes"),
            name = jsonObject.getStringOrThrow("name"),
            purpose = jsonObject.getStringOrThrow("purpose")
        )

        override fun deserialize(decoder: Decoder): VerificationPreviewInfo {
            val jsonDecoder = decoder as? JsonDecoder
                ?: throw SerializationException("This deserializer only works with JSON format.")

            val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

            return when {
                jsonObject.jsonObject.containsKey("jsonRepresentation") -> {

                    val id = jsonObject["id"]?.jsonPrimitive?.content ?: throw SerializationException("Missing id")
                    val url = jsonObject["url"]?.jsonPrimitive?.content ?: throw SerializationException("Missing url")
                    val label = jsonObject["label"]?.jsonPrimitive?.contentOrNull
                    val comment = jsonObject["comment"]?.jsonPrimitive?.contentOrNull
                    val jsonRepresentation = jsonObject["jsonRepresentation"]
                    val documentTypes = jsonObject["documentTypes"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
                    val name = jsonObject["name"]?.jsonPrimitive?.content ?: throw SerializationException("Missing name")
                    val purpose = jsonObject["purpose"]?.jsonPrimitive?.content ?: throw SerializationException("Missing purpose")

                    VerificationPreviewInfo(
                        id = id,
                        url = url,
                        label = label,
                        comment = comment,
                        documentTypes = documentTypes,
                        name = name,
                        purpose = purpose,
                        jsonRepresentation = jsonRepresentation
                    )
                }
                jsonObject.jsonObject.containsKey("invitation") -> {
                    val invitation = jsonDecoder.json.decodeFromJsonElement(
                        InvitationPreviewInfo.serializer(),
                        jsonObject
                    )

                    var documentTypes = mutableListOf<String>()
                    var name = ""
                    var purpose = ""

                    invitation.jsonRepresentation?.let { jsonRepresentation ->
                        if (invitation.formats.contains("dif/presentation-exchange/definition@v1.0")) {
                            val presentationDefinition =
                                jsonRepresentation.jsonObject["presentation_definition"]?.jsonObject
                            val inputDescriptors =
                                presentationDefinition?.get("input_descriptors")?.jsonArray
                            inputDescriptors?.firstOrNull()?.jsonObject?.let {
                                name = it["name"]?.jsonPrimitive?.content ?: "Name not found"
                                purpose = it["purpose"]?.jsonPrimitive?.content ?: "Purpose not found"
                                documentTypes.add(it["id"]?.jsonPrimitive?.content ?: "Id not found")
                            }
                        } else if (invitation.formats.contains("hlindy/proof-req@v2.0")) {
                            jsonRepresentation.jsonObject["name"]?.jsonPrimitive?.content?.let {
                                name = it
                            }

                            jsonRepresentation.jsonObject["cred_def_id"]?.jsonPrimitive?.content?.let {
                                documentTypes.add(it)
                            } ?: run {

                                val requestedAttributes =
                                    jsonRepresentation.jsonObject["requested_attributes"]?.jsonObject
                                val purposes = mutableListOf<String>()

                                requestedAttributes?.forEach { (_, value) ->
                                    val referent = value.jsonObject
                                    val restrictions = referent["restrictions"]?.jsonArray

                                    referent["name"]?.jsonPrimitive?.content?.let { purposes.add(it) }

                                    restrictions?.forEach { restriction ->
                                        restriction.jsonObject.forEach { (_, itemValue) ->
                                            itemValue.jsonPrimitive.contentOrNull?.let {
                                                documentTypes.add(
                                                    it
                                                )
                                            }
                                        }
                                    }
                                }

                                purpose = purposes.joinToString(" ")
                            }
                        } else {
                            documentTypes = emptyList<String>().toMutableList()
                        }
                    }

                    VerificationPreviewInfo(
                        id = invitation.id,
                        url = invitation.url,
                        label = invitation.label,
                        comment = invitation.comment,
                        documentTypes = documentTypes,
                        name = name,
                        purpose = purpose,
                        jsonRepresentation = invitation.jsonRepresentation
                    )
                }
                else -> throw SerializationException("Unknown structure")
            }
        }

        override fun serialize(encoder: Encoder, value: VerificationPreviewInfo) = encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.url)
            value.label?.let { encodeStringElement(descriptor, 2, it) }
            value.comment?.let { encodeStringElement(descriptor, 3, it) }
            value.jsonRepresentation?.let { encodeSerializableElement(descriptor, 4, JsonElement.serializer(), it) }
            encodeSerializableElement(descriptor, 5, ListSerializer(String.serializer()), value.documentTypes)
            encodeStringElement(descriptor, 6, value.name)
            encodeStringElement(descriptor, 7, value.purpose)
        }
    }
}