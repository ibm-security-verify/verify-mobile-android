/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.authentication.TokenInfo
import kotlinx.serialization.Serializable
import java.net.URL


@Serializable
class OnPremiseAuthenticator (
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
    override var publicKeyCertificate: String?,
    @Serializable(with = URLSerializer::class)
    val qrLoginUri: URL?,
    private var ignoreSSLCertificate_: Boolean = false,
    private var clientId_: String
) : MFAAuthenticatorDescriptor {

    val ignoreSSLCertificate: Boolean get() = ignoreSSLCertificate_
    val clientId: String get() = clientId_
}