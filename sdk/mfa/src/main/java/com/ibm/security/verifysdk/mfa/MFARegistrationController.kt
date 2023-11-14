/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.mfa.cloud.CloudRegistrationProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class MFARegistrationController(private var data: String) {

    var ignoreSSLCertificate: Boolean = false
        private set

    init {

        val json = Json { ignoreUnknownKeys = true }
        val jsonObject: JsonObject = json.parseToJsonElement(data).jsonObject

        jsonObject.let {
            it.contains("options").let {
                this.ignoreSSLCertificate =
                    jsonObject["options"].toString()
                        .filter { c -> !c.isWhitespace() } == "ignoreSslCerts=true"
            }

        }
    }

    suspend fun initiate(
        accountName: String,
        skipTotpEnrollment: Boolean = true,
        pushToken: String? = "",
        additionalData: Map<String, Any>? = null
    ): Result<MFARegistrationDescriptor<MFAAuthenticatorDescriptor>> {
        val cloudRegistrationProvider = CloudRegistrationProvider(data)
        cloudRegistrationProvider.initiate(accountName, skipTotpEnrollment, pushToken)
            .let { resultInitiate ->
                resultInitiate.onSuccess {
                    return Result.success(cloudRegistrationProvider)
                }
                resultInitiate.onFailure {
                    return Result.failure(it)
                }
            }

        return Result.failure(MFARegistrationError.InvalidFormat)
    }
}
