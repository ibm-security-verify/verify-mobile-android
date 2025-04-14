/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a request for a verifiable presentation.
 *
 * The [PresentationRequest] class encapsulates the details of a presentation request, which includes
 * the [presentationDefinition] that defines the structure and requirements of the presentation,
 * and optional [options] for additional parameters like challenge and domain.
 *
 * @property presentationDefinition The definition of the presentation, outlining the input descriptors,
 * purpose, and any associated requirements for the presentation.
 * @property options Optional additional parameters for the presentation request, such as a challenge
 * or domain, which can be used to enhance the security and context of the request.
 *
 * @since 3.0.4
 */
@ExperimentalSerializationApi
@Serializable
data class PresentationRequest (

    @SerialName("presentation_definition")
    val presentationDefinition: PresentationDefinition,

    @SerialName("options")
    val options: PresentationOptions? = null
)
