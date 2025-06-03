package com.ibm.security.verifysdk.dc.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement

/**
 * Represents a credential interface.
 *
 * This sealed interface defines the common properties and behaviors of credential descriptors,
 * which are used to represent credentials in various formats, roles, and states.
 *
 * Implementing classes should provide their own implementations for retrieving a friendly name,
 * agent name, and agent URL.
 *
 * @property id The unique identifier of the credential.
 * @property format The format of the credential, represented by [CredentialFormat].
 * @property role The role associated with the credential, represented by [CredentialRole].
 * @property state The current state of the credential, represented by [CredentialState].
 * @property issuerDid The decentralized identifier (DID) of the issuer.
 * @property jsonRepresentation The JSON representation of the credential, if available.
 *
 * @since 3.0.7
 */
interface CredentialDescriptor{
    val id: String
    val format: CredentialFormat
    val role: CredentialRole
    val state: CredentialState
    val issuerDid: DID
    @SerialName("cred_json")
    val jsonRepresentation: JsonElement?

    /**
     * Retrieves the type of the credential descriptor.
     *
     * @return The simple class name of the implementing descriptor, or "Unknown" if unavailable.
     */
    fun getTyp(): String {
        return this::class.simpleName ?: "Unknown"
    }
}

typealias Verkey = String
typealias DID = String
