package com.ibm.security.verifysdk.dc.api

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.testutils.ApiMockEngine
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
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

@OptIn(ExperimentalSerializationApi::class)
@RunWith(Parameterized::class)
class InvitationsApiTest(private val inputUrl: String?) {

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
                arrayOf("/diagency/v1.0/diagency/invitations/"),
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
    fun delete() = runTest {
        apiMockEngine.addMockResponse(
            method = HttpMethod.Delete,
            urlPath = "/diagency/v1.0/diagency/invitations/${id}",
            httpCode = HttpStatusCode.NoContent
        )

        inputUrl?.let {
            InvitationsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            InvitationsApi(baseUrl = baseUrl).delete(
                accessToken = accessToken,
                id = id
            )
        }
            .onFailure {
                throw (it)
            }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/invitations/${id}")
    }

    @Test
    fun getAll() = runTest {
        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.inivtations_get_all_response)

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/invitations/",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            InvitationsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}")
            )
        } ?: run {
            InvitationsApi(baseUrl = baseUrl).getAll(
                accessToken = accessToken
            )
        }
            .onSuccess { invitationList ->
                assertEquals(33, invitationList.count)
                assertEquals("string", invitationList.items[0].url)
            }
            .onFailure { log.info(it.toString()) }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/invitations/")
    }

    @Test
    fun getOne() = runTest {
        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.invitations_get_one_response)

        apiMockEngine.addMockResponse(
            method = HttpMethod.Get,
            urlPath = "/diagency/v1.0/diagency/invitations/${id}",
            httpCode = HttpStatusCode.OK,
            responseBody = responseBody.toString()
        )

        inputUrl?.let {
            InvitationsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                url = URL("${baseUrl}${inputUrl}${id}"),
                id = id
            )
        } ?: run {
            InvitationsApi(baseUrl = baseUrl).getOne(
                accessToken = accessToken,
                id = id
            )
        }
            .onSuccess { invitationInfo ->
                assertEquals("string", invitationInfo.url)
                assertEquals(3, invitationInfo.timestamps?.count())
            }
            .onFailure { log.info(it.toString()) }
        apiMockEngine.checkLastRequestedUrl("/diagency/v1.0/diagency/invitations/${id}")
    }
}