/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  The agent's relationship to the credential.
 *
 *  @property value The string representation of the role, as defined in the API specification.
 *  @since 3.0.7
 */
@Serializable
enum class CredentialRole(val value: String) {

    @SerialName("issuer")
    ISSUER("issuer"),

    @SerialName("holder")
    HOLDER("holder");

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
        fun encode(data: Any?): String? = if (data is CredentialRole) "$data" else null

        /**
         * Returns a valid [CredentialRole] for [data], null otherwise.
         */
        fun decode(data: Any?): CredentialRole? = data?.let {
          val normalizedData = "$it".lowercase()
          entries.firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }

        fun fromSerialName(name: String): CredentialRole {
            return entries.firstOrNull { it.value == name }
                ?: throw IllegalArgumentException("Unknown role: $name")
        }
    }
}

