/*
 *  Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response structure for a network request that retrieves a list of invitations.
 *
 * This data class is used to encapsulate the response from a network request, which includes a count of the total
 * number of invitations and a list of [InvitationInfo] objects containing details about each individual invitation.
 * It is primarily used for handling the results of a network request and extracting the actual list of invitations.
 *
 * @property count The total number of invitations in the response.
 * @property items A list of [InvitationInfo] objects, each containing the details of an individual invitation.
 *
 * @since 3.0.4
 */
@ExperimentalSerializationApi
@Serializable
internal data class InvitationList(

    @SerialName("count")
    val count: Int,

    @SerialName("items")
    val items: List<InvitationInfo>
)
