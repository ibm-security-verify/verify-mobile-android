/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.cloud

import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.core.ContextHelper
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.mfa.EnrollableSignature
import com.ibm.security.verifysdk.mfa.FactorType
import com.ibm.security.verifysdk.mfa.InitializationInfo
import com.ibm.security.verifysdk.mfa.MFAAttributeInfo
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationError
import com.ibm.security.verifysdk.mfa.SignatureEnrollableFactor
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URL

class CloudRegistrationProvider(data: String) : MFARegistrationDescriptor<MFAAuthenticatorDescriptor> {

    private val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val initializationInfo: InitializationInfo

    private var registration: Registration? = null
    private var token: TokenInfo? = null
    private var factors: MutableList<FactorType> = mutableListOf()
    private var currentFactor: SignatureEnrollableFactor? = null

    override var pushToken: String = ""
    override var accountName: String = ""
    override val countOfAvailableEnrollments: Int = 0

    override fun nextEnrollment(): EnrollableSignature? {
        TODO("Not yet implemented")
    }

    override suspend fun enroll() {
        TODO("Not yet implemented")
    }

    override suspend fun enroll(name: String, publicKey: String, signedData: String) {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): MFAAuthenticatorDescriptor {
        TODO("Not yet implemented")
    }

    init {
        initializationInfo = decoder.decodeFromString(data)
        accountName = initializationInfo.accountName
        pushToken = ""
    }

    internal fun initiate(accountName: String, skipTotpEnrollment: Boolean = true, pushToken: String?) {
        this.accountName = accountName
        this.pushToken = pushToken.orEmpty()

        val attributes = MFAAttributeInfo.init(ContextHelper.context).dictionary().toMutableMap()
        attributes["accountName"] = this.accountName
        attributes["pushToken"] = this.pushToken
        attributes.remove("applicationName")

        val data: HashMap<String, Any> = HashMap<String, Any>()
        data["code"] = initializationInfo.code
        data["attributes"] = attributes

        val body = decoder.encodeToString(data)
        val registrationUrl = URL("${initializationInfo.uri}?skipTotpEnrollment=${skipTotpEnrollment}")



    }
}