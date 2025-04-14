/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a subject in a credential verification process with specific field constraints.
 *
 * This class defines a subject by referencing field identifiers and applying a directive that specifies
 * how the subject's information should be handled in the verification process.
 *
 * @property fieldId A list of field identifiers that relate to the subject.
 * @property directive The directive specifying the optionality or requirement of the subject's presence.
 *
 * @since 3.0.4
 */
@Serializable
data class HolderSubject(

    @SerialName("field_id")
    val fieldId: List<String>,

    @SerialName("directive")
    val directive: Optionality
)
