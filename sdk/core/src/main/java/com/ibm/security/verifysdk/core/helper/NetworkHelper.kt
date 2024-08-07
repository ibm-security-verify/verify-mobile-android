/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.helper

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Suppress("MemberVisibilityCanBePrivate")
object NetworkHelper {

    private lateinit var client: HttpClient

    var connectTimeoutMillis: Long = 30000L
    var requestTimeoutMillis: Long = 30000L
    var readTimeOutMillis: Long = 30000L
    var logLevel = LogLevel.ALL
    var logger = Logger.ANDROID
    var followRedirects = true
    var followSslRedirects = true
    var customInterceptor: Interceptor? = null
    var customLoggingInterceptor: HttpLoggingInterceptor? = null
    var certificatePinner: CertificatePinner? = null
    var sslContext: SSLContext? = null
    var hostnameVerifier: HostnameVerifier? = null
    var trustManager: X509TrustManager? = null

    init {
        initialize()
    }

    val getInstance: HttpClient
        get() {
            return client
        }

    fun initialize(httpClientEngine: HttpClientEngine? = null) {

        httpClientEngine?.let {
            client = HttpClient(httpClientEngine) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.ALL
                }

                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(HttpTimeout) {
                    NetworkHelper.connectTimeoutMillis.also { connectTimeoutMillis = it }
                    NetworkHelper.requestTimeoutMillis.also { requestTimeoutMillis = it }
                }
            }
        } ?: run {
            client = HttpClient(engineFactory = OkHttp) {
                engine {
                    config {
                        followRedirects(NetworkHelper.followRedirects)
                        followSslRedirects(followSslRedirects)
                    }
                    customInterceptor?.let { customInterceptor ->
                        addInterceptor(customInterceptor)
                    }

                    customLoggingInterceptor?.let { httpLoggingInterceptor ->
                        addInterceptor(httpLoggingInterceptor)
                    }

                    preconfigured = createOkHttpClient()
                }

                install(Logging) {
                    logger = NetworkHelper.logger
                    level = logLevel
                }

                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(HttpTimeout) {
                    NetworkHelper.connectTimeoutMillis.also { connectTimeoutMillis = it }
                    NetworkHelper.requestTimeoutMillis.also { requestTimeoutMillis = it }
                }
            }
        }
    }

    internal fun createOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        readTimeOutMillis.let { readTimeOutMillis ->
            okHttpClientBuilder.readTimeout(readTimeOutMillis, TimeUnit.MILLISECONDS)
        }

        certificatePinner?.let { certificatePinner ->
            okHttpClientBuilder.certificatePinner(certificatePinner)
        }

        sslContext?.let { sslContext ->
            trustManager?.let { trustManager ->
                okHttpClientBuilder.sslSocketFactory(sslContext.socketFactory, trustManager)
            }
        }

        hostnameVerifier?.let { hostnameVerifier ->
            okHttpClientBuilder.hostnameVerifier(hostnameVerifier)
        }

        return okHttpClientBuilder.build()
    }

    fun insecureTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out java.security.cert.X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out java.security.cert.X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        }
    }
}