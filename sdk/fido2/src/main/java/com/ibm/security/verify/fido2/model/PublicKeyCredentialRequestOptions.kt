package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

@Serializable
data class PublicKeyCredentialRequestOptions(
    val rpId: String,
    val timeout: Int,
    val challenge: String,
    val allowCredentials: ArrayList<PublicKeyCredentialDescriptor> = arrayListOf(),
    val extensions: AuthenticationExtensionsClientInputs? = null,
    val userVerification: String,
    val status: String,
    val errorMessage: String
)
