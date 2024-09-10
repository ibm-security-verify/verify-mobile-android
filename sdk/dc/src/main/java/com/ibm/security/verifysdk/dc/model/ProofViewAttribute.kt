/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class ProofViewAttribute(
    
    @SerialName("cred_def_id")
    val credDefId: String,

    @SerialName("name")
    val name: String,

    @SerialName("value")
    val `value`: JsonPrimitive
)
