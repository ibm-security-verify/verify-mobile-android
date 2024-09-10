/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class VerificationRole(val value: String) {

    @SerialName("verifier")
    VERIFIER("verifier"),

    @SerialName("prover")
    PROVER("prover");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is VerificationRole) "$data" else null

        /**
         * Returns a valid [VerificationRole] for [data], null otherwise.
         */
        fun decode(data: Any?): VerificationRole? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
