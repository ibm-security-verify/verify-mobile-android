/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents a descriptor for a public key credential.
 *
 * @property type The type of the public key credential.
 * @property id The identifier associated with the public key credential.
 * @property transports The list of authenticator transports supported by the public key credential. Default is null.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PublicKeyCredentialDescriptor(
    val type: PublicKeyCredentialType,
    val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER) val transports: ArrayList<AuthenticatorTransport>? = null
)