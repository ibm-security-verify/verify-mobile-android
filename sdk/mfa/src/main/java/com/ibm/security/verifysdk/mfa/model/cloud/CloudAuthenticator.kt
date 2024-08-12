/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.model.cloud

import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.core.serializer.URLSerializer
import com.ibm.security.verifysdk.mfa.FactorType
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import kotlinx.serialization.Serializable
import java.net.URL

/**
 * `CloudAuthenticator` enables authenticators to be able to perform transaction and token refresh
 * operations.
 *
 * @property refreshUri The URI used to refresh the authenticator, serialized with [URLSerializer].
 * @property transactionUri The URI used for transaction operations, serialized with [URLSerializer].
 * @property theme A map representing the theme settings for the authenticator.
 * @property token Information about the token associated with the authenticator.
 * @property id The unique identifier of the authenticator.
 * @property serviceName The name of the service using the authenticator.
 * @property accountName The name of the account associated with the authenticator.
 * @property allowedFactors A list of allowed factor types for multi-factor authentication.
 * @property customAttributes A key value pair for configuring custom attributes of the authenticator.
 *
 * @since 3.0.2
 */
@Serializable
data class CloudAuthenticator(
    @Serializable(with = URLSerializer::class)
    override val refreshUri: URL,
    @Serializable(with = URLSerializer::class)
    override val transactionUri: URL,
    override var theme: Map<String, String>,
    override var token: TokenInfo,
    override val id: String,
    override val serviceName: String,
    override var accountName: String,
    override val allowedFactors: List<FactorType>,
    val customAttributes: Map<String, String>
) : MFAAuthenticatorDescriptor
