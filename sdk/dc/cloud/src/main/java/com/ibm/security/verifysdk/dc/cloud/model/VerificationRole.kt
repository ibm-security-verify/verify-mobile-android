/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the role in a verification process.
 *
 * A verification process involves two roles:
 * - **VERIFIER**: The entity requesting proof.
 * - **PROVER**: The entity providing proof.
 *
 * This enum provides utility methods for encoding and decoding role values.
 *
 * @property value The string representation of the role.
 *
 * @since 3.0.7
 */
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
        fun decode(data: Any?): VerificationRole? {
            val normalizedData = data?.toString()?.lowercase() ?: return null
            return entries.firstOrNull {
                it.name.equals(
                    normalizedData,
                    ignoreCase = true
                ) || it.value.equals(normalizedData, ignoreCase = true)
            }
        }
    }
}
