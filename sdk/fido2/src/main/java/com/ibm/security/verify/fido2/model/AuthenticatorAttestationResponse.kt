package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AuthenticatorAttestationResponse(
    val id: String,
    val rawId: String,
    val response: ResponseAttestation,
    val type: String,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) val nickname: String = "FIDO2App - Android"
)

@Serializable
data class ResponseAttestation(
    val clientDataJSON: String,
    val attestationObject: String
)