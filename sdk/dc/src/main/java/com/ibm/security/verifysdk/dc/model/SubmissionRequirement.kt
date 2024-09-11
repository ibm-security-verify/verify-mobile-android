/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionRequirement(

    @SerialName("rule")
    val rule: Rules,

    @SerialName("name")
    val name: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("count")
    val count: Double? = null,

    @SerialName("min")
    val min: Double? = null,

    @SerialName("max")
    val max: Double? = null,

    @SerialName("from")
    val from: String? = null,

    @SerialName("from_nested")
    val fromNested: List<String>? = null
)
