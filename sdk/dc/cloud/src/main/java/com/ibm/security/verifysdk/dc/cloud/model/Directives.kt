/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents directive rules that define access or permission levels.
 *
 * This enum class specifies directives that can be applied to certain operations,
 * indicating whether they are required, allowed, or disallowed.
 *
 * @property value The string representation of the directive, used in API communication.
 * @since 3.0.7
 */
@Serializable
enum class Directives(val value: String) {

    @SerialName("required")
    REQUIRED("required"),

    @SerialName("allowed")
    ALLOWED("allowed"),

    @SerialName("disallowed")
    DISALLOWED("disallowed");

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
        fun encode(data: Any?): String? = if (data is Directives) "$data" else null

        /**
         * Returns a valid [Directives] for [data], null otherwise.
         */
        fun decode(data: Any?): Directives? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}