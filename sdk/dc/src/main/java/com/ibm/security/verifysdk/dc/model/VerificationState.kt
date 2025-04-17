/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the various states of a verification process.
 *
 * A verification process involves multiple stages, such as sending and receiving verification requests,
 * generating and sharing proofs, and determining the outcome of verification.
 *
 * @property value The string representation of the verification state.
 *
 * @since 3.0.7
 */
@Serializable
enum class VerificationState(val value: String) {


    /** A verification request has been sent to an external entity. */
    @SerialName("outbound_verification_request")
    OUTBOUND_VERIFICATION_REQUEST("outbound_verification_request"),

    /** A verification request has been received from an external entity. */
    @SerialName("inbound_verification_request")
    INBOUND_VERIFICATION_REQUEST("inbound_verification_request"),

    /** A proof request has been sent to an external entity. */
    @SerialName("outbound_proof_request")
    OUTBOUND_PROOF_REQUEST("outbound_proof_request"),

    /** A proof request has been received from an external entity. */
    @SerialName("inbound_proof_request")
    INBOUND_PROOF_REQUEST("inbound_proof_request"),

    /** A proof has been generated in response to a proof request. */
    @SerialName("proof_generated")
    PROOF_GENERATED("proof_generated"),

    /** A proof has been successfully shared with a verifier. */
    @SerialName("proof_shared")
    PROOF_SHARED("proof_shared"),

    /** The verification process has completed successfully. */
    @SerialName("passed")
    PASSED("passed"),

    /** The verification process has failed. */
    @SerialName("failed")
    FAILED("failed"),

    /** The verification record has been deleted. */
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
        fun decode(data: Any?): VerificationState? {
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

