/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class VerificationState(val value: String) {

    @SerialName("outbound_verification_request")
    OUTBOUND_VERIFICATION_REQUEST("outbound_verification_request"),

    @SerialName("inbound_verification_request")
    INBOUND_VERIFICATION_REQUEST("inbound_verification_request"),

    @SerialName("outbound_proof_request")
    OUTBOUND_PROOF_REQUEST("outbound_proof_request"),

    @SerialName("inbound_proof_request")
    INBOUND_PROOF_REQUEST("inbound_proof_request"),

    @SerialName("proof_generated")
    PROOF_GENERATED("proof_generated"),

    @SerialName("proof_shared")
    PROOF_SHARED("proof_shared"),

    @SerialName("passed")
    PASSED("passed"),

    @SerialName("failed")
    FAILED("failed"),

    @SerialName("deleted")
    DELETED("deleted");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is VerificationState) "$data" else null

        /**
         * Returns a valid [VerificationState] for [data], null otherwise.
         */
        fun decode(data: Any?): VerificationState? = data?.let {
          val normalizedData = "$it".lowercase()
          entries.firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

