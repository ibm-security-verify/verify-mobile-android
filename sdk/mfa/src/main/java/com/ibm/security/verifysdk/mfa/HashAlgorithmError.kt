/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

sealed class HashAlgorithmError : Error() {

    data object InvalidHash : HashAlgorithmError()

    val errorDescription: String
        get() = localizedDescription

    private val localizedDescription: String
        get() = when (this) {
            is HashAlgorithmError.InvalidHash -> "The hash type is invalid."
        }
}
