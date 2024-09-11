/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Field(

    @SerialName("path")
    val path: List<String>,

    @SerialName("id")
    val id: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("filter")
    val filter: Filter? = null,

    @SerialName("predicate")
    val predicate: Optionality? = null
)
