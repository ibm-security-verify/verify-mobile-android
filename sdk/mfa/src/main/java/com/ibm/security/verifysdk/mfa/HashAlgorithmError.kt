/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

/**
 * Represents errors related to hash algorithm operations.
 *
 * @since 3.0.2
 */
sealed class HashAlgorithmError : Error() {

    /**
     * Indicates that the hash type is invalid.
     */
    data object InvalidHash : HashAlgorithmError()

    /**
     * Provides a description of the error.
     */
    val errorDescription: String
        get() = localizedDescription

    private val localizedDescription: String
        get() = when (this) {
            is HashAlgorithmError.InvalidHash -> "The hash type is invalid."
        }
}
