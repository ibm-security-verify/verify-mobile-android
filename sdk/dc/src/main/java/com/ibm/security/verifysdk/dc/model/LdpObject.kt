/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Linked Data Proof (LDP) object containing proof type details.
 *
 * This class is used to deserialize Linked Data Proof (LDP) information, specifically
 * the list of cryptographic proof types associated with a credential or document.
 *
 * @property proofType A list of cryptographic proof types used for verifying the integrity
 * and authenticity of the data.
 *
 * @since 3.0.7
 */
@Serializable
data class LdpObject(

    @SerialName("proof_type")
    val proofType: List<String>
)
