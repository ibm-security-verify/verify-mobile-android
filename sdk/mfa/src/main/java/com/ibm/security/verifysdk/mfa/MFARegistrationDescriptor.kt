/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

interface MFARegistrationDescriptor<out Authenticator : MFAAuthenticatorDescriptor> {

    var pushToken: String
    var accountName: String
    var authenticationRequired: Boolean
    var invalidatedByBiometricEnrollment: Boolean

    val countOfAvailableEnrollments: Int

    @Throws
    fun nextEnrollment(): EnrollableSignature?

    @Throws
    suspend fun enroll()

    @Throws
    suspend fun enroll(name: String, publicKey: String, signedData: String)

    @Throws
    suspend fun finalize(): Result<MFAAuthenticatorDescriptor>
}
