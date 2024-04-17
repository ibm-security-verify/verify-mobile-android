/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents the client data for assertion during a WebAuthn operation.
 *
 * @property type The type of the client data.
 * @property challenge The challenge associated with the client data.
 * @property origin The origin associated with the client data.
 */
@Serializable
data class ClientDataJsonAssertion(
    val type: String,
    val challenge: String,
    val origin: String,
)
