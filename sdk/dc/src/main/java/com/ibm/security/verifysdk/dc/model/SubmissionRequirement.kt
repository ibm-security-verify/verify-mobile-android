/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the submission requirements for a presentation definition.
 *
 * The `SubmissionRequirement` class defines the criteria that must be met for a specific submission
 * in a presentation definition. This includes the rule to be followed, optional details like name and purpose,
 * and constraints such as count, min, and max values.
 *
 * @property rule The [Rules] that define the submission rule for the presentation. This is a mandatory field.
 * @property name An optional name for the submission requirement. This field may be null.
 * @property purpose An optional purpose for the submission requirement. This field may be null.
 * @property count An optional field that specifies the number of times the requirement must be fulfilled.
 *                 This field is nullable.
 * @property min An optional minimum value for the submission requirement. This field is nullable.
 * @property max An optional maximum value for the submission requirement. This field is nullable.
 * @property from An optional field specifying the source of the data for the requirement. This field may be null.
 * @property fromNested An optional list of strings specifying nested sources for the data required for the submission.
 *                       This field may be null.
 *
 * @since 3.0.7
 */
@Serializable
data class SubmissionRequirement(

    @SerialName("rule")
    val rule: Rules,

    @SerialName("name")
    val name: String? = null,

    @SerialName("purpose")
    val purpose: String? = null,

    @SerialName("count")
    val count: Double? = null,

    @SerialName("min")
    val min: Double? = null,

    @SerialName("max")
    val max: Double? = null,

    @SerialName("from")
    val from: String? = null,

    @SerialName("from_nested")
    val fromNested: List<String>? = null
)
