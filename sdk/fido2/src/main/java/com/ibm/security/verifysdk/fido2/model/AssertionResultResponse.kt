/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
@file:Suppress("PropertyName")

package com.ibm.security.verifysdk.fido2.model

import com.ibm.security.verifysdk.core.toJsonElement
import com.ibm.security.verifysdk.core.toJsonObject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonPrimitive

/**
 * Represents the response received after an assertion operation.
 *
 * @property errorMessage An optional error message indicating any errors that occurred during the assertion.
 * @property status An optional status message indicating the result of the assertion operation.
 * @property additionalData An optional map containing additional data associated with the assertion result.
 *                           The keys are strings and the values are JsonElement instances representing arbitrary JSON data.
 */
@Serializable(with = AssertionResultResponseSerializer::class)
data class AssertionResultResponse(
    val errorMessage: String? = null,
    val status: String? = null,
    val additionalData: Map<String, JsonElement>? = null
)

internal object AssertionResultResponseSerializer : KSerializer<AssertionResultResponse> {

    private val stringToJsonElementSerializer =
        MapSerializer(String.serializer(), JsonElement.serializer())
    override val descriptor: SerialDescriptor = stringToJsonElementSerializer.descriptor

    private val knownKeys = setOf("errorMessage", "status")

    override fun deserialize(decoder: Decoder): AssertionResultResponse {
        require(decoder is JsonDecoder)
        val filtersMap = decoder.decodeSerializableValue(stringToJsonElementSerializer)
        val errorMessage = filtersMap["errorMessage"]?.jsonPrimitive?.content
        val status = filtersMap["status"]?.jsonPrimitive?.content
        val additionalData = filtersMap.filter { (key, _) -> !knownKeys.contains(key) }

        return AssertionResultResponse(errorMessage, status, additionalData)
    }

    override fun serialize(encoder: Encoder, value: AssertionResultResponse) {
        require(encoder is JsonEncoder)
        val map: MutableMap<String, JsonElement> = mutableMapOf()

        value.errorMessage?.let { map["errorMessage"] = value.errorMessage.toJsonElement() }
        value.status?.let { map["status"] = value.status.toJsonElement() }
        value.additionalData?.let { map.putAll(value.additionalData.toJsonObject()) }

        encoder.encodeSerializableValue(stringToJsonElementSerializer, map)
    }
}

/**
 * Represents a response received after an attestation operation, which is equivalent to an
 * assertion result response.
 *
 * This typealias is used to create an alias for AssertionResultResponse.
 */
typealias AttestationResultResponse = AssertionResultResponse