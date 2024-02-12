/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * An enum class representing the attachment modality of an authenticator.
 *
 * @property value The string value representing the authenticator attachment modality.
 */
@Serializable
enum class AuthenticatorAttachment(val value: String) {
    PLATFORM("platform"),
    CROSS_PLATFORM("cross-platform")
}
