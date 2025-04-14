/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a filter used for selecting credentials based on specific attributes.
 *
 * This data class defines criteria for filtering credentials in proof requests.
 * It allows specifying attribute names, expected values, and optional exclusion rules.
 *
 * @property attrName The name of the attribute to filter by.
 * @property attrValues A list of acceptable values for the specified attribute.
 * @property proofRequestReferent The optional referent used to link the filter to a proof request.
 * @property exclude If `true`, excludes credentials matching the specified criteria instead of including them.
 *
 * @since 3.0.4
 */
@ExperimentalSerializationApi
@Serializable
data class CredFilter(

    @SerialName("attr_name")
    val attrName: String,

    @SerialName("attr_values")
    val attrValues: List<String>,

    @SerialName("proof_request_referent")
    val proofRequestReferent: String? = null,

    @SerialName("exclude")
    val exclude: Boolean? = null
)