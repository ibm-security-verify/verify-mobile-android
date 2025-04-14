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
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.Base64

/**
 * Represents an Indy-based verifiable credential.
 *
 * This class models a credential issued using the Indy framework, containing metadata such as
 * the credential definition, schema details, issuer information, and additional properties.
 *
 * @property credentialDefinitionId The identifier of the credential definition used for issuance.
 * @property schemaName The name of the schema associated with this credential.
 * @property schemaVersion The version of the schema used.
 * @property connection The connection information related to the credential issuance.
 * @property properties A map of additional properties related to the credential.
 *
 * @since 3.0.4
 */
@Serializable
data class IndyCredential(
    override val id: String,
    override val role: CredentialRole,
    override val state: CredentialState,
    override val issuerDid: DID,
    override val format: CredentialFormat,
    @SerialName("cred_json")
    override val jsonRepresentation: JsonElement?,
    @SerialName("cred_def_id")
    val credentialDefinitionId: String,
    @SerialName("schema_name")
    val schemaName: String,
    @SerialName("schema_version")
    val schemaVersion: String,
    val connection: ConnectionInfo,
    val properties: Map<String, JsonElement>,

) : CredentialDescriptor {

    override fun getAgentName(): String = connection.remote.name
    override fun getAgentUrl(): String = connection.remote.url
    override fun getFriendlyName(): String = properties["name"]?.jsonPrimitive.toString()

    val offerTime: Instant
        get() {
            val time = properties["time"]?.jsonPrimitive?.toString()
            return if (time != null) {
                try {
                    Instant.parse(time)
                } catch (e: DateTimeParseException) {
                    Instant.now()
                }
            } else {
                Instant.now()
            }
        }

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