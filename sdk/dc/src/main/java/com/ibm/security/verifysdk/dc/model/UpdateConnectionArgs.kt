/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the arguments required to update a connection's state and properties.
 *
 * The `UpdateConnectionArgs` class encapsulates the necessary information for updating a connection's state
 * and optional properties. This includes the current state of the connection and a map of properties that
 * may be updated.
 *
 * @property state The [ConnectionState] representing the current state of the connection. This is a mandatory field.
 * @property properties An optional map of key-value pairs representing the properties of the connection.
 *                      These properties can be updated. This field may be null.
 *
 * @since 3.0.7
 */
@Serializable
data class UpdateConnectionArgs (

    @SerialName("state")
    val state: ConnectionState,

    @SerialName("properties")
    val properties: Map<String, String>? = null
)
