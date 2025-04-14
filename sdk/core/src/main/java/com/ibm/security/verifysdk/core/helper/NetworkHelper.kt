/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.core.helper

import com.ibm.security.verifysdk.core.BuildConfig
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@OptIn(ExperimentalSerializationApi::class)
@Suppress("MemberVisibilityCanBePrivate")
object NetworkHelper {

    private lateinit var client: HttpClient

    var connectTimeoutMillis: Long = 15000L
    var requestTimeoutMillis: Long = 15000L
    var readTimeOutMillis: Long = 15000L
    var logLevel = LogLevel.ALL
    var logger = Logger.ANDROID
    var followRedirects = true
    var followSslRedirects = true
    var customInterceptor: Interceptor? = null
    var customLoggingInterceptor: HttpLoggingInterceptor? = null
    var certificatePinner: CertificatePinner? = null
    var sslContext: SSLContext? = null
    var hostnameVerifier: HostnameVerifier? = null
        set(value) {
            if (field != value) {
                field = value
                client = initializeClient(null)
            }
        }

    var trustManager: X509TrustManager? = null
        set(value) {
            if (field != value) {
                field = value
                client = initializeClient(null)
            }
        }

    var customDnsResolver: Dns? = null
        set(value) {
            if (field != value) {
                field = value
                client = initializeClient(null)
            }
        }

    init {
        initialize(null)
    }

    val getInstance: HttpClient
        get() {
            return client
        }

    @Synchronized
    fun initialize(customClient: HttpClient? = null, httpClientEngine: HttpClientEngine? = null) {
        client = customClient ?: initializeClient(httpClientEngine)
    }

    @Synchronized
    private fun initializeClient(httpClientEngine: HttpClientEngine?): HttpClient {

        if (!BuildConfig.DEBUG) {
            if (this@NetworkHelper.logLevel == LogLevel.ALL) {
                println("LogLevel.All should not be used in production!")
            }
        }

        return httpClientEngine?.let {
            HttpClient(it) {
                install(Logging) {
                    logger = this@NetworkHelper.logger
                    level = this@NetworkHelper.logLevel
                }
                install(ContentNegotiation) {
                    json(Json {
                        explicitNulls = false
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
                install(HttpTimeout) {
                    connectTimeoutMillis = this@NetworkHelper.connectTimeoutMillis
                    requestTimeoutMillis = this@NetworkHelper.requestTimeoutMillis
                }
            }
        } ?: HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(this@NetworkHelper.followRedirects)
                    followSslRedirects(this@NetworkHelper.followSslRedirects)

                    customInterceptor?.let { addInterceptor(it) }
                    customLoggingInterceptor?.let { addInterceptor(it) }
                }
                preconfigured = createOkHttpClient()
            }
            install(Logging) {
                logger = this@NetworkHelper.logger
                level = this@NetworkHelper.logLevel
            }
            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    encodeDefaults = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                connectTimeoutMillis = this@NetworkHelper.connectTimeoutMillis
                requestTimeoutMillis = this@NetworkHelper.requestTimeoutMillis
            }
        }

//        httpClientEngine?.let {
//            client = HttpClient(httpClientEngine) {
//                install(Logging) {
//                    logger = this@NetworkHelper.logger
//                    level = this@NetworkHelper.logLevel
//                }
//
//                install(ContentNegotiation) {
//                    json(Json {
//                        isLenient = true
//                        ignoreUnknownKeys = true
//                    })
//                }
//                install(HttpTimeout) {
//                    NetworkHelper.connectTimeoutMillis.also { connectTimeoutMillis = it }
//                    NetworkHelper.requestTimeoutMillis.also { requestTimeoutMillis = it }
//                }
//            }
//        } ?: run {
//            client = HttpClient(engineFactory = OkHttp) {
//                engine {
//                    config {
//                        followRedirects(this@NetworkHelper.followRedirects)
//                        followSslRedirects(this@NetworkHelper.followSslRedirects)
//
//                        this@NetworkHelper.customInterceptor?.let { customInterceptor ->
//                            addInterceptor(customInterceptor)
//                        }
//
//                        this@NetworkHelper.customLoggingInterceptor?.let { httpLoggingInterceptor ->
//                            addInterceptor(httpLoggingInterceptor)
//                        }
//
//                        preconfigured = createOkHttpClient()
//                    }
//                }
//
//                install(Logging) {
//                    logger = this@NetworkHelper.logger
//                    level = this@NetworkHelper.logLevel
//                }
//
//                install(ContentNegotiation) {
//                    json(Json {
//                        isLenient = true
//                        ignoreUnknownKeys = true
//                    })
//                }
//                install(HttpTimeout) {
//                    NetworkHelper.connectTimeoutMillis.also { connectTimeoutMillis = it }
//                    NetworkHelper.requestTimeoutMillis.also { requestTimeoutMillis = it }
//                }
//            }
//        }
    }


//
//    val okHttpClientBuilder = OkHttpClient.Builder()
//
//    okHttpClientBuilder.readTimeout(15000L, TimeUnit.MILLISECONDS)
//
//    if (customSslServer.isNullOrEmpty().not()) {
//        val trustManager = getCustomTrustManager()
//        val sslContext = SSLContext.getInstance("TLS").apply {
//            init(null, arrayOf(trustManager), null)
//        }
//        val hostnameVerifier = HostnameVerifier { hostname, _ ->
//            hostname == "localhost" || hostname == customSslServer
//        }
//
//        okHttpClientBuilder.sslSocketFactory(sslContext.socketFactory, trustManager)
//        okHttpClientBuilder.hostnameVerifier(hostnameVerifier)
//    }
//
//    return okHttpClientBuilder.build()

    internal fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(readTimeOutMillis, TimeUnit.MILLISECONDS)
            certificatePinner?.let { certificatePinner(it) }
            sslContext?.let { sslContext ->
                trustManager?.let {
                    sslSocketFactory(sslContext.socketFactory, it)
                }
            }
            hostnameVerifier?.let { hostnameVerifier(it) }
            customDnsResolver?.let { dns(it) }
        }.build()
    }

    fun insecureTrustManager(): X509TrustManager {

        if (!BuildConfig.DEBUG) {
            throw IllegalStateException("Insecure Trust Manager should not be used in production!")
        }

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

    fun closeClient() {
        if (this::client.isInitialized) {
            client.close()
        }
    }
}