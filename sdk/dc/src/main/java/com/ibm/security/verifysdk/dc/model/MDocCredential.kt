/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64

/**
 * Represents a Credential in the MDoc format (mobile document format) with associated details and
 * metadata.
 *
 * This class provides information about the credential, including its ID, role, state, issuer, format,
 * connection details, and additional properties such as name and icon.
 *
 * @property id The unique identifier of the credential.
 * @property role The role of the credential (e.g., holder, issuer).
 * @property state The current state of the credential.
 * @property issuerDid The Decentralized Identifier (DID) of the credential issuer.
 * @property format The format of the credential.
 * @property jsonRepresentation The JSON representation of the credential, if available.
 * @property connection Information about the connection used for issuing the credential.
 * @property properties A map of additional properties associated with the credential, such as name and icon.
 *
 * @since 3.0.4
 */
@Serializable
data class MDocCredential(
    override val id: String,
    override val role: CredentialRole,
    override val state: CredentialState,
    override val issuerDid: DID,
    override val format: CredentialFormat,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    val connection: ConnectionInfo,
    val properties: Map<String, JsonElement>
) : CredentialDescriptor {

    override fun getAgentName(): String = connection.remote.name
    override fun getAgentUrl(): String = connection.remote.url
    override fun getFriendlyName(): String = properties["name"]?.jsonPrimitive.toString()

    val icon: Bitmap?
        get() {
            val icon = properties["icon"]
            val value = icon?.jsonPrimitive?.toString()
            if (!value.isNullOrEmpty()) {
                val components = value.split("base64,")
                if (components.size == 2) {
                    val data = Base64.getDecoder().decode(components[1])
                    return BitmapFactory.decodeByteArray(data, 0, data.size)
                }
            }
            return null
        }
}