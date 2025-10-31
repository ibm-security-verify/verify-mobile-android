/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.model.cloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionResult(
    @SerialName("total") var count: Int = 0,
    @SerialName("verifications") var verifications: List<VerificationInfo>? = null
) {
    @Serializable
    data class VerificationInfo(
        val id: String,
        val creationTime: kotlinx.datetime.Instant,
        @SerialName("transactionData") val transactionInfo: String,
        @SerialName("authenticationMethods") val methodInfo: List<MethodInfo>
    ) {
        @Serializable
        data class MethodInfo(
            val id: String,
            val methodType: String,
            val subType: String
        )
    }
}