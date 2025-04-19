/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.AgentInfoList
import com.ibm.security.verifysdk.testutils.ApiMockEngine
import com.ibm.security.verifysdk.testutils.json
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.skyscreamer.jsonassert.JSONAssert
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@OptIn(ExperimentalDigitalCredentialsSdk::class)
@RunWith(Parameterized::class)
class AgentsApiTest(private val inputUrl: String?) {

    @Suppress("unused")
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val baseUrl = URL("http://localhost")
    private val accessToken = "abcdef123456"
    private val id = "01234567-6789-abcd-efgh-jiklmnopqrst"

    companion object {
        private var apiMockEngine = ApiMockEngine()

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String?>> {
            return listOf(
                arrayOf("/diagency/v1.0/diagency/agents/"),
                arrayOf(null)
            )
        }

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
    fun getDecoder() {
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun getAll() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.agents_get_all_response)

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/agents/",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            AgentsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            AgentsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }

        result
            .onSuccess { agentList ->
                JSONAssert.assertEquals(
                    json.encodeToString(json.decodeFromString<AgentInfoList>(responseBody.toString()).items),
                    json.encodeToString(agentList),
                    true
                )

                assertEquals(1, agentList.count())
                assertEquals("string", agentList[0].id)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/agents/")
    }

    @Test
    fun getOne() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.agents_get_one_response)

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/agents/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            AgentsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            AgentsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onSuccess { agent ->
                assertEquals("string", agent.id)
                assertEquals("string", agent.name)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/agents/${id}")
    }

    @Test
    fun delete() = runTest {

        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/agents/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        val result = inputUrl?.let {
            AgentsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            AgentsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/agents/${id}")
    }
}