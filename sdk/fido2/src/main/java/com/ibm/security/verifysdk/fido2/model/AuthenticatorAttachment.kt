/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.Serializable

/**
 * An enumeration representing authenticator attachment options according to the FIDO2 specification.
 * Authenticator attachment specifies the method by which the authenticator communicates with the client.
 *
 * This enumeration provides two options:
 * - [PLATFORM]: Represents authenticators embedded in the platform, such as built-in biometric sensors.
 * - [CROSS_PLATFORM]: Represents authenticators connected to the client platform through external means, such as USB or NFC.
 *
 * @property value The string value representing the authenticator attachment modality.
 *
 */
@Serializable
enum class AuthenticatorAttachment(val value: String) {
    /**
     * Represents authenticators embedded in the platform.
     */
    PLATFORM("platform"),

    /**
     * Represents authenticators connected to the client platform through external means.
     */
    CROSS_PLATFORM("cross-platform")
}
