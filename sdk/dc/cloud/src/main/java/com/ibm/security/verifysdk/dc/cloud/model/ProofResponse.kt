/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A data class representing the response to a proof request, containing credential definitions and self-attested attributes.
 *
 * This class encapsulates the credential definitions and self-attested attributes that are provided as part of
 * a proof response. The credential definitions specify the credentials used to fulfill the proof request,
 * and the self-attested attributes are attributes that are directly provided by the requester.
 *
 * @property credDefs A map where the key is the identifier of a credential definition, and the value is a list of
 *                    `ProofResponseCred` objects representing the credentials associated with that definition.
 * @property selfAttestedAttributes A map containing self-attested attributes, where the key is the attribute name,
 *                                  and the value is the attribute's value, provided directly by the requester.
 *
 * @since 3.0.7
 */
@Serializable
data class ProofResponse (

    @SerialName("cred_defs")
    val credDefs: Map<String, List<ProofResponseCred>>,

    @SerialName("self_attested_attributes")
    val selfAttestedAttributes: Map<String, String>
)
