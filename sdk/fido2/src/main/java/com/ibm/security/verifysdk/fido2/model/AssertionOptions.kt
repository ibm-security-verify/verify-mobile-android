/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.Serializable

/**
 * Serializable data class representing options for assertion.
 *
 * This data class encapsulates options for assertion, including the username
 * and user verification method.
 *
 * @property username The username associated with the assertion.
 * @property userVerification The user verification method used for the assertion.
 *
 * @constructor Creates an AssertionOptions instance with the provided username and user verification.
 */
@Serializable
data class AssertionOptions(
    val username: String,
    val userVerification: String
)



