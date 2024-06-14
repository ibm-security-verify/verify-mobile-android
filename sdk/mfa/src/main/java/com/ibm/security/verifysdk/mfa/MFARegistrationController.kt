/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import android.util.Log
import com.ibm.security.verifysdk.core.threadInfo
import com.ibm.security.verifysdk.mfa.cloud.CloudRegistrationProvider
import com.ibm.security.verifysdk.mfa.onprem.OnPremiseRegistrationProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory

class MFARegistrationController(private var data: String) {

    private val log = LoggerFactory.getLogger(javaClass)

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
        additionalHeaders: HashMap<String, String>? = null
    ): Result<MFARegistrationDescriptor<MFAAuthenticatorDescriptor>> {

        try {
            CloudRegistrationProvider(data).let { cloudRegistrationProvider ->
                cloudRegistrationProvider.initiate(accountName, skipTotpEnrollment, pushToken)
                    .let { resultInitiate ->
                        resultInitiate.onSuccess {
                            return Result.success(cloudRegistrationProvider)
                        }
                        resultInitiate.onFailure {
                            return Result.failure(it)
                        }
                    }
            }
        } catch (t: Throwable) {
            t.localizedMessage?.let { localizedMessage ->
                log.error(localizedMessage)
            }
        }

        try {
            OnPremiseRegistrationProvider(data).let { onPremiseRegistrationProvider ->
                onPremiseRegistrationProvider.initiate(
                    accountName,
                    skipTotpEnrollment,
                    pushToken,
                    additionalHeaders
                )
                    .let { resultInitiate ->
                        resultInitiate.onSuccess {
                            return Result.success(onPremiseRegistrationProvider)
                        }
                        resultInitiate.onFailure {
                            return Result.failure(it)
                        }
                    }
            }
        } catch (t: Throwable) {
            t.localizedMessage?.let { localizedMessage ->
                log.error(localizedMessage)
            }
        }

        return Result.failure(MFARegistrationError.InvalidFormat)
    }
}
