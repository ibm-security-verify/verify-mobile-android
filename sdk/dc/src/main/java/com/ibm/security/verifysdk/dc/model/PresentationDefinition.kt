/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
