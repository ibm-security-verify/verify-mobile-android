/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the role of a participant in a connection.
 *
 * This enum ensures that the correct role values are used when communicating with the API.
 *
 * @property value The string representation of the role, as defined in the API specification.
 *
 * @since 3.0.7
 */
@Serializable
enum class ConnectionRole(val value: String) {

    @SerialName("inviter")
    INVITER("inviter"),

    @SerialName("invitee")
    INVITEE("invitee");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     */
    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is ConnectionRole) "$data" else null

        /**
         * Returns a valid [ConnectionRole] for [data], null otherwise.
         */
        fun decode(data: Any?): ConnectionRole? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}