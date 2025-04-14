/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
* Represents detailed information about an agent involved in a [ConnectionInfo].
*
* This data class encapsulates key attributes related to the connection agent, such as its name,
* URL, and [DID].
*
* @property name The name of the agent
* @property url The URL needed to connect to the agent.
* @property ext A boolean indicating whether the agent is an extended agent (i.e., has additional features).
* @property properties A map of additional properties associated with the connection agent, stored as key-value pairs.
* @property did The decentralized identifier (DID) of the connection agent, if available.
* @property didDoc The DID document associated with the agent's DID, if available, represented as a JSON element.
 *
 * @since 3.0.4
*/
@Serializable
data class ConnectionAgentInfo(

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String,

    @SerialName("ext")
    val ext: Boolean,

    @SerialName("properties")
    val properties: Map<String, JsonElement>? = null,

    @SerialName("did")
    val did: String? = null,

    @SerialName("did_doc")
    val didDoc: JsonElement? = null
)