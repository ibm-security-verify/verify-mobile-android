/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/// Used by a Verifier to describe the information required of a Holder before an interaction can proceed.


/**
 * Represents an input descriptor for a credential request.
 *
 * This data class holds various properties used by a Verifier to describe the information required
 * of a Holder before an interaction can proceed.
 *
 * @property id The unique identifier for the input descriptor.
 * @property schema The schema associated with the input descriptor.
 * @property name The name of the input descriptor.
 * @property purpose The purpose of the input descriptor.
 * @property group The list of groups associated with the input descriptor.
 * @property issuance The issuance information for the input descriptor.
 * @property constraints The constraints associated with the input descriptor.
 *
 * @since 3.0.7
 */
@Serializable
data class InputDescriptor(

    @SerialName("id")
    val id: String,

    @SerialName("schema")
    val schema: List<Schema>? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("group")
    val group: List<String>? = null,

    @SerialName("issuance")
    val issuance: List<JsonElement>? = null,

    @SerialName("constraints")
    val constraints: Constraints? = null
)
