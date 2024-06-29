/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa

import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricPrompt
import com.ibm.security.verifysdk.core.KeystoreHelper


interface AuthenticatorDescriptor {
    val id: String
    val serviceName: String
    var accountName: String
    val allowedFactors: List<FactorType>
}

internal fun generateKeys(
    keyName: String,
    algorithm: String,
    authenticationRequired: Boolean = false,
    base64EncodingOptions: Int = Base64.NO_WRAP
): String {

    return Base64.encodeToString(
        KeystoreHelper.createKeyPair(
            keyName,
            algorithm,
            KeyProperties.PURPOSE_SIGN,
            authenticationRequired
        ).encoded,
        base64EncodingOptions
    )
}

internal fun sign(
    keyName: String,
    algorithm: String,
    dataToSign: String,
    base64EncodingOptions: Int = Base64.DEFAULT
): String {

    val signatureAlgorithm: String = when (HashAlgorithmType.fromString(algorithm)) {
        HashAlgorithmType.SHA1 -> "SHA1withRSA"
        HashAlgorithmType.SHA256 -> "SHA256withRSA"
        HashAlgorithmType.SHA384 -> "SHA384withRSA"
        HashAlgorithmType.SHA512 -> "SHA512withRSA"
    }

    return KeystoreHelper.signData(keyName, signatureAlgorithm, dataToSign, base64EncodingOptions) ?: ""
}

internal fun sign(
    cryptoObject: BiometricPrompt.CryptoObject,
    dataToSign: String,
    base64EncodingOptions: Int = Base64.DEFAULT
): String {

    return KeystoreHelper.signData(cryptoObject, dataToSign, base64EncodingOptions) ?: ""
}
