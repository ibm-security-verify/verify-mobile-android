/*
 *  Copyright contributors to the IBM Security Verify FIDO2 SDK for Android project
 */
package com.ibm.security.verify.fido2.model

import kotlinx.serialization.Serializable

/**
 * Represents parameters for a public key credential.
 *
 * @property type The type of the public key credential.
 * @property alg The COSE (CBOR Object Signing and Encryption) algorithm identifier associated with the public key credential.
 */
@Serializable
data class PublicKeyCredentialParameters(
    val type: PublicKeyCredentialType,
    val alg: COSEAlgorithmIdentifier?,
)

/**
 * Represents the COSE (CBOR Object Signing and Encryption) algorithm identifier.
 * This typealias is used to represent a COSE algorithm identifier, which is a long value.
 */
typealias COSEAlgorithmIdentifier = Long