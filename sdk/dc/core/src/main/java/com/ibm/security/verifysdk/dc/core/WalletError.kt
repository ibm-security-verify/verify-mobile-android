/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.core

/**
 * Represents various errors that can occur within the wallet system.
 *
 * This sealed class extends [Error] and defines different types of wallet-related errors
 * that may occur during parsing, formatting, or initialization processes.
 *
 * @since 3.0.7
 */
sealed class WalletError : Error() {

    /**
     * Indicates a failure to parse JSON into the expected type.
     */
    class FailedToParse : WalletError()

    /**
     * Indicates that the JSON format is invalid for creating an object.
     */
    class InvalidFormat : WalletError()

    /**
     * Represents a failure in data initialization, such as an invalid signing hash algorithm.
     */
    class DataInitializationFailed : WalletError()

    /**
     * Indicates that a required field is missing in the data.
     *
     * @param fieldName The name of the missing field.
     */
    data class MissingField(val fieldName: String) : WalletError()

    /**
     * Represents a general error with a custom message.
     *
     * @param message The error message describing the issue.
     */
    data class General(override val message: String) : WalletError()

    val errorDescription: String
        get() = localizedDescription

    /**
     * Returns a localized description of the specific error type.
     */
    private val localizedDescription: String
        get() = when (this) {
            is FailedToParse -> "JSON value fails to parse as the specified type."
            is InvalidFormat -> "Invalid JSON format to create."
            is DataInitializationFailed -> "The signing hash algorithm was invalid."
            is MissingField -> "The required field '$fieldName' is missing."
            is General -> message
        }
}