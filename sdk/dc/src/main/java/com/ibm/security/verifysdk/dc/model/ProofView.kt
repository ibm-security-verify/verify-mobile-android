/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofView(

    @SerialName("attributes")
    val attributes: List<ProofViewAttribute>,

    @SerialName("predicates")
    val predicates: List<ProofViewPredicate>
)
