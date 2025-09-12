/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import android.util.Base64
import com.ibm.security.verifysdk.authentication.model.TokenInfo
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.net.URL

interface MFAServiceDescriptor {
    val accessToken: String
    val refreshUri: URL
    val transactionUri: URL
    val currentPendingTransaction: PendingTransactionInfo?
    val authenticatorId: String

    suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?,
        httpClient: HttpClient = NetworkHelper.getInstance
    ): Result<TokenInfo>

    suspend fun nextTransaction(
        transactionID: String? = null,
        httpClient: HttpClient = NetworkHelper.getInstance
    ): Result<NextTransactionInfo>

    suspend fun completeTransaction(
        userAction: UserAction,
        signedData: String,
        httpClient: HttpClient = NetworkHelper.getInstance
    ): Result<Unit>
}

typealias NextTransactionInfo = Pair<PendingTransactionInfo?, Int>

suspend fun MFAServiceDescriptor.login(
    loginUri: URL,
    code: String,
    httpClient: HttpClient = NetworkHelper.getInstance
): Result<Unit> {
    val body = buildJsonObject {
        put("lsi", code)
    }

    val decoder = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    return try {
        val response = httpClient.post {
            url(loginUri.toString())
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            bearerAuth(accessToken)
            setBody(body)
        }

        if (response.status.isSuccess()) {
            Result.success(decoder.decodeFromString<Unit>(response.bodyAsText()))
        } else {
            Result.failure(MFAServiceError.General(response.bodyAsText()))
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

suspend fun MFAServiceDescriptor.completeTransaction(
    userAction: UserAction = UserAction.VERIFY,
    factorType: FactorType,
    httpClient: HttpClient = NetworkHelper.getInstance
): Result<Unit> {
    var signedData = ""
    val pendingTransaction =
        currentPendingTransaction ?: throw MFAServiceError.InvalidPendingTransaction()

    if (userAction == UserAction.VERIFY) {
        val (keyName, algorithm) = factorKeyNameAndAlgorithm(factorType)
        signedData = sign(
            keyName = keyName,
            algorithm = HashAlgorithmType.forSigning(algorithm.name),
            dataToSign = pendingTransaction.dataToSign,
            base64EncodingOptions = Base64.NO_WRAP
        )
    }

    return completeTransaction(
        userAction = userAction,
        signedData = signedData,
        httpClient = httpClient
    )
}

internal fun factorKeyNameAndAlgorithm(factorType: FactorType): Pair<String, HashAlgorithmType> {

    return when (factorType) {
        is FactorType.Face -> Pair(factorType.value.keyName, factorType.value.algorithm)
        is FactorType.Fingerprint -> Pair(factorType.value.keyName, factorType.value.algorithm)
        is FactorType.UserPresence -> Pair(factorType.value.keyName, factorType.value.algorithm)
        else -> throw MFAServiceError.General("Invalid factor to perform signing.")
    }
}
