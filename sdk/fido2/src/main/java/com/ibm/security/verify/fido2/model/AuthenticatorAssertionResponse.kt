package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
data class AuthenticatorAssertionResponse(
    val id: String,
    val rawId: String,
    val response: ResponseAssertion,
    val type: String,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) val nickname: String = "FIDO2App - Android"
)


@Serializable
data class ResponseAssertion(
    val clientDataJSON: String,
    val authenticatorData: String,
    val signature: String
)