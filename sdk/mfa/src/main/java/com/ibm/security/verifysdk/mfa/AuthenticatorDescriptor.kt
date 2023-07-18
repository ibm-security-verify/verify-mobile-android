/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.RSAKeyGenParameterSpec

interface AuthenticatorDescriptor {
    val id: String
    val serviceName: String
    var accountName: String
    val allowedFactors: List<FactorType>
}

internal fun factorNameAndAlgorithm(factorType: FactorType): Pair<String, HashAlgorithmType>? {
    return when (factorType) {
        is FactorType.Face -> Pair(factorType.value.displayName, factorType.value.algorithm)
        is FactorType.Fingerprint -> Pair(factorType.value.displayName, factorType.value.algorithm)
        is FactorType.UserPresence -> Pair(factorType.value.displayName, factorType.value.algorithm)
        else -> null
    }
}

internal fun generateKeys(
    keyName: String,
    algorithm: String,
    authenticationRequired: Boolean = false,
    base64EncodingOptions: Int = Base64.NO_WRAP
): String {

    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    val keyPairGenerator =
        KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")

    val digestAlgorithm: String = when (HashAlgorithmType.fromString(algorithm)) {
        HashAlgorithmType.SHA256 -> KeyProperties.DIGEST_SHA256
        HashAlgorithmType.SHA384 -> KeyProperties.DIGEST_SHA384
        HashAlgorithmType.SHA512 -> KeyProperties.DIGEST_SHA512
        else -> {
            ""
        }
    }

    val keyGenParameterBuilder = KeyGenParameterSpec.Builder(
        keyName,
        KeyProperties.PURPOSE_SIGN
    )
        .setDigests(digestAlgorithm)
        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
        /* The issue is that you're asking Android Keystore to sign using this RSA
           private key using SHA-512 digest and PKCS #1 v1.5 signature padding
           scheme, but you didn't authorize the key to be used with the
           PKCS #1 v1.5 signature padding scheme. Unfortunately, it's not obvious
           from Signature algorithm name "SHA512withRSA" that this padding scheme
           will be used... To fix, invoke
           setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1) on the
           KeyGenParameterSpec.Builder when generating the key

           https://groups.google.com/forum/#!msg/android-developers/gDb8cJoSzqc/tJchSd0DDAAJ
         */
        .setAlgorithmParameterSpec(
            RSAKeyGenParameterSpec(
                2048,
                RSAKeyGenParameterSpec.F4
            )
        )
        .setUserAuthenticationRequired(authenticationRequired)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        keyGenParameterBuilder.setUserAuthenticationParameters(
            0, KeyProperties.AUTH_BIOMETRIC_STRONG
        )
    } else {
        @Suppress("DEPRECATION")
        keyGenParameterBuilder.setUserAuthenticationValidityDurationSeconds(-1)
    }

    keyPairGenerator.initialize(keyGenParameterBuilder.build())
    keyPairGenerator.generateKeyPair()

    return Base64.encodeToString(
        keyStore.getCertificate(keyName).publicKey.encoded,
        base64EncodingOptions
    )
}


internal fun sign(
    keyName: String,
    algorithm: String,
    dataToSign: String,
    base64EncodingOptions: Int
): String {

    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    if (keyStore.containsAlias(keyName)) {
        val privateKey = keyStore.getKey(keyName, null) as PrivateKey
        val hashAlgorithmType = HashAlgorithmType.fromString(algorithm)
        val signatureAlgorithm: String = when (hashAlgorithmType) {
            HashAlgorithmType.SHA256 -> "SHA256withRSA"
            HashAlgorithmType.SHA384 -> "SHA384withRSA"
            HashAlgorithmType.SHA512 -> "SHA512withRSA"
            else -> {
                ""
            }
        }
        val digestAlgorithm: String = when (hashAlgorithmType) {
            HashAlgorithmType.SHA256 -> "SHA-256"
            HashAlgorithmType.SHA384 -> "SHA-384"
            HashAlgorithmType.SHA512 -> "SHA-512"
            else -> {
                ""
            }
        }
        val messageDigest = MessageDigest.getInstance(digestAlgorithm)
        val value = messageDigest.digest(dataToSign.toByteArray())
        val signature = Signature.getInstance(signatureAlgorithm)
        signature.initSign(privateKey)
        signature.update(value)
        val signedBytes = signature.sign()
        return Base64.encodeToString(signedBytes, base64EncodingOptions)
    }
    throw MFAServiceError.InvalidSigningHash()
}
