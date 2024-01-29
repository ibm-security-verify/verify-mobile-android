package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatorSelection(
    val requireResidentKey: Boolean,
    val authenticatorAttachment: String
)