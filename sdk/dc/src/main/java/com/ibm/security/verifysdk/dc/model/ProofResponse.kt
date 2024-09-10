/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofResponse (

    @SerialName("cred_defs")
    val credDefs: Map<String, List<ProofResponseCred>>,

    @SerialName("self_attested_attributes")
    val selfAttestedAttributes: Map<String, String>
)
