package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialDescriptor(
    val type: PublicKeyCredentialType,
    val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val transports: ArrayList<AuthenticatorTransport>? = null
)