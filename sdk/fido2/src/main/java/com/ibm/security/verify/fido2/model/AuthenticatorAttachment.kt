package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
enum class AuthenticatorAttachment(val value: String) {
    PLATFORM("platform"),
    CROSS_PLATFORM("cross-platform")
}
