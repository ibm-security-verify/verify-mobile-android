/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a presentation definition in the context of a verifiable credential.
 *
 * A presentation definition outlines the structure of a presentation, including the descriptors
 * for the inputs, the purpose, format, and any submission requirements for a verifiable presentation.
 *
 * @property id A unique identifier for the presentation definition.
 * @property inputDescriptors A list of [InputDescriptor] objects that describe the required inputs for the presentation.
 * @property name An optional name for the presentation definition.
 * @property purpose An optional description of the purpose of the presentation definition.
 * @property format An optional [Format] describing the format of the presentation.
 * @property submissionRequirements An optional list of [SubmissionRequirement] objects that specify the requirements for submitting the presentation.
 *
 * @since 3.0.4
 */
@Serializable
data class PresentationDefinition(

    @SerialName("id")
    val id: String,

    @SerialName("input_descriptors")
    val inputDescriptors: List<InputDescriptor>,

    @SerialName("name")
    val name: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("format")
    val format: Format? = null,

    @SerialName("submission_requirements")
    val submissionRequirements: List<SubmissionRequirement>? = null
)
