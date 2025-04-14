/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Enum class representing the rules for selection, defining the two possible rule types:
 * "all" and "pick".
 *
 * This class provides a way to specify how certain selection operations should behave
 * based on the associated rule type, where:
 *
 * - `ALL`: Represents a rule where all elements are selected.
 * - `PICK`: Represents a rule where a specific number of elements are selected.
 *
 * The class ensures that the correct rule values are used when serializing and deserializing
 * the enum, particularly when the variable name and the value defined in the API specification
 * differ.
 *
 * @property value The string representation of the rule value that is used in the API specification.
 *
 * @since 3.0.4
 */
@Serializable
enum class Rules(val value: String) {

    @SerialName("all")
    ALL("all"),

    @SerialName("pick")
    PICK("pick");

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
        fun encode(data: Any?): String? = if (data is Rules) "$data" else null

        /**
         * Returns a valid [Rules] for [data], null otherwise.
         */
        fun decode(data: Any?): Rules? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}