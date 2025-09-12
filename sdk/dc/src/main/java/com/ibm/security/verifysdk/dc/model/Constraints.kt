/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
* Represents the constraints for a query.
*
* This data class holds various optional constraints that can be applied to a query, such as
* limiting disclosure, filtering by statuses, selecting specific fields, and specifying
* subject-related constraints.
*
* @property limitDisclosure The optionality of limiting disclosure.
* @property statuses The statuses to filter the results by.
* @property fields The list of fields to include in the response.
* @property subjectIsIssuer The optionality of the subject being the issuer.
* @property isHolder The list of holder subjects to filter the results by.
* @property sameSubject The list of holder subjects to filter the results by, ensuring the same subject.
*/
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
