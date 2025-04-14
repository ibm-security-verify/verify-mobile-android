/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents a field within a structured data model.
 *
 * This class defines a field with an associated path, optional identifier, purpose, and predicate constraint.
 * It is primarily used in contexts where filtering or structuring data is required.
 *
 * @since 3.0.4
 */
@Serializable
data class Field(

    @SerialName("path")
    val path: List<String>,

    @SerialName("id")
    val id: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("predicate")
    val predicate: Optionality? = null
)
