/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa

interface AuthenticatorDescriptor {
    val id : String
    val serviceName : String
    var accountName : String
    val allowedFactors : Array<FactorType>
}