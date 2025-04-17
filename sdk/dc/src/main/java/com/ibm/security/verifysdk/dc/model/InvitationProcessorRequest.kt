/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.Serializable

/**
 * Represents the payload used in the [InvitationsApi] network request.
 *
 * This class is used to send information required to process an invitation in the network request.
 * It includes the invitation URL and a flag indicating whether to inspect the invitation.
 *
 * @property url The URL of the invitation to be processed.
 * @property inspect A flag indicating whether the invitation should be inspected before processing.
 *
 * @since 3.0.7
 */
@Serializable
data class InvitationProcessorRequest(

    val url: String,
    val inspect: Boolean
)
