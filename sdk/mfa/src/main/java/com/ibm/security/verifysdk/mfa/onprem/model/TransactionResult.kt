/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem.model

import com.ibm.security.verifysdk.mfa.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.util.Date

@Serializable
internal data class TransactionResult(
    @SerialName("transactionsPending")
    var transactions: List<TransactionInfo> = emptyList(),
    @SerialName("attributesPending")
    var attributes: List<AttributeInfo> = emptyList()
) {

    @Serializable
    data class AttributeInfo(
        val dataType: String,
        val values: List<String>,
        val uri: String,
        val transactionId: String
    )

    @Serializable
    data class TransactionInfo(
        @Serializable(with = DateSerializer::class)
        val creationTime: Date,
        val requestUrl: String,
        val transactionId: String,
        @SerialName("authnPolicyURI")
        val authnPolicyUri: String
    )

    object TransactionResultSerializer : JsonTransformingSerializer<TransactionResult>(serializer()) {
        override fun transformDeserialize(element: JsonElement): JsonElement {
            val transactionsElement = element.jsonObject["urn:ietf:params:scim:schemas:extension:isam:1.0:MMFA:Transaction"]
            val attributesPending = transactionsElement?.jsonObject?.get("attributesPending")
            val transactionsPending = transactionsElement?.jsonObject?.get("transactionsPending")

            return buildJsonObject {
                if (attributesPending != null) put("attributesPending", attributesPending)
                if (transactionsPending != null) put("transactionsPending", transactionsPending)
            }
        }
    }
}

