/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */

package com.ibm.security.verify.fido2

/**
 * Represents an exception specific to biometric authentication.
 *
 * This class is used to indicate errors related to biometric authentication
 * processes. It extends the standard Exception class and provides a constructor
 * that accepts a message describing the error.
 *
 * @param message A string describing the exception.
 *
 * @constructor Creates a BiometricAuthenticationException instance with the given message.
 */
internal class BiometricAuthenticationException(message: String) : Exception(message)