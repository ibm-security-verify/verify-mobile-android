/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.dc.cloud.model.Optionality.PREFERRED
import com.ibm.security.verifysdk.dc.cloud.model.Optionality.REQUIRED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the optionality of a credential or field, indicating whether it is required or preferred.
 *
 * This enum defines two possible values:
 * - [REQUIRED]: Indicates that the field is required.
 * - [PREFERRED]: Indicates that the field is preferred but not mandatory.
 *
 * The [value] of each enum is used to align with the values defined in the API specification to ensure
 * that the client sends the correct values to the server.
 *
 * @property value The string value representing the optionality type (either "required" or "preferred").
 *
 * @since 3.0.7
 */
@Serializable
enum class Optionality(val value: String) {

    @SerialName("required")
    REQUIRED("required"),

    @SerialName("preferred")
    PREFERRED("preferred");

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
        fun encode(data: kotlin.Any?): String? = if (data is Optionality) "$data" else null

        /**
         * Returns a valid [Optionality] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): Optionality? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}