/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents the options for requesting a public key credential during a WebAuthn assertion operation.
 *
 * @property rpId The relying party identifier associated with the request.
 * @property timeout The timeout value in milliseconds for the assertion operation.
 * @property challenge The challenge used during the assertion process.
 * @property allowCredentials The list of allowed credentials for the assertion operation. Defaults to an empty list.
 * @property extensions Additional client-specific extensions for the assertion operation. Defaults to null.
 * @property userVerification The preference for user verification during the assertion operation.
 * @property status The status of the assertion operation.
 * @property errorMessage An error message indicating any errors that occurred during the assertion operation.
 */
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
