/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

internal enum class EnrollableType {
    TOTP,
    HOTP,
    FACE,
    FINGERPRINT,
    USER_PRESENCE
}