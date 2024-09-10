/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofResponseCred(

    @SerialName("attributes")
    val attributes: Map<String, String>,

    @SerialName("predicates")
    val predicates: List<String>
)
