/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofViewPredicate(
    
    @SerialName( "cred_def_id")
    val credDefId: String,

    @SerialName( "predicate")
    val predicate: String
)
