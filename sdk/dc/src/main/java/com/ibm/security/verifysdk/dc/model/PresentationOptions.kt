/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the options for a verifiable presentation request.
 *
 * The [PresentationOptions] class contains optional parameters that can be included
 * when making a presentation request. These options help to ensure the integrity and
 * authenticity of the presentation request by providing challenge and domain values.
 *
 * @property challenge An optional challenge string that serves as a unique, nonce-like value
 * used to verify the authenticity of the presentation request.
 * @property domain An optional domain string representing the context or domain
 * in which the presentation is being requested.
 *
 * @since 3.0.4
 */
@Serializable
data class PresentationOptions(

    @SerialName("challenge")
    val challenge: String? = null,

    @SerialName("domain")
    val domain: String? = null
)
