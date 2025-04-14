@file:OptIn(ExperimentalDigitalCredentialsSdk::class)

package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.model.UpdateCredentialArgs
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
class CredentialsApiTest(private val inputUrl: String?) {

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
                arrayOf("/diagency/v1.0/diagency/credentials/"),
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

    @Test
    fun getAll_3types() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_get_all_3types_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/credentials",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            CredentialsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            CredentialsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }

        result
            .onSuccess {
                log.info(it.toString())
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/credentials/")
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

        val result = inputUrl?.let {
            CredentialsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            CredentialsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }

        result
            .onSuccess {
                log.info(it.toString())
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/credentials/")
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

        val result = inputUrl?.let {
            CredentialsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            CredentialsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/credentials/${id}")
    }

    @Test
    fun delete() = runTest {
        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        val result = inputUrl?.let {
            CredentialsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            CredentialsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onFailure {
                log.info(it.toString())
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/credentials/${id}")
    }

    @Test
    fun update() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_response)
        val requestBody =
            json.decodeFromJsonElement<UpdateCredentialArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.credentials_patch_request))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Patch,
            urlPath = "/diagency/v1.0/diagency/credentials/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            CredentialsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id,
                updateCredentialArgs = requestBody
            )
        } ?: run {
            CredentialsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                id = id,
                updateCredentialArgs = requestBody
            )
        }

        result
            .onSuccess { credential ->
                assertEquals("string", credential.id)
                assertEquals("string", credential.issuerDid)
            }
            .onFailure {
                log.error(it.toString())
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl(
            "/diagency/v1.0/diagency/credentials/${id}",
            HttpMethod.Patch
        )
    }
}