package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatorSelectionCriteria(
    val authenticatorAttachment: AuthenticatorAttachment = AuthenticatorAttachment.PLATFORM,
    val requireResidentKey: Boolean = false,
    val userVerification: UserVerificationRequirement = UserVerificationRequirement.PREFERRED
)
