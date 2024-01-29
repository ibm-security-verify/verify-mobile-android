package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class AssertionOptions(
    val username: String,
    val userVerification: String
)
