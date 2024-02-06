/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AttestationOptions(
    @EncodeDefault(EncodeDefault.Mode.NEVER) val authenticatorSelection: AuthenticatorSelection? = null,
    val attestation: AttestationConveyancePreference = AttestationConveyancePreference.DIRECT,
    val displayName: String
)
