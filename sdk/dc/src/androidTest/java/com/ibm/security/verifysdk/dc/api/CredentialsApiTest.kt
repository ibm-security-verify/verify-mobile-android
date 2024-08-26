package com.ibm.security.verifysdk.dc.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.model.UpdateCredentialArgs
import com.ibm.security.verifysdk.testutils.ApiMockEngine
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@ExperimentalSerializationApi
@RunWith(AndroidJUnit4::class)
class CredentialsApiTest {

    @Suppress("unused")
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val baseUrl = URL("http://localhost")
    private val accessToken = "abcdef123456"
    private val id = "01234567-6789-abcd-efgh-jiklmnopqrst"


    private val json = Json

    companion object {
        private var apiMockEngine = ApiMockEngine()

        @BeforeClass
        @JvmStatic
        fun setup() {
            NetworkHelper.initialize(httpClientEngine = apiMockEngine.get())
            apiMockEngine.get().config.requestHandlers.clear()
        }
    }

    @Before
    fun setUp() {
        apiMockEngine.get().config.requestHandlers.clear()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getDecoder() {

    }

    @Test
    fun getAll() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_get_all_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/credentials",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        CredentialsApi(baseUrl = baseUrl).getAll(accessToken = accessToken)
            .onSuccess {
                log.info(it.toString())
            }
            .onFailure {
                log.info(it.toString())
            }
    }

    @Test
    fun getAll_withUrl() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_get_all_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/credentials",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        CredentialsApi(baseUrl = baseUrl).getAll(
            accessToken = accessToken,
            url = URL("$baseUrl/diagency/v1.0/diagency/credentials")
        )
            .onSuccess { credentialList ->
                assertEquals(1, credentialList.count)
                assertEquals("string", credentialList.items[0].id)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
    }

    @Test
    fun getOne() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_get_one_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        CredentialsApi(baseUrl = baseUrl).getOne(
            accessToken = accessToken,
            id = id
        )
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
    }

    @Test
    fun getOne_withUrl() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_get_one_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        CredentialsApi(baseUrl = baseUrl).getOne(
            accessToken = accessToken,
            url = URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}"),
            id = id
        )
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
    }

    @Test
    fun delete() = runTest {
        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        CredentialsApi(baseUrl).delete(
            accessToken = accessToken,
            id = id
        )
            .onFailure {
                throw (it)
            }

        apiMockEngine.get().requestHistory.last().let { requestData ->
            val requestBody = requestData.body.toByteArray().toString(Charsets.UTF_8)
            assertTrue(requestBody.isEmpty())
            assertEquals(
                "/diagency/v1.0/diagency/credentials/${id}",
                requestData.url.encodedPath
            )
        }
    }

    @Test
    fun delete_withUrl() = runTest {
        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        CredentialsApi(baseUrl).delete(
            accessToken = accessToken,
            url = URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}"),
            id = id
        )
            .onFailure {
                throw (it)
            }

        apiMockEngine.get().requestHistory.last().let { requestData ->
            val requestBody = requestData.body.toByteArray().toString(Charsets.UTF_8)
            assertTrue(requestBody.isEmpty())
            assertEquals(
                "/diagency/v1.0/diagency/credentials/${id}",
                requestData.url.encodedPath
            )
        }
    }

    @Test
    fun update() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_response)
        val requestBody =
            json.decodeFromJsonElement<UpdateCredentialArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_request_body))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Patch,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            responseBody = responseBody.toString(),
            httpCode = HttpStatusCode.OK
        )

        CredentialsApi(baseUrl).update(
            accessToken = accessToken,
            id = id,
            updateCredentialArgs = requestBody
        )
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
    }

    @Test
    fun update_with_url() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_response)
        val requestBody =
            json.decodeFromJsonElement<UpdateCredentialArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_request_body))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Patch,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            responseBody = responseBody.toString(),
            httpCode = HttpStatusCode.OK
        )

        CredentialsApi(baseUrl).update(
            accessToken = accessToken,
            url = URL("$baseUrl/diagency/v1.0/diagency/credentials/${id}"),
            id = id,
            updateCredentialArgs = requestBody
        )
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
    }
}