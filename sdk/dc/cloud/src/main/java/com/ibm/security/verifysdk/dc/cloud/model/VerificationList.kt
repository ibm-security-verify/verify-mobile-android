/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response structure for a network request that retrieves a list of verifications.
 *
 * This data class is used to encapsulate the response from a network request, which includes a count of the total
 * number of verifications and a list of [VerificationInfo] objects containing details about each individual verification.
 * It is primarily used for handling the results of a network request and extracting the actual list of verifications.
 *
 * @property count The total number of verifications in the response.
 * @property items A list of [VerificationInfo] objects, each containing the details of an individual verification.
 *
 * @since 3.0.7
 */
@ExperimentalSerializationApi
@Serializable
data class VerificationList(

    @SerialName("count")
    val count: Int,

    @SerialName("items")
    val items: List<VerificationInfo>
)