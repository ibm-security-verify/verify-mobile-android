/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.onprem

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.mfa.FactorType
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import com.ibm.security.verifysdk.mfa.URLSerializer
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
    @Serializable(with = URLSerializer::class)
    val qrLoginUri: URL?,
    val ignoreSSLCertificate: Boolean = false,
    val clientId: String
) : MFAAuthenticatorDescriptor {

//    val ignoreSSLCertificate: Boolean get() = _ignoreSSLCertificate
//    val clientId: String get() = _clientId
}