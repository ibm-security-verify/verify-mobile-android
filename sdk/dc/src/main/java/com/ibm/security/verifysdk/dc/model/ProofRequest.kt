/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofRequest(

    @SerialName("nonce")
    val nonce: String,

    @SerialName("name")
    val name: String? = null,

    @SerialName("version")
    val version: String? = null,

    @SerialName("requested_attributes")
    val requestedAttributes: String? = null,

    @SerialName("requested_predicates")
    val requestedPredicates: String? = null,

    @SerialName("allow_proof_request_override")
    val allowProofRequestOverride: Boolean? = null,

    @SerialName("cred_filter")
    val credFilter: List<CredFilter>? = null,

    @SerialName("properties")
    val properties: Map<String, String>? = null,

    @SerialName("jsonld")
    val jsonld: DifPresentationRequest? = null,

    @SerialName("bbs")
    val bbs: DifPresentationRequest? = null
)