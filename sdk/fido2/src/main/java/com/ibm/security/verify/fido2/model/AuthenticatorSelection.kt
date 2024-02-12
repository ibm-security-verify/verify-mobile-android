/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents the selection criteria for an authenticator during a WebAuthn operation.
 *
 * @property requireResidentKey Indicates whether the authenticator should support resident keys.
 * @property authenticatorAttachment The attachment modality of the authenticator.
 */
@Serializable
data class AuthenticatorSelection(
    val requireResidentKey: Boolean,
    val authenticatorAttachment: String
)