/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import com.ibm.security.verifysdk.core.serializer.DefaultJson
import com.ibm.security.verifysdk.mfa.api.CloudRegistrationProvider
import com.ibm.security.verifysdk.mfa.api.OnPremiseRegistrationProvider
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.slf4j.LoggerFactory

class MFARegistrationController(private var data: String) {

    private val log = LoggerFactory.getLogger(javaClass)

    var ignoreSSLCertificate: Boolean = false
        private set

    init {

        val jsonObject: JsonObject = DefaultJson.parseToJsonElement(data).jsonObject

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
