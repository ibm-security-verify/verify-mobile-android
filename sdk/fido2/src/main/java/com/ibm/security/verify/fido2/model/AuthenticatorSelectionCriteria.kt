/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents the criteria for selecting an authenticator during a WebAuthn operation.
 *
 * @property authenticatorAttachment The attachment modality preference for the authenticator. Default is `PLATFORM`.
 * @property requireResidentKey Indicates whether the authenticator should support resident keys. Default is `false`.
 * @property userVerification The preference for user verification during the operation. Default is `PREFERRED`.
 */
@Serializable
data class AuthenticatorSelectionCriteria(
    val authenticatorAttachment: AuthenticatorAttachment = AuthenticatorAttachment.PLATFORM,
    val requireResidentKey: Boolean = false,
    val userVerification: UserVerificationRequirement = UserVerificationRequirement.PREFERRED
)
