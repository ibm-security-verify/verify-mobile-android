/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents the response received after an assertion operation by an authenticator.
 *
 * @property id The identifier associated with the response.
 * @property rawId The raw identifier associated with the response.
 * @property response The assertion response containing client data JSON, authenticator data, and signature.
 * @property type The type of the response.
 * @property nickname The nickname associated with the authenticator. Defaults to "FIDO2App - Android".
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AuthenticatorAssertionResponse(
    val id: String,
    val rawId: String,
    val response: ResponseAssertion,
    val type: String,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) val nickname: String = "FIDO2App - Android"
)

/**
 * Represents the response data included in an assertion response.
 *
 * @property clientDataJSON The client data JSON included in the assertion response.
 * @property authenticatorData The authenticator data included in the assertion response.
 * @property signature The signature included in the assertion response.
 */
@Serializable
data class ResponseAssertion(
    val clientDataJSON: String,
    val authenticatorData: String,
    val signature: String
)