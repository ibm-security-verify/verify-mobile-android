/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a JSON Web Token (JWT) object containing algorithm details.
 *
 * This class is used to deserialize JWT-related data, specifically the list of supported
 * cryptographic algorithms used for signing or verifying the token.
 *
 * @property alg A list of cryptographic algorithms supported by this JWT.
 *
 * @since 3.0.4
 */
@Serializable
data class MdocObject(
    @SerialName("alg")
    val alg: List<String>
)
