/*
 *  Copyright contributors to the IBM Security Verify DC SDK for Android project
 */
package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@ExperimentalSerializationApi
@Serializable
data class OutOfBandInvitationAttachArgs (

    @SerialName("recipient")
    val recipient: InvitationRole,

    @SerialName("use_connection")
    val useConnection: Boolean,

    @SerialName("verification_request")
    val verificationRequest: CreateVerificationRequestArgs? = null,

    @SerialName("verification_proposal")
    val verificationProposal: CreateVerificationProposalArgs? = null,

    @SerialName("cred_offer")
    val credOffer: OutOfBandCredentialOfferArgs? = null,

    @SerialName("cred_proposal")
    val credProposal: OutOfBandCredentialProposalArgs? = null,

    @SerialName("interaction")
    val interaction: CreateInteractionRequest? = null
)