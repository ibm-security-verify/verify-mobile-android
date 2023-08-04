/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import android.app.Application
import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.NetworkHelper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import java.util.UUID

class CloudAuthenticatorService(
    private var _accessToken: String,
    private var _refreshUri: URL,
    private var _transactionUri: URL,
    private val authenticatorId: String
) : MFAServiceDescriptor {

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

    internal enum class TransactionFilter(val value: String) {
        NEXT_PENDING("?filter=id,creationTime,transactionData,authenticationMethods&search=state=%22PENDING%22&sort=-creationTime"),
        PENDING_BY_IDENTIFIER("?filter=id,creationTime,transactionData,authenticationMethods&search=state=\\u{22}PENDING\\u{22}&id=\\u{22}%@\\u{22}")
    }

    override fun setCurrentPendingTransaction(value: PendingTransactionInfo?) {
        _currentPendingTransaction = value
    }

    override suspend fun refreshToken(
        refreshToken: String,
        accountName: String?,
        pushToken: String?,
        additionalData: Map<String, Any>?
    ): Result<TokenInfo> {

        return try {
            val attributes = mutableMapOf<String, Any>().apply {
                putAll(MFAAttributeInfo.init(Application().applicationContext).dictionary())
                remove("applicationName")
            }

            accountName?.let { attributes["accountName"] = it }
            pushToken?.let { attributes["pushToken"] = it }

            val data = mapOf(
                "refreshToken" to refreshToken,
                "attributes" to attributes
            )

            val requestBody: RequestBody =
                JSONObject(data).toString().toRequestBody("application/json".toMediaType())
            NetworkHelper.handleApi<TokenInfo>(
                NetworkHelper.networkApi()
                    .refresh(refreshUri.toString(), metadataInResponse = true, requestBody)
            )
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

            parsePendingTransaction(
                NetworkHelper.handleApi(
                    NetworkHelper.networkApi()
                        .transaction(transactionUri.toString(), accessToken)
                )
            )
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

            // Create the request parameters.
            val data = mapOf(
                "id" to pendingTransaction.factorID.toString().lowercase(Locale.ROOT),
                "userAction" to userAction.value,
                "signedData" to (if (userAction == UserAction.VERIFY) signedData else null)
            )

            val requestBody: RequestBody =
                JSONObject(data).toString().toRequestBody("application/json".toMediaType())

            NetworkHelper.handleApi(
                NetworkHelper.networkApi()
                    .completeTransaction(
                        pendingTransaction.postbackUri.toString(),
                        accessToken,
                        requestBody
                    )
            )
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    private fun parsePendingTransaction(response: Result<String>): Result<NextTransactionInfo> {

        response.onSuccess { responseBody ->
            val decoder = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }

            val result: TransactionResult = try {
                decoder.decodeFromString(responseBody)
            } catch (e: Exception) {
                return Result.failure(MFAServiceError.DecodingFailed())
            }

            // Check if there are no verifications, return a null.
            if (result.count == 0) {
                Result.success(NextTransactionInfo(null, 0))
            }

            // Create the pending transaction.
            val pendingTransaction: PendingTransactionInfo? = createPendingTransaction(result)

            return if (pendingTransaction != null) {
                Result.success(NextTransactionInfo(pendingTransaction, result.count))
            } else {
                Result.failure(MFAServiceError.UnableToCreateTransaction())
            }
        }

        response.onFailure {
            Result.failure(MFAServiceError.InvalidDataResponse())
        }
    }

    private fun createPendingTransaction(result: TransactionResult): PendingTransactionInfo? {
        // 1. Get the first transaction.
        val verificationInfo = result.verifications?.first() ?: return null

        // 2. Get the postback to the transaction.
        val postbackUri = URL(transactionUri, verificationInfo.id)

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
        val jsonArray = JSONArray(transactionInfo)

        jsonArray.let {
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val name = item.optString("name")
                val value = item.optString("value")

                when (name) {
                    "originIpAddress" -> result[TransactionAttribute.IPAddress] = value
                    "originUserAgent" -> result[TransactionAttribute.UserAgent] = value
                    "type" -> result[TransactionAttribute.Type] = value
                    "originLocation" -> result[TransactionAttribute.Location] = value
                    "imageURL" -> result[TransactionAttribute.Image] = value
                    else -> result[TransactionAttribute.Custom] = jsonArray.toString()
                }
            }
        }

        // Add the default type (of request) to the result if not already specified.
        result.putIfAbsent(TransactionAttribute.Type, "PendingRequestTypeDefault")

        return result
    }

    private fun transactionMessage(value: String): String {
        JSONObject(value).let {
            return it.optString("message", "PendingRequestMessageDefault")
        }
    }
}