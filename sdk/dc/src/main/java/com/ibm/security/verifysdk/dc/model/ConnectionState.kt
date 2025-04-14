/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName

/**
 * Represents the state of a connection.
 *
 * This enum ensures that the correct state values are used when communicating with the API.
 *
 * @property value The string representation of the state, as defined in the API specification.
 *
 * @since 3.0.4
 */
enum class ConnectionState(val value: String) {

    @SerialName("inbound_invitation")
    INBOUND_INVITATION("inbound_invitation"),

    @SerialName("outbound_offer")
    OUTBOUND_OFFER("outbound_offer"),

    @SerialName("inbound_offer")
    INBOUND_OFFER("inbound_offer"),

    @SerialName("did_exchange_response_sent")
    DID_EXCHANGE_RESPONSE_SENT("did_exchange_response_sent"),

    @SerialName("connected")
    CONNECTED("connected"),

    @SerialName("rejected")
    REJECTED("rejected");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     */
    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? =
            if (data is ConnectionState) "$data" else null

        /**
         * Returns a valid [ConnectionState] for [data], null otherwise.
         */
        fun decode(data: Any?): ConnectionState? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}

