/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.test.R.raw
import com.ibm.security.verifysdk.testutils.ApiMockEngine
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@RunWith(AndroidJUnit4::class)
class WalletProviderTest {

    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val baseUrl = URL("http://localhost")

    companion object {
        private var apiMockEngine = ApiMockEngine()

        @BeforeClass
        @JvmStatic
        fun setup() {
            NetworkHelper.initialize(httpClientEngine = apiMockEngine.get())
        }
    }

    @Before
    fun setUp() {
        apiMockEngine.get().config.requestHandlers.clear()
    }

    @After
    fun tearDown() {
        apiMockEngine.get().close()
    }

    @Test
    @Ignore
    fun initiate() = runTest {

        val hostLocal = "10.0.2.2"
        val hostRemote =
            "isvaop-default.isvavc-d7ed96fc9dc6db24d2d0bc7a632ccf66-0000.au-syd.containers.appdomain.cloud"

        //                    "url":"https://$hostLocal:9720/diagency/v1.0/diagency",

        val jsonData = """
            {
                "type":"agent",
                "data":{
                    "name":"user_1",
                    "id":"cn=user_1,ou=users,dc=ibm,dc=com",
                    "url":"http://localhost/diagency/v1.0/diagency",
                    "clientId":"onpremise_vcholders",
                    "tokenEndpoint": "https://$hostLocal:8436/oauth2/token"
                }
            }
            """

        val agentResponseBody =
            loadJsonFromRawResource(raw.agents_get_one_response)

        val invitationResponseBody =
            loadJsonFromRawResource(raw.invitations_get_all_response)

        val connectionResponseBody =
            loadJsonFromRawResource(raw.connections_get_all_response)

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/info/",
            httpCode = HttpStatusCode.OK,
            responseBody = agentResponseBody.toString()
        )

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/connections/",
            httpCode = HttpStatusCode.OK,
            responseBody = connectionResponseBody.toString()
        )

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/inventions/",
            httpCode = HttpStatusCode.OK,
            responseBody = invitationResponseBody.toString()
        )


        val walletProvider =
            WalletProvider(apiMockEngine.getEngine(), jsonData, ignoreSSLCertificate = true)
        val wallet = walletProvider.initiate("John", "user_1", "secret")
        log.info("Wallet created: $wallet")
    }
}