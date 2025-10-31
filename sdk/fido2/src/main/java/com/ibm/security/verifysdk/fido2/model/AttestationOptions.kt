/*
 *  Copyright contributors to the IBM Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents the options for attestation during a WebAuthn operation.
 *
 * @property authenticatorSelection Specifies the authenticator selection criteria. Defaults to null.
 * @property attestation Specifies the preference for attestation conveyance. Defaults to direct attestation.
 * @property displayName The display name associated with the authenticator.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AttestationOptions(
    @EncodeDefault(EncodeDefault.Mode.NEVER) val authenticatorSelection: AuthenticatorSelection? = null,
    val attestation: AttestationConveyancePreference = AttestationConveyancePreference.DIRECT,
    val displayName: String
)
