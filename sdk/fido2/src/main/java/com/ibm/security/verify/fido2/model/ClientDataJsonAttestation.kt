package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientDataJsonAttestation(
    val type: String,
    val challenge: String,
    val origin: String,
    val crossOrigin: Boolean
)
