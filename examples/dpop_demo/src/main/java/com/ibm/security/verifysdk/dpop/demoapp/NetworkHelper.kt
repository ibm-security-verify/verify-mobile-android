/*
 *  Copyright contributors to the IBM Verify DPoP Sample App for Android project
 */
package com.ibm.security.verifysdk.dpop.demoapp


import com.ibm.security.verifysdk.core.helper.ContextHelper
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class NetworkHelper(private val customSslServer: String? = null) {

    var client = HttpClient(engineFactory = OkHttp) {
        engine {
            config {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

                preconfigured = createOkHttpClient()
            }
        }

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
            connectTimeoutMillis = 15000L
            requestTimeoutMillis = 15000L
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.readTimeout(15000L, TimeUnit.MILLISECONDS)

        if (customSslServer.isNullOrEmpty().not()) {
            val trustManager = getCustomTrustManager()
            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(trustManager), null)
            }
            val hostnameVerifier = HostnameVerifier { hostname, _ ->
                hostname == "localhost" || hostname == customSslServer
            }

            okHttpClientBuilder.sslSocketFactory(sslContext.socketFactory, trustManager)
            okHttpClientBuilder.hostnameVerifier(hostnameVerifier)
        }

        return okHttpClientBuilder.build()
    }

    private fun getCustomTrustManager(): X509TrustManager {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate =
            ContextHelper.context.resources.openRawResource(R.raw.cert).use { inputStream ->
                certificateFactory.generateCertificate(inputStream) as X509Certificate
            }

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("custom", certificate)
        }

        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

        return trustManagerFactory.trustManagers[0] as X509TrustManager
    }
}