/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.dc.serializer.CredentialSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
@Serializable(with = CredentialSerializer::class)
sealed interface CredentialDescriptor{
    val id: String
    val format: CredentialFormat
    val role: CredentialRole
    val state: CredentialState
    val issuerDid: DID
    @SerialName("cred_json")
    val jsonRepresentation: JsonElement?
    val connection: ConnectionInfo

    /**
     * Retrieves the type of the credential descriptor.
     *
     * @return The simple class name of the implementing descriptor, or "Unknown" if unavailable.
     */
    fun getTyp(): String {
        return this::class.simpleName ?: "Unknown"
    }

    /**
     * Retrieves a user-friendly name for the credential.
     *
     * Implementing classes should override this function to provide a meaningful name
     * for display purposes.
     *
     * @return A string representing the friendly name of the credential.
     */
    fun getFriendlyName(): String

    /**
     * Retrieves the agent name associated with the credential.
     *
     * Implementing classes should override this function to specify the name of the
     * agent handling the credential.
     *
     * @return A string representing the agent's name.
     */
    fun getAgentName(): String

    /**
     * Retrieves the agent URL associated with the credential.
     *
     * Implementing classes should override this function to specify the URL of the
     * agent managing the credential.
     *
     * @return A string representing the agent's URL.
     */
    fun getAgentUrl(): String
}

typealias Verkey = String
typealias DID = String