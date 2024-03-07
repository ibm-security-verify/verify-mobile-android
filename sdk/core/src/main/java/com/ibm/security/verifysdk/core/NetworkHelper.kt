/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * A singleton to configures the network object that handles the calls.
 *
 * @since 3.0.0
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object NetworkHelper {

    private val log = LoggerFactory.getLogger(javaClass)

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost")
        .build()

    /**
     * Indicates whether redirects should be followed. Default is `true`.
     */
    var followSslRedirects = true
        set(value) {
            field = value
            recreate()
        }

    /**
     * Defines a customized interceptor for logging. Added as an
     * [Application Interceptor](https://square.github.io/okhttp/features/interceptors/)
     */
    var customLoggingInterceptor: HttpLoggingInterceptor? = null
        set(value) {
            field = value
            recreate()
        }

    /**
     * Defines a customized interceptor. Added as an
     * [Application Interceptor](https://square.github.io/okhttp/features/interceptors/)
     */
    var customInterceptor: Interceptor? = null
        set(value) {
            field = value
            recreate()
        }

    /**
     * Constrains which certificates are trusted.
     */
    var certificatePinner: CertificatePinner? = null
        set(value) {
            field = value
            recreate()
        }

    /**
     * Defines the timeout in seconds to receive a response. Default is 30 seconds.
     */
    var readTimeOut = 30
        set(value) {
            field = value
            recreate()
        }

    /**
     * Defines the timeout in seconds to establish a connection. Default is 30 seconds.
     */
    var connectionTimeOut = 30
        set(value) {
            field = value
            recreate()
        }

    val networkApi: NetworkApi by lazy {
        retrofit.create(NetworkApi::class.java)
    }

    private fun recreate() {

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.connectTimeout(connectionTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(readTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.followSslRedirects(followSslRedirects)

        certificatePinner?.let {
            okHttpClientBuilder.certificatePinner(it)
        }

        customLoggingInterceptor?.let {
            okHttpClientBuilder.addInterceptor(it)
        }

        customInterceptor?.let {
            okHttpClientBuilder.addInterceptor(it)
        }

        retrofit = Retrofit.Builder()
            .client(okHttpClientBuilder.build())
            .baseUrl("http://localhost")
            .build()
    }

    /**
     * Wrap the network response in a result object of a given type.
     */
    inline fun <reified T : Any> handleApi(response: Response<ResponseBody>): Result<T> {

        return try {
            val body = response.body()
            val json = Json { ignoreUnknownKeys = true }
            if (response.isSuccessful && body != null) {
                Result.success(json.decodeFromString(body.string()))
            } else {
                val errorBody = json.encodeToString(
                    response.errorBody()?.string()
                        ?: "{\"errorDescription\":\"No error description found in response.\"}"
                )
                Result.failure(AuthenticationException("IVMCR0001E", errorBody))
            }
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }
}