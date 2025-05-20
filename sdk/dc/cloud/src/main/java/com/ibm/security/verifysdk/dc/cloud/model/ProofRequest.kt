/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A data class representing a proof request in the context of a decentralized identity verification process.
 *
 * This class contains various properties to define the structure of a proof request, including attributes
 * for nonces, names, versions, requested attributes, predicates, and other related fields. It also supports
 * optional fields such as proof overrides and credential filters.
 *
 * @property nonce An optional nonce used to ensure the freshness of the proof request.
 * @property name An optional name for the proof request.
 * @property version The version of the proof request.
 * @property requestedAttributes A JSON string representing the attributes that are being requested in the proof request.
 * @property requestedPredicates A JSON string representing the predicates that are being requested in the proof request.
 * @property allowProofRequestOverride A flag indicating whether proof request overrides are allowed.
 * @property credFilter A list of credential filters used to filter the credentials requested by the proof request.
 * @property properties A map containing additional properties related to the proof request.
 * @property mdoc An optional `PresentationRequest` related to the `mso_mdoc` field of the proof request.
 * @property jsonld An optional `PresentationRequest` related to the `jsonld` field of the proof request.
 * @property bbs An optional `PresentationRequest` related to the `bbs` field of the proof request.
 *
 * @since 3.0.7
 */
@Serializable
data class ProofRequest(

    @SerialName("nonce")
    val nonce: String? = null,

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

    @SerialName("mso_mdoc")
    val mdoc: PresentationRequest? = null,

    @SerialName("jsonld")
    val jsonld: PresentationRequest? = null,

    @SerialName("bbs")
    val bbs: PresentationRequest? = null
)