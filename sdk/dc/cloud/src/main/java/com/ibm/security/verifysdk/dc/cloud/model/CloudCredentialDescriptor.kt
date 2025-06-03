/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import com.ibm.security.verifysdk.dc.cloud.serializer.CloudCredentialSerializer
import com.ibm.security.verifysdk.dc.core.CredentialDescriptor
import com.ibm.security.verifysdk.dc.core.CredentialFormat
import com.ibm.security.verifysdk.dc.core.CredentialRole
import com.ibm.security.verifysdk.dc.core.CredentialState
import com.ibm.security.verifysdk.dc.core.DID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable(with = CloudCredentialSerializer::class)
abstract class CloudCredentialDescriptor : CredentialDescriptor {

    abstract override val id: String

    @SerialName("format")
    abstract override val format: CredentialFormat
    abstract override val role: CredentialRole
    abstract override val state: CredentialState
    abstract override val issuerDid: DID
    abstract override val jsonRepresentation: JsonElement?
    abstract val connection: ConnectionInfo

    /**
     * Retrieves a user-friendly name for the credential.
     *
     * Implementing classes should override this function to provide a meaningful name
     * for display purposes.
     *
     * @return A string representing the friendly name of the credential.
     */
    abstract fun getFriendlyName(): String

    /**
     * Retrieves the agent name associated with the credential.
     *
     * Implementing classes should override this function to specify the name of the
     * agent handling the credential.
     *
     * @return A string representing the agent's name.
     */
    abstract fun getAgentName(): String

    /**
     * Retrieves the agent URL associated with the credential.
     *
     * Implementing classes should override this function to specify the URL of the
     * agent managing the credential.
     *
     * @return A string representing the agent's URL.
     */
    abstract fun getAgentUrl(): String
}