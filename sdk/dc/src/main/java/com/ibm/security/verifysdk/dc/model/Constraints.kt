/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Constraints(

    @SerialName("limit_disclosure")
    val limitDisclosure: Optionality? = null,

    @SerialName("statuses")
    val statuses: Statuses? = null,

    @SerialName("fields")
    val fields: List<Field>? = null,

    @SerialName("subject_is_issuer")
    val subjectIsIssuer: Optionality? = null,

    @SerialName("is_holder")
    val isHolder: List<HolderSubject>? = null,

    @SerialName("same_subject")
    val sameSubject: List<HolderSubject>? = null
)
