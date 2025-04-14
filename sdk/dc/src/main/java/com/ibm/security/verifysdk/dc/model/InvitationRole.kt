/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.dc.model.InvitationRole.INVITEE
import com.ibm.security.verifysdk.dc.model.InvitationRole.INVITER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Enum class representing the possible roles in an invitation process.
 *
 * This enum defines two roles: [INVITER] and [INVITEE]. These roles are used to represent
 * the sender (inviter) and the receiver (invitee) of an invitation.
 *
 * @property value The string value associated with the role, used for serialization and communication.
 *
 * @since 3.0.4
 */
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

