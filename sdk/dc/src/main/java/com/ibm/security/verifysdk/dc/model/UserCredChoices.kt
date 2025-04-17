/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the user's choices for credential attributes and predicates.
 *
 * The `UserCredChoices` class encapsulates the user's selected credential attributes and predicates
 * for a given credential. These selections are provided as maps where the key represents the attribute
 * or predicate name, and the value represents the chosen value for that attribute or predicate.
 *
 * @property attributes A map containing the selected credential attributes. Each key represents
 * the attribute name, and the corresponding value represents the chosen value for that attribute.
 * @property predicates A map containing the selected predicates for the credential. Each key represents
 * the predicate name, and the corresponding value represents the chosen value for that predicate.
 *
 * @since 3.0.7
 */
@Serializable
data class UserCredChoices(

    @SerialName( "attributes")
    val attributes: Map<String, String>,

    @SerialName( "predicates")
    val predicates: Map<String, String>
)
