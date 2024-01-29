@file:OptIn(ExperimentalStdlibApi::class)

package com.ibm.security.verify.fido2

import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper
import com.ibm.security.verify.fido2.model.AssertionOptions
import com.ibm.security.verify.fido2.model.AttestationOptions
import com.ibm.security.verify.fido2.model.AuthenticatorAssertionResponse
import com.ibm.security.verify.fido2.model.AuthenticatorAttestationResponse
import com.ibm.security.verify.fido2.model.ClientDataJsonAssertion
import com.ibm.security.verify.fido2.model.ClientDataJsonAttestation
import com.ibm.security.verify.fido2.model.PublicKeyCredentialCreationOptions
import com.ibm.security.verify.fido2.model.PublicKeyCredentialRequestOptions
import com.ibm.security.verify.fido2.model.ResponseAssertion
import com.ibm.security.verify.fido2.model.ResponseAttestation
import com.ibm.security.verifysdk.core.KeystoreHelper
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.core.base64UrlEncode
import com.ibm.security.verifysdk.core.sha256
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.interfaces.ECPublicKey
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalUnsignedTypes::class)
class Fido2Api {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun initiateAttestation(
        attestationOptionsUrl: String,
        accessToken: String,
        attestationOptions: AttestationOptions
    ): Result<PublicKeyCredentialCreationOptions> {
        return try {
            NetworkHelper.handleApi<PublicKeyCredentialCreationOptions>(
                NetworkHelper.networkApi.attestationOptions(
                    HashMap(),
                    attestationOptionsUrl,
                    accessToken,
                    attestationOptions.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun initiateAssertion(
        assertionOptionsUrl: String,
        accessToken: String,
        assertionOptions: AssertionOptions
    ): Result<PublicKeyCredentialRequestOptions> {
        return try {
            NetworkHelper.handleApi<PublicKeyCredentialRequestOptions>(
                NetworkHelper.networkApi.attestationOptions(
                    HashMap(),
                    assertionOptionsUrl,
                    accessToken,
                    assertionOptions.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                )
            )
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private fun buildAttestationStatement(
        keyName: String,
        clientDataString: String,
        authenticatorData: ByteArray
    ): Map<String, Any> {

        val result = HashMap<String, Any>()
        val dataToSign = mutableListOf<Byte>()
        dataToSign.addAll(authenticatorData.toList())
        dataToSign.addAll(clientDataString.sha256().hexToByteArray().toList())

        val sig = KeystoreHelper.signData(
            keyName,
            "SHA256withECDSA",
            dataToSign.toByteArray()
        )

        result["alg"] = -7
        result["sig"] = sig!!
        return result
    }

    private fun toUnsignedFixedLength(arr: ByteArray, fixedLength: Int): ByteArray {
        val fixed = ByteArray(fixedLength)
        val offset = fixedLength - arr.size
        val copyLength = min(arr.size, fixedLength)
        System.arraycopy(arr, max(-offset, 0), fixed, max(offset, 0), copyLength)
        return fixed
    }

    private fun getCoseKey(keyName: String): COSEKey {
        val ecPublicKey = KeystoreHelper.getPublicKey(keyName) as ECPublicKey
        val point = ecPublicKey.w
        val x: ByteArray = toUnsignedFixedLength(point.affineX.toByteArray(), 32)
        val y: ByteArray = toUnsignedFixedLength(point.affineY.toByteArray(), 32)
        return COSEKey(2, -7, 1, x, y)
    }

    fun buildAuthenticatorAttestationResponse(
        aaGuid: String,
        keyName: String,
        flags: Byte,
        clientDataJson: ClientDataJsonAttestation,
        options: PublicKeyCredentialCreationOptions
    ): AuthenticatorAttestationResponse {

        val id = keyName.sha256().hexToByteArray()
        val clientDataString = json.encodeToString(clientDataJson)

        // Build authenticatorData
        val authenticatorDataParams = mutableListOf<Byte>()
        authenticatorDataParams.addAll(options.rp.id!!.sha256().hexToByteArray().toList())
        authenticatorDataParams.add(flags)

        val now = Clock.System.now().epochSeconds
        val counter = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(now.toInt()).array()
        authenticatorDataParams.addAll(counter.toList())

        // Build attestedCredentialData
        val attestedCredentialData = mutableListOf<Byte>()
        attestedCredentialData.addAll(aaGuid.replace("-", "").hexToByteArray().toList())

        val length = byteArrayOf(
            ((id.size shr 8) and 0xFF).toByte(),
            (id.size and 0xFF).toByte()
        )

        attestedCredentialData.addAll(length.toList())
        // Add the credentialId
        attestedCredentialData.addAll(id.toList())

        val coseKey = getCoseKey(keyName)
        attestedCredentialData.addAll(coseKey.toByteArray.toList())

        // Add attestedCredential to authenticatorData
        authenticatorDataParams.addAll(attestedCredentialData)

        val attestationStatement = buildAttestationStatement(
            keyName,
            clientDataString,
            authenticatorDataParams.toByteArray()
        )

        val attestedObject = HashMap<String, Any>()
        attestedObject["fmt"] = "packed"
        attestedObject["attStmt"] = attestationStatement
        attestedObject["authData"] = authenticatorDataParams.toByteArray()

        return AuthenticatorAttestationResponse(
            id.base64UrlEncode(),
            id.base64UrlEncode(),
            ResponseAttestation(
                clientDataString.base64UrlEncode(),
                CBORMapper().writeValueAsBytes(attestedObject).toUByteArray().base64UrlEncode()
            ),
            "public-key"
        )
    }

    fun buildAuthenticatorAssertionResponse(
        options: PublicKeyCredentialRequestOptions,
        clientDataJson: ClientDataJsonAssertion,
        flags: Byte,
        keyName: String,
        message: String?
    ): AuthenticatorAssertionResponse {

        val id = keyName.sha256().hexToByteArray()
        val clientDataString = json.encodeToString(clientDataJson)

        val authenticatorDataParams = mutableListOf<Byte>()
        authenticatorDataParams.addAll(options.rpId.sha256().hexToByteArray().toList())
        authenticatorDataParams.add(flags)

        val now = Clock.System.now().epochSeconds
        val counter = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(now.toInt()).array()
        authenticatorDataParams.addAll(counter.toList())

        message?.let {
            val extension = HashMap<String, Any>()
            extension["txAuthSimple"] = message
            authenticatorDataParams.addAll(CBORMapper().writeValueAsBytes(extension).toList())
        }

        val dataToSign = mutableListOf<Byte>()
        dataToSign.addAll(authenticatorDataParams.toList())
        dataToSign.addAll(clientDataString.sha256().hexToByteArray().toList())

        val sig = KeystoreHelper.signData(
            keyName,
            "SHA256withECDSA",
            dataToSign.toByteArray()
        )!!.base64UrlEncode()

        return AuthenticatorAssertionResponse(
            id.base64UrlEncode(),
            id.base64UrlEncode(),
            ResponseAssertion(
                clientDataString.base64UrlEncode(),
                authenticatorDataParams.toByteArray().base64UrlEncode(),
                sig
            ),
            "public-key"
        )
    }
}