/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core

import kotlinx.serialization.decodeFromString
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
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * A singleton to configures the network object that handles the calls.
 *
 * @since 3.0.0
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object NetworkHelper {

    private val log = LoggerFactory.getLogger(javaClass)

    private var networkApi: NetworkApi = recreate().create(NetworkApi::class.java)
    private var networkApiCustomSSL: NetworkApi = recreateCustomSSL().create(NetworkApi::class.java)

    /**
     * Indicates whether redirects should be followed. Default is `true`.
     */
    var followSslRedirects = true
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    /**
     * Defines a customized interceptor for logging. Added as an
     * [Application Interceptor](https://square.github.io/okhttp/features/interceptors/)
     */
    var customLoggingInterceptor: HttpLoggingInterceptor? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    /**
     * Defines a customized interceptor. Added as an
     * [Application Interceptor](https://square.github.io/okhttp/features/interceptors/)
     */
    var customInterceptor: Interceptor? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    /**
     * Constrains which certificates are trusted.
     */
    var certificatePinner: CertificatePinner? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    /**
     * Defines the timeout in seconds to receive a response. Default is 30 seconds.
     */
    var readTimeOut = 30
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    /**
     * Defines the timeout in seconds to establish a connection. Default is 30 seconds.
     */
    var connectionTimeOut = 30
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    var sslContext: SSLContext? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    var hostnameVerifier: HostnameVerifier? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    var trustManager: X509TrustManager? = null
        set(value) {
            field = value
            networkApi = recreate().create(NetworkApi::class.java)
            networkApiCustomSSL = recreateCustomSSL().create(NetworkApi::class.java)
        }

    fun networkApi(useCustomSSL: Boolean = false): NetworkApi {
        return when (useCustomSSL) {
            true -> networkApi
            false -> networkApiCustomSSL
        }
    }


    private fun recreate(): Retrofit {

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.connectTimeout(connectionTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(readTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.followSslRedirects(followSslRedirects)

        certificatePinner?.let { okHttpClientBuilder.certificatePinner(it) }
        customLoggingInterceptor?.let { okHttpClientBuilder.addInterceptor(it) }
        customInterceptor?.let { okHttpClientBuilder.addInterceptor(it) }

        return Retrofit.Builder()
            .client(okHttpClientBuilder.build())
            .baseUrl("http://localhost")
            .build()
    }

    private fun recreateCustomSSL(): Retrofit {

        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.connectTimeout(connectionTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(readTimeOut.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.followSslRedirects(followSslRedirects)

        sslContext?.let { sslContext ->
            trustManager?.let { trustManager ->
                okHttpClientBuilder.sslSocketFactory(sslContext.socketFactory, trustManager)
            }
        }

        hostnameVerifier?.let { okHttpClientBuilder.hostnameVerifier(it) }
        certificatePinner?.let { okHttpClientBuilder.certificatePinner(it) }
        customLoggingInterceptor?.let { okHttpClientBuilder.addInterceptor(it) }
        customInterceptor?.let { okHttpClientBuilder.addInterceptor(it) }

        return Retrofit.Builder()
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
                val errorBody = response.errorBody()?.string() ?: ""
                Result.failure(AuthenticationException("IVMCR0001E", errorBody))
            }
        } catch (e: HttpException) {
            Result.failure(e)
        }
    }
}