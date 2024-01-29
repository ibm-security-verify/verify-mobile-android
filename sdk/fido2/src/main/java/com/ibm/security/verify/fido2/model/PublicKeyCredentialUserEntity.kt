package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Suppress("unused")
@Serializable
data class PublicKeyCredentialUserEntity(
    val id: String,
    val displayName: String,
    val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val icon: String? = null
)
