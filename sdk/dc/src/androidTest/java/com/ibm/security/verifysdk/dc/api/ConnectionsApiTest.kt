/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.CreateConnectionArgs
import com.ibm.security.verifysdk.dc.model.UpdateConnectionArgs
import com.ibm.security.verifysdk.testutils.ApiMockEngine
import com.ibm.security.verifysdk.testutils.json
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromJsonElement
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@ExperimentalSerializationApi
@RunWith(Parameterized::class)
class ConnectionsApiTest(private val inputUrl: String?) {

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
                arrayOf("/diagency/v1.0/diagency/connections/"),
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
    fun getAll() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_get_all_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/connections",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            ConnectionsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            ConnectionsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }
            .onSuccess {
                assertEquals(22, it.count)
                assertEquals("string", it.items[0].id)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/connections/")
    }

    @Test
    fun getOne() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_get_one_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/connections/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            ConnectionsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            ConnectionsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                id = id
            )
        }
            .onSuccess {
                assertEquals("string", it.id)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/connections/${id}")
    }

    @Test
    fun delete() = runTest {

        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/connections/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        inputUrl?.let {
            ConnectionsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            ConnectionsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                id = id
            )
        }
            .onSuccess {
                assert(it === Unit)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/connections/${id}")

    }

    @Test
    fun create() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_post_response)
        val requestBody =
            json.decodeFromJsonElement<CreateConnectionArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_post_request))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Post,
            urlPath = "/diagency/v1.0/diagency/connections/",
            httpCode = HttpStatusCode.Created,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            ConnectionsApi(baseUrl = baseUrl).create(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}"),
                createConnectionArgs = requestBody
            )
        } ?: run {
            ConnectionsApi(baseUrl = baseUrl).create(
                accessToken = accessToken,
                createConnectionArgs = requestBody
            )
        }
            .onSuccess {
                assertEquals("string", it.invitation?.id)
            }.onFailure {
                log.info(it.toString())
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/connections/", HttpMethod.Post)
    }

    @Test
    fun update() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_patch_response)
        val requestBody =
            json.decodeFromJsonElement<UpdateConnectionArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.connections_patch_request))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Patch,
            urlPath = "/diagency/v1.0/diagency/connections/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            ConnectionsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                updateConnectionArgs = requestBody,
                id = id
            )
        } ?: run {
            ConnectionsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                updateConnectionArgs = requestBody,
                id = id
            )
        }
            .onSuccess { connection ->
                assertEquals("string", connection.invitation?.id)
            }.onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/connections/${id}", HttpMethod.Patch)
    }
}