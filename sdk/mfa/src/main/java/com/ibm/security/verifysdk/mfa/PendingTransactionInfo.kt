/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

@file:UseSerializers(URLSerializer::class)
package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.core.serializer.URLSerializer
import com.ibm.security.verifysdk.core.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL
import java.util.UUID

@Serializable
data class PendingTransactionInfo(
    val id: String,
    val message: String,
    val postbackUri: URL,
    @Serializable(with = UUIDSerializer::class)
    val factorID: UUID,
    val factorType: String,
    val dataToSign: String,
    val timeStamp: Instant,
    val additionalData: Map<TransactionAttribute, String>
) {
    val shortId: String
        get() {
            val index = id.indexOf("-")
            return id.substring(0, index)
        }
}