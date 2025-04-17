/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the status of a process directive in a specific context.
 *
 * The class contains an optional [directive] property, which may hold information about a specific
 * directive related to the status of the process.
 *
 * @property directive An optional [Directives] value representing the directive associated with the status.
 *                     It can be null if no directive is specified.
 *
 * @since 3.0.7
 */
@Serializable
data class PdStatus(
    @SerialName("directive")
    val directive: Directives? = null
)
