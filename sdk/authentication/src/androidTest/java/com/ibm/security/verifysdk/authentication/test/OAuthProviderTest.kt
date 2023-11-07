package com.ibm.security.verifysdk.authentication.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.authentication.OAuthProvider
import com.ibm.security.verifysdk.core.AuthenticationException
import com.ibm.security.verifysdk.core.NetworkHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalSerializationApi
@RunWith(AndroidJUnit4::class)
internal class OAuthProviderTest {

    @Suppress("unused")
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {

        private const val clientId = "clientId"
        private const val clientSecret = "clientSecret"
        private var oAuthProvider = OAuthProvider(clientId, clientSecret)

        private var okHttpClientBuilder = OkHttpClient.Builder()

        private var mockWebServer = MockWebServer()
        private const val mockWebServerPort = 44444

        @BeforeClass
        @JvmStatic
        fun setup() {
            mockWebServer.start(mockWebServerPort)

            okHttpClientBuilder.addNetworkInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )

            val retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .client(okHttpClientBuilder.build())
                .build()

            NetworkHelper.retrofit = retrofit
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            mockWebServer.shutdown()
        }
    }

    @Before
    fun initialize() {
        oAuthProvider = OAuthProvider(clientId, clientSecret)
    }

    @Suppress("unused")
    private fun addMockResponse(httpCode: Int, body: String) {
        mockWebServer.enqueue(MockResponse().setResponseCode(httpCode).setBody(body))
    }

    @Suppress("unused")
    private fun addMockResponse(httpCode: Int, body: String, socketPolicy: SocketPolicy) {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(httpCode).setSocketPolicy(socketPolicy).setBody(body)
        )
    }

    @Suppress("unused")
    private fun addMockResponse(httpCode: Int, body: String, header: HashMap<String, String>) {
        val mockResponse = MockResponse().setResponseCode(httpCode).setBody(body)
        header.forEach { (key, value) -> mockResponse.addHeader(key, value) }
        mockWebServer.enqueue(mockResponse)
    }

    @Test
    fun refresh_clientSecretIsNull_shouldReturnSuccess() = runTest {
        val oAuthProviderSecretNull = OAuthProvider(clientId, null)
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProviderSecretNull.refresh(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "refreshToken",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("refresh_token=refreshToken"))
            assertTrue(body.contains("grant_type=refresh_token"))
            assertTrue(body.contains("scope=name%20age"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun refresh_withScope_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseRefreshOk)
        val result =
            oAuthProvider.refresh(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "refreshToken",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("refresh_token=refreshToken"))
            assertTrue(body.contains("grant_type=refresh_token"))
            assertTrue(body.contains("scope=name%20age"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun refresh_happyPath_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseRefreshOk)
        val result =
            oAuthProvider.refresh(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "refreshToken"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("refresh_token=refreshToken"))
            assertTrue(body.contains("grant_type=refresh_token"))
            assertTrue(body.contains("scope="))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun refresh_emptyBody_shouldReturnFailure() = runTest {
        addMockResponse(200, "")
        val result =
            oAuthProvider.refresh(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "refreshToken"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS) // not used

        assertTrue(result.isFailure)
    }

    @Test
    fun authorize_codeHappyPath_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                URL("https://callback"),
                "authorizationCode",
                "codeVerifier",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("grant_type=authorization_code"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_codeVerifierIsNull_shouldReturnSuccess() = runTest {

        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                URL("https://callback"),
                "authorizationCode",
                null,
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("code_verifier=&"))
            assertTrue(body.contains("grant_type=authorization_code"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_codeClientSecretIsNull_shouldReturnSuccess() = runTest {
        val oAuthProviderSecretNull =
            OAuthProvider(clientId, null)
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProviderSecretNull.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                URL("https://callback"),
                "authorizationCode",
                "codeVerifier",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("code_verifier=codeVerifier"))
            assertTrue(body.contains("grant_type=authorization_code"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_codeServerError_shouldReturnFailure() = runTest {
        addMockResponse(500, "Server error")
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                URL("https://callback"),
                "authorizationCode",
                "codeVerifier",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS) // not used

        assertTrue(result.isFailure)
        result.onFailure { it ->
            assertTrue(it is AuthenticationException)
            (it as AuthenticationException).let {
                assertEquals("IVMCR0001E", it.error)
                assertEquals("Server error", it.errorDescription)
            }
        }
    }

    @Test
    fun authorize_codeEmptyBody_shouldReturnFailure() = runTest {
        addMockResponse(200, "")
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                URL("https://callback"),
                "authorizationCode",
                "codeVerifier",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("code_verifier=codeVerifier"))
            assertTrue(body.contains("grant_type=authorization_code"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isFailure)
        result.onFailure {
            assertTrue(it is SerializationException)
            assertTrue(
                it.message.toString()
                    .contains("Expected start of the object '{', but had 'EOF' instead")
            )
        }
    }


    @Test
    fun authorize_credsHappyPath_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("username=username"))
            assertTrue(body.contains("grant_type=password"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_credsWithScope_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("username=username"))
            assertTrue(body.contains("grant_type=password"))
            assertTrue(body.contains("scope=name%20age"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_credsClientSecretIsNull_shouldReturnSuccess() = runTest {
        val oAuthProviderSecretNull =
            OAuthProvider(clientId, null)
        addMockResponse(200, responseAuthorizeOk)
        val result =
            oAuthProviderSecretNull.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password",
                arrayOf("name", "age")
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("POST", request.method)
            val body = request.body.readUtf8()
            assertTrue(body.contains("client_id=clientId"))
            assertTrue(body.contains("client_secret=&"))
            assertTrue(body.contains("username=username"))
            assertTrue(body.contains("grant_type=password"))
            assertTrue(body.contains("scope=name%20age"))
        } ?: assert(false) { "Request timed out" }

        assertTrue(result.isSuccess)
        result.onSuccess { token ->
            assertEquals("A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn", token.accessToken)
        }
    }

    @Test
    fun authorize_credsEmptyBody_shouldReturnFailure() = runTest {
        addMockResponse(200, "")
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS) // not used

        assertTrue(result.isFailure)
        result.onFailure {
            assertTrue(it is SerializationException)
            assertTrue(
                it.message.toString()
                    .contains("Expected start of the object '{', but had 'EOF' instead")
            )
        }
    }

    @Test
    fun authorize_credsServerError_shouldReturnFailure() = runTest {
        addMockResponse(500, "Server error")
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS) // not used

        assertTrue(result.isFailure)
        result.onFailure { it ->
            assertTrue(it is AuthenticationException)
            (it as AuthenticationException).let {
                assertEquals("IVMCR0001E", it.error)
                assertEquals("Server error", it.errorDescription)
            }
        }
    }


    /**
     * An empty token is returned with the unknown attributes in the response added
     * to [TokenInfo.additionalData]
     */
    @Test
    fun authorize_credsResponseUnknownJson_shouldReturnSuccess() = runTest {
        addMockResponse(
            200,
            "{\"foo\":\"bar\"}"
        )
        val result =
            oAuthProvider.authorize(
                URL("http://localhost:44444/v1.0/endpoint/default/token)"),
                "username",
                "password"
            )

        mockWebServer.takeRequest(10, TimeUnit.SECONDS) // not used

        assertTrue(result.isSuccess)
    }

    @Test
    fun discover_serverError_shouldReturnFailure() = runTest {

        addMockResponse(500, "Server error")
        val result =
            oAuthProvider.discover(URL("http://localhost:44444/.well-known/openid-configuration"))

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)?.let { request ->
            assertEquals("GET", request.method)
        }

        assertTrue(result.isFailure)
        result.onFailure { it ->
            assertTrue(it is AuthenticationException)
            (it as AuthenticationException).let {
                assertEquals("IVMCR0001E", it.error)
                assertEquals("Server error", it.errorDescription)
            }
        }
    }

    @Test
    fun discover_happyPath_shouldReturnSuccess() = runTest {
        addMockResponse(200, responseDiscoveryOk)
        val result =
            oAuthProvider.discover((URL("http://localhost:44444/.well-known/openid-configuration")))

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(13, it.idTokenSigningAlgValuesSupported.size)
            assertEquals("https://sdk.verify.ibm.com/v1.0/endpoint/default/token", it.tokenEndpoint)
        }
    }

    @Test
    fun discover_wrongUrlPath_shouldReturnFailure() = runTest {

        val result = oAuthProvider.discover(URL("https://localhost"))
        assertTrue(result.isFailure)
        result.onFailure {
            assertTrue(it is MalformedURLException)
        }
    }

    @Test
    fun discover_fieldsMissingInResponse_shouldReturnFailure() = runTest {
        addMockResponse(
            200,
            "{\"foo\":\"bar\"}"
        )
        val result =
            oAuthProvider.discover(URL("http://localhost:44444/.well-known/openid-configuration"))

        mockWebServer.takeRequest(10, TimeUnit.SECONDS)

        assertTrue(result.isFailure)
        result.onFailure {
            assertTrue(it is SerializationException)
            assertTrue(it.message?.endsWith("but they were missing") ?: false)
        }
    }

    @Test
    fun constructor_withAdditionalData_shouldReturnInstance() {

        val headers = HashMap<String, String>()
        headers["key1"] = "value1"

        val parameters = HashMap<String, String>()
        parameters["key2"] = "value2"

        val oAuthProvider = OAuthProvider(clientId, clientSecret, headers, parameters)
        assertEquals(1, oAuthProvider.additionalHeaders.size)
        assertEquals(1, oAuthProvider.additionalParameters.size)
        assertEquals("value1", oAuthProvider.additionalHeaders["key1"])
        assertEquals("value2", oAuthProvider.additionalParameters["key2"])
    }

    @Test
    fun constructor_withAdditionalDataAsNull_shouldReturnInstance() {

        val oAuthProvider = OAuthProvider(clientId, clientSecret, null, null)
        assertEquals(0, oAuthProvider.additionalHeaders.size)
        assertEquals(0, oAuthProvider.additionalParameters.size)
    }

    @Test
    fun getAdditionalHeaders() {
        assertEquals(0, oAuthProvider.additionalHeaders.size)
    }

    @Test
    fun setAdditionalHeaders() {
        assertEquals(0, oAuthProvider.additionalHeaders.size)
        oAuthProvider.additionalHeaders["key1"] = "value1"
        assertEquals(1, oAuthProvider.additionalHeaders.size)
        assertEquals("value1", oAuthProvider.additionalHeaders["key1"])

        val headers = HashMap<String, String>()
        headers["key2"] = "value2"
        oAuthProvider.additionalHeaders = headers
        assertEquals(1, oAuthProvider.additionalHeaders.size)
        assertEquals("value2", oAuthProvider.additionalHeaders["key2"])

    }

    @Test
    fun getAdditionalParameters() {
        assertEquals(0, oAuthProvider.additionalParameters.size)
    }

    @Test
    fun setAdditionalParameters() {
        assertEquals(0, oAuthProvider.additionalParameters.size)
        oAuthProvider.additionalParameters["key1"] = "value1"
        assertEquals(1, oAuthProvider.additionalParameters.size)
        assertEquals("value1", oAuthProvider.additionalParameters["key1"])

        val parameters = HashMap<String, String>()
        parameters["key2"] = "value2"
        oAuthProvider.additionalParameters = parameters
        assertEquals(1, oAuthProvider.additionalParameters.size)
        assertEquals("value2", oAuthProvider.additionalParameters["key2"])
    }


    @Test
    fun getClientId() {
        assertEquals("clientId", oAuthProvider.clientId)
    }

    @Test
    fun getClientSecret() {
        assertEquals("clientSecret", oAuthProvider.clientSecret)
    }

    private val responseDiscoveryOk = """
        {
           "request_parameter_supported":true,
           "introspection_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/introspect",
           "claims_parameter_supported":true,
           "scopes_supported":[
              "openid",
              "profile",
              "email",
              "phone"
           ],
           "issuer":"https://sdk.verify.ibm.com/oidc/endpoint/default",
           "id_token_encryption_enc_values_supported":[
              "none",
              "A128GCM",
              "A192GCM",
              "A256GCM"
           ],
           "userinfo_encryption_enc_values_supported":[
              "none"
           ],
           "authorization_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/authorize",
           "request_object_encryption_enc_values_supported":[
              "none"
           ],
           "device_authorization_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/device_authorization",
           "userinfo_signing_alg_values_supported":[
              "none"
           ],
           "claims_supported":[
              "realmName",
              "preferred_username",
              "given_name",
              "uid",
              "upn",
              "groupIds",
              "employee_id",
              "name",
              "tenantId",
              "mobile_number",
              "department",
              "family_name",
              "job_title",
              "email",
              "iss"
           ],
           "claim_types_supported":[
              "normal"
           ],
           "token_endpoint_auth_methods_supported":[
              "client_secret_basic",
              "client_secret_post",
              "client_secret_jwt",
              "private_key_jwt"
           ],
           "response_modes_supported":[
              "query",
              "fragment",
              "form_post"
           ],
           "token_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/token",
           "response_types_supported":[
              "code",
              "none",
              "token",
              "id_token",
              "token id_token",
              "code id_token",
              "code token",
              "code token id_token"
           ],
           "user_authorization_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/user_authorization",
           "request_uri_parameter_supported":false,
           "userinfo_encryption_alg_values_supported":[
              "none"
           ],
           "grant_types_supported":[
              "authorization_code",
              "implicit",
              "client_credentials",
              "password",
              "refresh_token",
              "urn:ietf:params:oauth:grant-type:jwt-bearer",
              "urn:ietf:params:oauth:grant-type:device_code",
              "policyauth"
           ],
           "revocation_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/revoke",
           "userinfo_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/userinfo",
           "id_token_encryption_alg_values_supported":[
              "none",
              "RSA-OAEP",
              "RSA-OAEP-256"
           ],
           "jwks_uri":"https://sdk.verify.ibm.com/v1.0/endpoint/default/jwks",
           "subject_types_supported":[
              "public"
           ],
           "id_token_signing_alg_values_supported":[
              "none",
              "HS256",
              "HS384",
              "HS512",
              "RS256",
              "RS384",
              "RS512",
              "PS256",
              "PS384",
              "PS512",
              "ES256",
              "ES384",
              "ES512"
           ],
           "registration_endpoint":"https://sdk.verify.ibm.com/v1.0/endpoint/default/client_registration",
           "request_object_signing_alg_values_supported":[
              "none"
           ],
           "request_object_encryption_alg_values_supported":[
              "none"
           ]
        }
        """.trimIndent()

    private val responseAuthorizeOk = """
        {
           "access_token":"A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn",
           "grant_id":"3cd86861-9e2e-4741-b828-0910beb2f5c9",
           "token_type":"Bearer",
           "expires_in":7200
        }
        """.trimIndent()

    private val responseRefreshOk = """
        {
           "access_token":"A7y0nh0aaDpz8g0aTcVVBnr6veTocbYLpH7K8Jqn",
           "grant_id":"3cd86861-9e2e-4741-b828-0910beb2f5c9",
           "token_type":"Bearer",
           "expires_in":7200
        }
        """.trimIndent()
}