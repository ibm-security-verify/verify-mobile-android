/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import org.json.JSONException
import org.json.JSONObject

class MFARegistrationController(json: String) {

    var ignoreSSLCertificate: Boolean = false
        private set

    init {
        JSONObject(json).let {jsonObject ->
            jsonObject.has("options")?.let {
                this.ignoreSSLCertificate = jsonObject.getString("options").contains("ignoreSslCerts=true")
            }

        }
    }

    suspend fun initiate(accountName: String, skipTotpEnrollment: Boolean = true, pushToken: String? = "", additionalData: Map<String, Any>? = null): MFARegistrationDescriptor {
        return when {
            try {
                CloudRegistrationProvider(json).initiate(accountName, skipTotpEnrollment, pushToken)
                true
            } catch (e: MFARegistrationError) {
                false
            }

            try {
                OnPremiseRegistrationProvider(json).initiate(accountName, skipTotpEnrollment, pushToken, additionalData)
                true
            } catch (e: MFARegistrationError) {
                false
            }
            else -> throw MFARegistrationError.InvalidFormat
        }
    }
}
