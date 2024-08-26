/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class InvitationRole(val value: String) {

    @SerialName("inviter")
    INVITER("inviter"),

    @SerialName("invitee")
    INVITEE("invitee");

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
        fun encode(data: Any?): String? = if (data is InvitationRole) "$data" else null

        /**
         * Returns a valid [InvitationRole] for [data], null otherwise.
         */
        fun decode(data: Any?): InvitationRole? = data?.let {
          val normalizedData = "$it".lowercase()
          entries.firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

