/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A data class representing a credential within a proof response, containing attributes and predicates associated with the credential.
 *
 * This class encapsulates the attributes and predicates of a credential that is part of a proof response. The attributes
 * represent the data elements that are included in the credential, while the predicates represent conditions or constraints
 * related to the credential's attributes that need to be verified.
 *
 * @property attributes A map where the key is the attribute name and the value is the attribute's value, representing
 *                      the data elements of the credential.
 * @property predicates A list of predicates associated with the credential, where each predicate represents a condition
 *                      or constraint that is associated with the credential's attributes that must be satisfied.
 *
 * @since 3.0.4
 */
@Serializable
data class ProofResponseCred(

    @SerialName("attributes")
    val attributes: Map<String, String>,

    @SerialName("predicates")
    val predicates: List<String>
)
