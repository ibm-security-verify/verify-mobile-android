/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ibm.security.verifysdk.core.extension.toJsonElement
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.DriversLicenseCredentialCardView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.DriversLicenseCredentialOfferView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.EmployeeCredentialCardView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.EmployeeCredentialOfferView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.MedicareCredentialCardView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.MedicareCredentialOfferView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.ResidentCredentialCardView
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.ResidentCredentialOfferView
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = ViewDescriptorSerializer::class)
abstract class ViewDescriptor {
    abstract var jsonRepresentation: JsonElement

    val json: Json
        get() = Json {
            ignoreUnknownKeys = true
        }

    @Composable
    abstract fun ShowCredential(modifier: Modifier)

}

object ViewDescriptorSerializer : KSerializer<ViewDescriptor> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ViewDescriptor") {
        element<String>("nameSpaces")
    }

    override fun deserialize(decoder: Decoder): ViewDescriptor {
        require(decoder is JsonDecoder)

        val jsonElement = decoder.decodeJsonElement().jsonObject

        return jsonElement["attributes"]?.jsonArray
            ?.takeIf { attributes ->
                attributes.firstOrNull()?.jsonObject
                    ?.get("ns")?.jsonPrimitive?.content == "au.gov.servicesaustralia.medicare.card"
            }
            ?.let { attributesArray ->
                MedicareCredentialCardView(jsonRepresentation = attributesArray)
            }

            ?: jsonElement["nameSpaces"]?.jsonObject
                ?.get("au.gov.servicesaustralia.medicare.card")
                ?.let { medicareJsonElement ->
                    MedicareCredentialOfferView(jsonRepresentation = medicareJsonElement)
                }

            ?: jsonElement["attributes"]?.jsonArray
                ?.takeIf { attributes ->
                    attributes.firstOrNull()?.jsonObject
                        ?.get("ns")?.jsonPrimitive?.content == "org.iso.18013.5.1"
                }
                ?.let { attributesArray ->
                    DriversLicenseCredentialCardView(jsonRepresentation = attributesArray)
                }

            ?: jsonElement["docType"]?.jsonPrimitive?.content
                ?.takeIf { it == "org.iso.18013.5.1.mDL" }
                ?.let {
                    jsonElement["nameSpaces"]?.jsonObject
                        ?.get("org.iso.18013.5.1")?.jsonObject
                        ?.let { driversLicenseJsonElement ->
                            DriversLicenseCredentialOfferView(jsonRepresentation = driversLicenseJsonElement)
                        }
                }

            ?: jsonElement["credential"]?.jsonObject
                ?.get("type")?.jsonArray
                ?.any { it.jsonPrimitive.content == "PermanentResidentCard" }
                ?.takeIf { it }
                ?.let {
                    ResidentCredentialOfferView(jsonRepresentation = jsonElement["credential"]?.jsonObject.toJsonElement())
                }

            ?: jsonElement["type"]?.jsonArray
                ?.any { it.jsonPrimitive.content == "PermanentResidentCard" }
                ?.takeIf { it }
                ?.let {
                    ResidentCredentialCardView(jsonRepresentation = jsonElement)
                }

            ?: jsonElement.takeIf { it.jsonObject.containsKey("attributes") }
                ?.get("schema_id")?.jsonPrimitive?.content
                ?.takeIf { it.endsWith("employee_role:4.2", ignoreCase = true) }
                ?.let {
                    EmployeeCredentialOfferView(jsonRepresentation = jsonElement)
                }

            ?: jsonElement.takeIf { it.jsonObject.containsKey("values") }
                ?.get("schema_id")?.jsonPrimitive?.content
                ?.takeIf { it.endsWith("employee_role:4.2", ignoreCase = true) }
                ?.let {
                    EmployeeCredentialCardView(jsonRepresentation = jsonElement)
                }

            ?: throw IllegalArgumentException("Unknown credential in : $jsonElement")
    }

    override fun serialize(encoder: Encoder, value: ViewDescriptor) {
        require(encoder is JsonEncoder)
        val jsonObject = buildJsonObject {
            put("jsonRepresentation", value.jsonRepresentation.toJsonElement())
        }
        encoder.encodeJsonElement(jsonObject)
    }
}