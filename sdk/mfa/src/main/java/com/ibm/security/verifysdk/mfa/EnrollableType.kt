/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

enum class EnrollableType {
    TOTP,
    HOTP,
    FACE,
    FINGERPRINT,
    USER_PRESENCE;

    companion object {
        fun fromString(type: String): EnrollableType? {
            return try {
                valueOf(type.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}