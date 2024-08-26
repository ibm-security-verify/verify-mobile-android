/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName

enum class CredentialState(val value: String) {

    @SerialName("outbound_request")
    OUTBOUND_REQUEST("outbound_request"),

    @SerialName("inbound_request")
    INBOUND_REQUEST("inbound_request"),

    @SerialName("outbound_offer")
    OUTBOUND_OFFER("outbound_offer"),

    @SerialName("inbound_offer")
    INBOUND_OFFER("inbound_offer"),

    @SerialName("accepted")
    ACCEPTED("accepted"),

    @SerialName("rejected")
    REJECTED("rejected"),

    @SerialName("issued")
    ISSUED("issued"),

    @SerialName("stored")
    STORED("stored"),

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
        fun encode(data: Any?): String? = if (data is CredentialState) "$data" else null

        /**
         * Returns a valid [CredentialState] for [data], null otherwise.
         */
        fun decode(data: Any?): CredentialState? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}