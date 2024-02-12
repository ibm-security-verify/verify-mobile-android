/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents the client data for attestation during a WebAuthn operation.
 *
 * @property type The type of the client data.
 * @property challenge The challenge associated with the client data.
 * @property origin The origin associated with the client data.
 * @property crossOrigin Indicates whether the client data is from a cross-origin operation.
 */
@Serializable
data class ClientDataJsonAttestation(
    val type: String,
    val challenge: String,
    val origin: String,
    val crossOrigin: Boolean
)
