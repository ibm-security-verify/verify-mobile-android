/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class CredFilter (

    @SerialName("attr_name")
    val attrName: String,

    @SerialName("attr_values")
    val attrValues: List<String>,

    @SerialName("proof_request_referent")
    val proofRequestReferent: String? = null,

    @SerialName("exclude")
    val exclude: Boolean? = null

)