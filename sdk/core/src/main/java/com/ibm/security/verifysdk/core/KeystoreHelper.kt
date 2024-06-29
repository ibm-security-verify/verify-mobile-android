/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.Nullable
import androidx.biometric.BiometricPrompt
import org.slf4j.LoggerFactory
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.SecretKey

/**
 * Helper class to perform key management and signing operations.
 *
 * https://developer.android.com/training/articles/keystore
 *
 */
@Suppress("BooleanMethodIsAlwaysInverted")
object KeystoreHelper {

    private val log = LoggerFactory.getLogger(javaClass)

    var keystoreType = "AndroidKeyStore"
        set(value) {
            val keyStore = KeyStore.getInstance(value)
            keyStore.load(null)
            field = value
        }

    var keySize: Int = 2048

    // https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
    private val supportedAlgorithmsSigning: ArrayList<String> =
        arrayListOf("SHA1withRSA", "SHA256withRSA", "SHA512withRSA", "EC")

    /**
     * Generates a private and public key to sign data. An existing key pair with the same alias will
     * be deleted.
     * <p>
     * The key size is specific to provided algorithm and could be looked up from the algorithm-specific
     * parameters via <a href="https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder.html#setAlgorithmParameterSpec(java.security.spec.AlgorithmParameterSpec)">setAlgorithmParameterSpec</a>.
     *
     * @param keyName  the unique identifier of the key pair
     * @param algorithm  the standard string name of the algorithm to generate the key pair
     * @param purpose
     * @param authenticationRequired  indicates whether the generated key requires authentication
     *                               (fingerprint) in order to get access to it.
     * @param invalidatedByBiometricEnrollment  indicates whether the key should be invalidated on biometric enrollment.
     *                                          This is only supported since API level 24 (Nougat). For further details please
     *                                          see <a href="https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment(boolean)">setInvalidatedByBiometricEnrollment</a>
     *
     * @throws KeyStoreException  if the key could not be generated
     * @throws UnsupportedOperationException  if the `algorithm` is not supported
     */
    @Throws(KeyStoreException::class, UnsupportedOperationException::class)
    fun createKeyPair(
        keyName: String,
        algorithm: String,
        purpose: Int,
        authenticationRequired: Boolean = false,
        invalidatedByBiometricEnrollment: Boolean = false
    ): PublicKey {

        lateinit var digest: String

        log.entering()

        try {
            if ((algorithm in supportedAlgorithmsSigning).not()) {
                throw UnsupportedOperationException(
                    String.format(
                        "Algorithm %s is not supported",
                        algorithm
                    )
                )
            }

            val keyStore = KeyStore.getInstance(keystoreType)
            keyStore.load(null)

            if (keyStore.containsAlias(keyName)) keyStore.deleteEntry(keyName)

            if (algorithm == "SHA1withRSA") {
                digest = KeyProperties.DIGEST_SHA1
            } else if (algorithm == "SHA256withRSA") {
                digest = KeyProperties.DIGEST_SHA256
            } else if (algorithm == "SHA512withRSA") {
                digest = KeyProperties.DIGEST_SHA512
            } else {
                digest = KeyProperties.DIGEST_SHA256 // EC
            }

            val keyGenParameterBuilder = KeyGenParameterSpec.Builder(
                keyName,
                purpose,
            )
                .setDigests(digest)
                .setUserAuthenticationRequired(authenticationRequired)
                .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)

            if (algorithm == "EC") {    // FIDO keys
                keyGenParameterBuilder.setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            } else {    // MFA keys
                // https://groups.google.com/forum/#!msg/android-developers/gDb8cJoSzqc/tJchSd0DDAAJ
                keyGenParameterBuilder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                keyGenParameterBuilder.setAlgorithmParameterSpec(
                    RSAKeyGenParameterSpec(
                        keySize,
                        RSAKeyGenParameterSpec.F4
                    )
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                keyGenParameterBuilder.setUserAuthenticationParameters(
                    0,
                    KeyProperties.AUTH_BIOMETRIC_STRONG
                ) else {
                @Suppress("deprecation")
                keyGenParameterBuilder.setUserAuthenticationValidityDurationSeconds(-1)
            }

            KeyPairGenerator.getInstance(
                if (algorithm == "EC") KeyProperties.KEY_ALGORITHM_EC else KeyProperties.KEY_ALGORITHM_RSA,
                keystoreType
            ).let {
                it.initialize(keyGenParameterBuilder.build())
                it.generateKeyPair()

                return keyStore.getCertificate(keyName).publicKey
            }
        } finally {
            log.exiting()
        }
    }

    /**
     * Delete a private and public key from the KeyStore.
     *
     * @param keyName  the unique identifier of the key pair
     */
    @Throws(KeyStoreException::class)
    fun deleteKeyPair(keyName: String) {
        log.entering()

        try {
            val keyStore = KeyStore.getInstance(keystoreType)
            keyStore.load(null)
            keyStore.deleteEntry(keyName)
        } finally {
            log.exiting()
        }
    }

    /**
     * Returns the public key retrieved from the keystore.
     *
     * @param keyName  the unique identifier of the key pair
     * @param base64EncodingOption  the encoding format of the public key
     *
     * @return String  encoded representation of the key or `null` if any error
     */
    fun exportPublicKey(keyName: String, base64EncodingOption: Int = Base64.DEFAULT): String? {

        log.entering()

        try {
            var key: String? = null
            KeyStore.getInstance(keystoreType).let { keyStore ->
                keyStore.load(null)
                keyStore.getCertificate(keyName)?.let { certificate ->
                    key = Base64.encodeToString(
                        certificate.publicKey.encoded,
                        base64EncodingOption
                    )
                }
            }

            return key
        } finally {
            log.exiting()
        }
    }

    /**
     * Query the keystore for a matching key name.
     *
     * @param keyName  the unique identifier of the key
     *
     * @return true  if the key exists, false otherwise
     *
     */
    fun exists(keyName: String): Boolean {

        log.entering()

        try {
            KeyStore.getInstance(keystoreType).let { keyStore ->
                keyStore.load(null)
                return keyStore.containsAlias(keyName)
            }
        } finally {
            log.exiting()
        }
    }

    /**
     * Query the Keystore for the private key.
     *
     * @param keyName  the unique identifier of the key pair
     *
     * @return the private key or null if the key is not found
     */
    @Nullable
    fun getPrivateKey(keyName: String): PrivateKey? {

        log.entering()

        try {
            var key: PrivateKey? = null
            KeyStore.getInstance(keystoreType).let { keyStore ->
                keyStore.load(null)
                keyStore.getKey(keyName, null)?.let {
                    key = it as PrivateKey
                }
            }

            return key
        } finally {
            log.exiting()
        }
    }

    @Nullable
    fun getSecretKey(keyName: String): SecretKey? {

        var key: SecretKey? = null
        KeyStore.getInstance(keystoreType).let { keyStore ->
            keyStore.load(null)
            keyStore.getKey(keyName, null)?.let {
                key = it as SecretKey
            }
        }

        return key
    }

    fun getPublicKey(keyName: String): PublicKey? {

        var key: PublicKey? = null
        KeyStore.getInstance(keystoreType).let { keyStore ->
            keyStore.load(null)
            keyStore.getCertificate(keyName)?.let { certificate ->
                key = certificate.publicKey
            }
        }

        return key
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> signData(
        keyName: String,
        algorithm: String,
        dataToSign: T,
        base64EncodingOption: Int = Base64.DEFAULT
    ): T? {

        log.entering()

        try {
            var signedData: T? = null
            Signature.getInstance(algorithm).let { signature ->
                getPrivateKey(keyName)?.let { privateKey ->
                    signature.initSign(privateKey)

                    when (dataToSign) {
                        is String -> {
                            signature.update(dataToSign.toByteArray())
                            signedData = Base64.encodeToString(signature.sign(), base64EncodingOption) as T
                        }
                        is ByteArray -> {
                            signature.update(dataToSign)
                            signedData = signature.sign() as T
                        }
                        else -> {
                            // Handle unsupported data type
                        }
                    }
                }
            }

            return signedData
        } finally {
            log.exiting()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> signData(
        cryptoObject: BiometricPrompt.CryptoObject,
        dataToSign: T,
        base64EncodingOption: Int = Base64.DEFAULT
    ): T? {

        log.entering()

        try {
            var signedData: T? = null
            cryptoObject.signature?.let { signature ->
                when (dataToSign) {
                    is String -> {
                        signature.update(dataToSign.toByteArray())
                        signedData = Base64.encodeToString(signature.sign(), base64EncodingOption) as T
                    }
                    is ByteArray -> {
                        signature.update(dataToSign)
                        signedData = signature.sign() as T
                    }
                    else -> {
                        // Handle unsupported data type
                    }
                }
            }

            return signedData
        } finally {
            log.exiting()
        }
    }

    internal fun hash(input: String, algorithm: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    internal fun hash(input: ByteArray, algorithm: String): String {
        return MessageDigest
            .getInstance(algorithm)
            .digest(input)
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}