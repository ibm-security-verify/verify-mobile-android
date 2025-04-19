/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Represents an invitation used for establishing a secure connection.
 *
 * This class models an invitation that contains details such as an identifier, recipient key,
 * and additional metadata, allowing participants to initiate a connection securely.
 *
 * @property id The unique identifier of the invitation.
 * @property url The full URL of the invitation.
 * @property shortUrl The optional shortened version of the invitation URL.
 * @property recipientKey The public key of the recipient for establishing a secure connection.
 * @property timestamps A map containing timestamp-related metadata associated with the invitation.
 * @property properties Additional properties related to the invitation.
 *
 * @since 3.0.7
 */
@ExperimentalSerializationApi
@Serializable
data class InvitationInfo(

    @SerialName("id")
    val id: String,

    @SerialName("url")
    val url: String,

    @SerialName("short_url")
    val shortUrl: String? = null,

    @SerialName("recipient_key")
    val recipientKey: String,

    @SerialName("timestamps")
    val timestamps: Map<String, TimeStampsValue>? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,
)

/**
 * Represents a timestamp value stored as a JSON element.
 */
typealias TimeStampsValue = JsonElement

