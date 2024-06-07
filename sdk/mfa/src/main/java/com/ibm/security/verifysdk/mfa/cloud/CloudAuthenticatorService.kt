/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.AuthorizationException
import com.ibm.security.verifysdk.core.ErrorResponse
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.mfa.MFAAttributeInfo
import com.ibm.security.verifysdk.mfa.MFAServiceDescriptor
import com.ibm.security.verifysdk.mfa.MFAServiceError
import com.ibm.security.verifysdk.mfa.NextTransactionInfo
import com.ibm.security.verifysdk.mfa.PendingTransactionInfo
import com.ibm.security.verifysdk.mfa.TransactionAttribute
import com.ibm.security.verifysdk.mfa.TransactionResult
import com.ibm.security.verifysdk.mfa.UserAction
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
class CloudAuthenticatorService(
    private var _accessToken: String,
    private var _refreshUri: URL,
    private var _transactionUri: URL,
    private var _authenticatorId: String
) : MFAServiceDescriptor {

    private val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private var _currentPendingTransaction: PendingTransactionInfo? = null
    private var transactionResult: TransactionResult? = null

    override val accessToken: String
        get() = _accessToken

    override val refreshUri: URL
        get() = _refreshUri

    override val transactionUri: URL
        get() = _transactionUri

    override val currentPendingTransaction: PendingTransactionInfo?
        get() = _currentPendingTransaction

    override val authenticatorId: String
        get() = _authenticatorId

    internal enum class TransactionFilter(val value: String) {
        NEXT_PENDING("?filter=id,creationTime,transactionData,authenticationMethods&search=state=%22PENDING%22&sort=-creationTime"),
        PENDING_BY_IDENTIFIER("?filter=id,creationTime,transactionData,authenticationMethods&search=state=\\u{22}PENDING\\u{22}&id=\\u{22}%@\\u{22}")
    }

    override suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo> {

        return try {
            val attributes = mutableMapOf<String, Any>().apply {
                putAll(MFAAttributeInfo.dictionary())
                remove("applicationName")
            }

            accountName?.let { attributes["accountName"] = it }
            pushToken?.let { attributes["pushToken"] = it }

            val data = mapOf(
                "refreshToken" to refreshToken,
                "attributes" to attributes
            )

            val response = NetworkHelper.getInstance.post {
                url(refreshUri.toString())
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                parameter("metadataInResponse", true)
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(decoder.decodeFromString<TokenInfo>(response.bodyAsText()))
            } else {
                val errorResponse = response.body<ErrorResponse>()
                Result.failure(
                    AuthorizationException(
                        response.status,
                        errorResponse.error,
                        errorResponse.errorDescription
                    )
                )
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }


    override suspend fun nextTransaction(transactionID: String?): Result<NextTransactionInfo> {

        return try {
            var transactionUri = URL("${transactionUri}${TransactionFilter.NEXT_PENDING.value}")
            if (transactionID != null) {
                transactionUri =
                    URL("${transactionUri}/${TransactionFilter.PENDING_BY_IDENTIFIER.value}/${transactionID}")
            }

            val response = NetworkHelper.getInstance.get {
                url(transactionUri.toString())
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                bearerAuth(accessToken)
            }

            if (response.status.isSuccess()) {
                parsePendingTransaction(response)
                    .onSuccess {
                        _currentPendingTransaction = it.first
                    }
            } else {
                Result.failure(MFAServiceError.General(response.bodyAsText()))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    override suspend fun completeTransaction(
        userAction: UserAction,
        signedData: String
    ): Result<Unit> {

        return try {
            val pendingTransaction =
                currentPendingTransaction ?: throw MFAServiceError.InvalidPendingTransaction()

            val data = arrayOf(mapOf(
                "id" to pendingTransaction.factorID.toString().lowercase(Locale.ROOT),
                "userAction" to userAction.value,
                "signedData" to (if (userAction == UserAction.VERIFY) signedData else null)
            ))

            val response = NetworkHelper.getInstance.post {
                url(pendingTransaction.postbackUri.toString())
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                bearerAuth(accessToken)
                setBody(data)
            }

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(MFAServiceError.General(response.bodyAsText()))
            }
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    private suspend fun parsePendingTransaction(response: HttpResponse): Result<NextTransactionInfo> {

        return try {
            val responseBody = response.bodyAsText()
            val result: TransactionResult = try {
                decoder.decodeFromString(responseBody)
            } catch (e: Exception) {
                return Result.failure(MFAServiceError.DecodingFailed())
            }

            if (result.count == 0) {
                Result.success(NextTransactionInfo(null, 0))
            }

            createPendingTransaction(result)?.let {
                Result.success(NextTransactionInfo(it, result.count))
            } ?: kotlin.run {
                Result.failure(MFAServiceError.UnableToCreateTransaction())
            }

        } catch (e: Throwable) {
            Result.failure(MFAServiceError.UnableToCreateTransaction())
        }
    }

    private fun createPendingTransaction(result: TransactionResult): PendingTransactionInfo? {
        // 1. Get the first transaction.
        val verificationInfo = result.verifications?.first() ?: return null

        // 2. Get the postback to the transaction.
        val postbackUri = URL(transactionUri, "verifications/${verificationInfo.id}")

        // 3. Get the message to display.
        val message = transactionMessage(verificationInfo.transactionInfo)

        // 4. Construct the factor that is used to look up the private key from the Keychain.
        val methodInfo = verificationInfo.methodInfo.firstOrNull() ?: return null

        // 5. Construct the transaction context information into additional data.
        val additionalData = createAdditionalData(verificationInfo.transactionInfo)

        return PendingTransactionInfo(
            id = verificationInfo.id,
            message = message,
            postbackUri = postbackUri,
            factorID = UUID.fromString(methodInfo.id) ?: UUID.randomUUID(),
            factorType = methodInfo.subType,
            dataToSign = verificationInfo.transactionInfo,
            timeStamp = verificationInfo.creationTime,
            additionalData = additionalData
        )
    }

    private fun createAdditionalData(transactionInfo: String): Map<TransactionAttribute, String> {
        val result: MutableMap<TransactionAttribute, String> = mutableMapOf()

        // Add the default type (of request) to the result.  Might be overriden if specified in additionalData.
        result.putIfAbsent(TransactionAttribute.Type, "PendingRequestTypeDefault")

        JSONObject(transactionInfo).let { transactionData ->
            transactionData.has("originIpAddress").let {
                result[TransactionAttribute.IPAddress] =
                    transactionData.optString("originIpAddress")
                transactionData.remove("originIpAddress")
            }

            transactionData.has("originUserAgent").let {
                result[TransactionAttribute.UserAgent] =
                    transactionData.optString("originUserAgent")
                transactionData.remove("originUserAgent")
            }

            transactionData.has("additionalData").let {
                val additionalData = JSONArray(transactionData.optString("additionalData"))
                val indicesToRemove = mutableListOf<Int>()

                for (i in 0 until additionalData.length()) {
                    val item = additionalData.getJSONObject(i)
                    val name = item.optString("name")

                    if (name.equals("type")) {
                        result[TransactionAttribute.Type] = item.optString("value")
                        indicesToRemove.add(i)
                    } else if (name.equals("originLocation")) {
                        result[TransactionAttribute.Location] = item.optString("value")
                        indicesToRemove.add(i)
                    } else if (name.equals("imageURL")) {
                        result[TransactionAttribute.Image] = item.optString("value")
                        indicesToRemove.add(i)
                    }
                }

                for (index in indicesToRemove.reversed()) {
                    additionalData.remove(index)
                }

                if (additionalData.length() > 0) {
                    result[TransactionAttribute.Custom] = additionalData.toString()
                }
            }
        }

        return result
    }

    private fun transactionMessage(value: String): String {
        JSONObject(value).let {
            return it.optString("message", "PendingRequestMessageDefault")
        }
    }
}