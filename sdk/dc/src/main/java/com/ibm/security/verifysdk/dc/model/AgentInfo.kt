/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the core information about an agent in the system. An `Agent` manages credentials and
 * is used to connect to other agents on behalf of the user.
 *
 * @property id The unique identifier of the agent.
 * @property name The name of the agent.
 * @property agentUrl The URL needed to connect to the agent.
 * @property connectionUrl The URL that represents the agent in a connection object.
 * @property creationTime The date and time when the agent was created.
 * @property did The decentralized identifier (DID) for the agent.
 * @property verkey The public key for the agent.
 *
 * @since 3.0.4
 */
@Serializable
data class AgentInfo (

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val agentUrl: String,

    @SerialName("connection_url")
    val connectionUrl: String,

    @SerialName("creation_time")
    val creationTime: Double,

    @SerialName("did")
    val did: DID,

    @SerialName("verkey")
    val verkey: Verkey
)

typealias Verkey = String
typealias DID = String