/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verifysdk.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Represents the options for creating a public key credential during a WebAuthn registration process.
 *
 * @property rp The public key credential relying party entity.
 * @property user The public key credential user entity.
 * @property challenge The challenge used during the registration process.
 * @property pubKeyCredParams The list of public key credential parameters supported by the authenticator. Defaults to an empty list.
 * @property timeout The timeout value in milliseconds for the registration operation.
 * @property excludeCredentials The list of credentials that should not be used for registration. Defaults to an empty list.
 * @property authenticatorSelection The criteria for selecting an authenticator for the registration process.
 * @property attestation The preference for attestation conveyance during the registration process. Defaults to NONE.
 * @property extension Additional client-specific extensions for the registration process. Defaults to null.
 */
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

/**
 * Represents client-specific authentication extensions inputs.
 * This typealias is used to define a map where the key is a string and the value is a JSON object.
 */
typealias AuthenticationExtensionsClientInputs = Map<String, JsonObject>