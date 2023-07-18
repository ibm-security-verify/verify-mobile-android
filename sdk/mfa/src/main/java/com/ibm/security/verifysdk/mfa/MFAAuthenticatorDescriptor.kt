/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import java.net.URL

import com.ibm.security.verifysdk.authentication.TokenInfo

interface MFAAuthenticatorDescriptor : AuthenticatorDescriptor {

    val refreshUri : URL
    val transactionUri : URL
    var theme : Map<String, String>
    var token : TokenInfo
    var publicKeyCertificate : String?
}