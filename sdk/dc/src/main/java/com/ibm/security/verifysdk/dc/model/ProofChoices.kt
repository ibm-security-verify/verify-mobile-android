/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofChoices(

    @SerialName("attributes")
    val attributes: Map<String, Map<String, AttrCredChoice>>,

    @SerialName("predicates")
    val predicates: Map<String, Map<String, PredCredChoicesValue>>
)
