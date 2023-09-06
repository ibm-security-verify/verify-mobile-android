/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

sealed class MFARegistrationError : Error() {

    object FailedToParse : MFARegistrationError()
    object InvalidFormat : MFARegistrationError()
    object NoEnrollableFactors : MFARegistrationError()
    object EnrollmentFailed : MFARegistrationError()
    object DataInitializationFailed : MFARegistrationError()
    object MissingAuthenticatorIdentifier : MFARegistrationError()
    object InvalidRegistrationData : MFARegistrationError()
    data class UnderlyingError(val error: Error) : MFARegistrationError()

    val errorDescription: String?
        get() = localizedDescription

    private val localizedDescription: String
        get() = when (this) {
            is FailedToParse -> "Failed to parse JSON value."
            is InvalidFormat -> "Invalid JSON format to create an MFARegistrationDescriptor."
            is NoEnrollableFactors -> "No enrollable factors available for enrollment."
            is EnrollmentFailed -> "Factor enrollment failed."
            is DataInitializationFailed -> "Initialization failed."
            is MissingAuthenticatorIdentifier -> "Authenticator identifier missing from OAuth token."
            is InvalidRegistrationData -> "Invalid multi-factor registration data."
            is UnderlyingError -> "A general registration error occurred: ${error.localizedMessage}"
        }
}

