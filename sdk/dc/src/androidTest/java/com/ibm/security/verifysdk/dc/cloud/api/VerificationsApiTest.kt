package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.api.VerificationsApi
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

@OptIn(ExperimentalDigitalCredentialsSdk::class)
@ExperimentalSerializationApi
@RunWith(Parameterized::class)
class VerificationsApiTest(private val inputUrl: String?) {

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
                arrayOf("/diagency/v1.0/diagency/verifications/"),
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
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.verifications_get_all_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/verifications",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            VerificationsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            VerificationsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }

        result
            .onSuccess {
                assertEquals(1, it.count())
                assertEquals("string", it[0].id)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/verifications/")
    }

    @Test
    fun getOne() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.verifications_get_one_response)
        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/verifications/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            VerificationsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            VerificationsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onSuccess {
                assertEquals("string", it.id)
            }
            .onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/verifications/${id}")
    }

    @Test
    fun delete() = runTest {
        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/verifications/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        val result = inputUrl?.let {
            VerificationsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            VerificationsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                id = id
            )
        }

        result
            .onFailure {
                log.info(it.toString())
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/verifications/${id}")
    }


    @Test
    fun update() = runTest {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.verifications_patch_response)
        val requestBody =
            json.decodeFromJsonElement<UpdateVerificationArgs>(loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.verifications_patch_request))

        apiMockEngine.addMockResponse(
            method = HttpMethod.Patch,
            urlPath = "/diagency/v1.0/diagency/verifications/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        val result = inputUrl?.let {
            VerificationsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id,
                updateVerificationArgs = requestBody
            )
        } ?: run {
            VerificationsApi(baseUrl = baseUrl).update(
                accessToken = accessToken,
                id = id,
                updateVerificationArgs = requestBody
            )
        }

        result
            .onSuccess {
                assertEquals("string", it.id)
                assertEquals("outbound_verification_request", it.state.value)
            }.onFailure {
                log.info(it.toString())
                throw (it)
            }

        apiMockEngine.checkLastRequestedUrl(
            "/diagency/v1.0/diagency/verifications/${id}",
            HttpMethod.Patch
        )
    }
}