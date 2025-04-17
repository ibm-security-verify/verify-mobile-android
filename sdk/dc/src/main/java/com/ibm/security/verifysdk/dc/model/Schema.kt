/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a schema that defines a structure for data validation.
 *
 * The `Schema` class contains the URI of the schema and a flag indicating whether
 * it is required. It can be used in contexts where a schema's existence or its
 * validation requirements need to be specified.
 *
 * @property uri The URI of the schema, typically a unique identifier that points
 *                to the location of the schema definition.
 * @property required A nullable Boolean indicating whether the schema is required.
 *                    If `null`, it implies that the requirement is unspecified.
 * @since 3.0.7
 */
@Serializable
data class Schema(

    @SerialName("uri")
    val uri: String,

    @SerialName("required")
    val required: Boolean? = null
)
