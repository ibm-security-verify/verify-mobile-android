/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialCreationOptions(
    val rp: PublicKeyCredentialRpEntity,
    val user: PublicKeyCredentialUserEntity,
    var challenge: String,
    val pubKeyCredParams: ArrayList<PublicKeyCredentialParameters> = arrayListOf(),
    val timeout: Int,
    val excludeCredentials: ArrayList<PublicKeyCredentialDescriptor> = arrayListOf(),
    val authenticatorSelection: AuthenticatorSelectionCriteria,
    val attestation: AttestationConveyancePreference = AttestationConveyancePreference.NONE,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val extension: AuthenticationExtensionsClientInputs? = null
)

typealias AuthenticationExtensionsClientInputs = Map<String, JsonObject>