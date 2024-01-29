package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialRpEntity(
    var id: String?,
    val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val icon: String? = null
) {
    init {
        this.id ?: name
    }
}
