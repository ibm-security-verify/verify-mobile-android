//
// Copyright contributors to the IBM Security Verify Authentication SDK for Android project
//
package com.ibm.security.verifysdk.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ibm.security.verifysdk.core.AuthenticationException
import com.ibm.security.verifysdk.core.AuthorizationException
import com.ibm.security.verifysdk.core.ErrorMessage
import com.ibm.security.verifysdk.core.ErrorResponse
import com.ibm.security.verifysdk.core.NetworkHelper
import com.ibm.security.verifysdk.core.NetworkHelper.trustManager
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume


/**
 * The OAuthProvider enables third-party applications to obtain limited access to an HTTP service,
 * either on behalf of a resource owner by orchestrating an approval interaction between the
 * resource owner and the HTTP service, or by allowing the third-party application to obtain access
 * on its own behalf.
 *
 * To use this library, the following attributes must be overridden in the `build.gradle` file
 * to specify the custom scheme that will be used for the OAuth2 redirect
 * {@see https://developer.android.com/guide/topics/manifest/data-element}:
 * - auth_redirect_scheme
 * - auth_redirect_host
 * - auth_redirect_path
 *
 * @since 3.0.0
 */
@OptIn(ExperimentalSerializationApi::class)
@Suppress("unused")
class OAuthProvider(val clientId: String, val clientSecret: String?) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val decoder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    var ignoreSsl: Boolean = false
        set(value) {
            field = value
            if (value) {
                trustManager = NetworkHelper.insecureTrustManager()
            }
        }

    var additionalHeaders: Map<String, String> = HashMap()
    var additionalParameters: Map<String, String> = HashMap()

    constructor(
        clientId: String,
        clientSecret: String? = null,
        additionalHeaders: Map<String, String>? = null,
        additionalParameters: Map<String, String>?,
    ) : this(clientId, clientSecret) {
        additionalHeaders?.let {
            this.additionalHeaders = it
        }

        additionalParameters?.let {
            this.additionalParameters = it
        }
    }

    /**
     * Discover the authorization service configuration from a compliant OpenID Connect endpoint.
     *
     * @param   url  The `URL` for the OpenID Connect service provider issuer.
     *
     * @return
     *
     */
    suspend fun discover(url: URL): Result<OIDCMetadataInfo> {

        return try {
            if (url.path.endsWith(".well-known/openid-configuration", ignoreCase = true).not()) {
                Result.failure(MalformedURLException("The URL does not end with the .well-known/openid-configuration path component."))
            } else {
                val response = NetworkHelper.getInstance.get {
                    url(url.toString())
                }.body<OIDCMetadataInfo>()
                Result.success(response)
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * Launches Chrome Custom Tabs to initiate the authorization code (AZN) flow using optional
     * Proof Key for Code Exchange (PKCE).
     *
     * @param   url   The `URL` to the authorize endpoint for the OpenID Connect service provider
     *                  issuer.
     * @param   redirectUrl  The redirect `URL` that is registered with the OpenID Connect service provider.
     * @param   codeChallenge   A challenge derived from a code verifier for support PKCE operations.
     * @param   method  The hash method used to derive code challenge.
     * @param   scope  The scope of the access request. Default is **openid**.
     * @param   state  An opaque value used by the client to maintain state between the request and
     *                  callback.  The authorization server includes this value when redirecting
     *                  back to the client.
     * @poram   activity  The activity invoking this method.
     *
     * @return  [Result] with the `code` to be used in the subsequent authorization request or
     *                  with `Throwable` in case of an error.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun authorizeWithBrowser(
        url: URL,
        redirectUrl: String,
        codeChallenge: String?,
        method: CodeChallengeMethod = CodeChallengeMethod.PLAIN,
        scope: Array<String>?,
        state: String?,
        activity: ComponentActivity
    ): Result<String> {

        return try {
            val uriBuilder = Uri.Builder()
            var myScope = scope ?: arrayOf("openid")

            uriBuilder.scheme((url.protocol))
                .encodedAuthority(url.authority)
                .appendEncodedPath(url.path)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUrl)

            codeChallenge?.let {
                uriBuilder.appendQueryParameter("code_challenge", codeChallenge)
                uriBuilder.appendQueryParameter("code_challenge_method", method.name)
            }

            if (myScope.contains("openid").not()) {
                myScope = myScope.plus("openid")
            }
            uriBuilder.appendQueryParameter("scope", myScope.joinToString(" "))

            state?.let { uriBuilder.appendQueryParameter("state", it) }
            additionalParameters.forEach {
                uriBuilder.appendQueryParameter(it.key, it.value)
            }

            val intent = Intent(activity, AuthenticationActivity::class.java)
            intent.putExtra("url", uriBuilder.build().toString())

            suspendCancellableCoroutine { continuation ->
                val getCode = activity.activityResultRegistry.register(
                    "code",
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.getStringExtra("code")?.let {
                            continuation.resume(Result.success(it), null)
                        } ?: run {
                            continuation.resume(
                                Result.failure(
                                    AuthorizationException(
                                        HttpStatusCode.OK,
                                        "ISVSDKOP01",
                                        "Authorization code not found"
                                    )
                                )
                            )
                        }
                    } else {
                        continuation.resume(
                            Result.failure(
                                AuthenticationException(
                                    HttpStatusCode.OK,
                                    "ISVSDKOP02",
                                    "Authentication canceled"
                                )
                            )
                        )
                    }
                }
                getCode.launch(intent)

                continuation.invokeOnCancellation {
                    getCode.unregister()
                }
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * The authorization code is obtained by using an authorization server as an intermediary
     * between the client and resource owner.
     *
     * @param   url  The `URL` for the OpenID Connect service provider issuer.
     * @param   redirectUrl  The redirect `URL` that is registered with the OpenID Connect service
     *                      provider. This parameter is required when the code was obtained through
     *                      `authorizeWithBrowser`.
     * @param   authorizationCode  The authorization code received from the authorization server.
     * @param   codeVerifier  The PKCE code verifier used to redeem the authorization code.
     * @param   scope  The scope of the access request.
     *
     * @return
     */
    suspend fun authorize(
        url: URL,
        redirectUrl: URL? = null,
        authorizationCode: String,
        codeVerifier: String? = null,
        scope: Array<String>?
    ): Result<TokenInfo> {

        return try {
            val formData = listOf(
                "client_id" to clientId,
                "client_secret" to (clientSecret ?: ""),
                "code" to authorizationCode,
                "code_verifier" to (codeVerifier ?: ""),
                "grant_type" to "authorization_code",
                "scope" to (scope?.joinToString(" ") ?: ""),
                "redirect_uri" to (redirectUrl?.toString() ?: "")
            )

            val response = NetworkHelper.getInstance.post {
                url(url.toString())
                headers {
                    additionalHeaders.forEach {
                        this@headers.append(it.key, it.value)
                    }
                }
                contentType(ContentType.Application.FormUrlEncoded)
                accept(ContentType.Application.Json)
                setBody((formData + additionalParameters.toList()).formUrlEncode())
            }

            if (response.status.isSuccess()) {
                Result.success(decoder.decodeFromString<TokenInfo>(response.bodyAsText()))
            } else {
                val errorResponse = response.body<ErrorResponse>()
                Result.failure(AuthorizationException(response.status, errorResponse.error, errorResponse.errorDescription))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * The resource owner password credentials (i.e., username and password) can be used directly
     * as an authorization grant to obtain an access token.
     *
     * @param   url  The `URL` for the OpenID Connect service provider issuer.
     * @param   username  The resource owner username.
     * @param   password  The resource owner password.
     * @param   scope  The scope of the access request.
     *
     * @return
     */
    suspend fun authorize(
        url: URL,
        username: String,
        password: String,
        scope: Array<String>? = arrayOf("")
    ): Result<TokenInfo> {

        return try {
            val formData = mapOf(
                "client_id" to clientId,
                "client_secret" to (clientSecret ?: ""),
                "username" to username,
                "password" to password,
                "grant_type" to "password",
                "scope" to (scope?.joinToString(" ") ?: ""),
            )

            val response = NetworkHelper.getInstance.post {
                url(url.toString())
                headers {
                    additionalHeaders.forEach {
                        this@headers.append(it.key, it.value)
                    }
                }
                contentType(ContentType.Application.FormUrlEncoded)
                accept(ContentType.Application.Json)
                setBody(formData + additionalParameters)
            }

            if (response.status.isSuccess()) {
                Result.success(decoder.decodeFromString<TokenInfo>(response.bodyAsText()))
            } else {
                val errorResponse = response.body<ErrorResponse>()
                Result.failure(AuthorizationException(response.status, errorResponse.error, errorResponse.errorDescription))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    /**
     * Refresh tokens are issued to the client by the authorization server and are used to obtain a
     * new access token when the current access token becomes invalid or expires, or to obtain
     * additional access tokens with identical or narrower scope.
     *
     * Because refresh tokens are typically long-lasting credentials used to request additional
     * access tokens, the refresh token is bound to the client to which it was issued.
     *
     * @param   url  The `URL` for the OpenID Connect service provider issuer.
     * @param   refreshToken  The refresh token previously issued by the authorization server.
     * @param   scope  The scope of the access request.  The requested scope must not include any
     *                  scope not originally granted by the resource owner, and if omitted is
     *                  treated as equal to the scope originally granted by the resource owner.
     *
     * @return
     */
    suspend fun refresh(
        url: URL,
        refreshToken: String,
        scope: Array<String>? = arrayOf("")
    ): Result<TokenInfo> {

        return try {
            val formData = mapOf(
                "refresh_token" to refreshToken,
                "grant_type" to "refresh_token",
                "scope" to (scope?.joinToString(" ") ?: ""),
            )

            val response = NetworkHelper.getInstance.post {
                url(url.toString())
                headers {
                    additionalHeaders.forEach {
                        this@headers.append(it.key, it.value)
                    }
                }
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(formData + additionalParameters)
            }

            if (response.status.isSuccess()) {
                Result.success(decoder.decodeFromString<TokenInfo>(response.bodyAsText()))
            } else {
                val errorResponse = response.body<ErrorResponse>()
                Result.failure(AuthorizationException(response.status, errorResponse.error, errorResponse.errorDescription))
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}