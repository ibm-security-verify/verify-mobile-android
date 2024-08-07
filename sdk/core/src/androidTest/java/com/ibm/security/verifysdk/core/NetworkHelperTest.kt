package com.ibm.security.verifysdk.core

import com.ibm.security.verifysdk.core.helper.NetworkHelper
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@Suppress("KotlinConstantConditions")
internal class NetworkHelperTest {


    @Test
    fun test_followSslRedirects() {
        NetworkHelper.followSslRedirects = true
        assertEquals(true, NetworkHelper.followSslRedirects)
        NetworkHelper.followSslRedirects = false
        assertEquals(false, NetworkHelper.followSslRedirects)
    }

    @Test
    fun test_customLoggingInterceptor() {

        NetworkHelper.customLoggingInterceptor = null
        assertEquals(null, NetworkHelper.customLoggingInterceptor)

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        NetworkHelper.customLoggingInterceptor = loggingInterceptor

        assertEquals(loggingInterceptor, NetworkHelper.customLoggingInterceptor)
    }

    @Test
    fun test_customInterceptor() {

        val interceptor =
            Interceptor { chain: Interceptor.Chain ->
                val originalRequest = chain.request()
                val requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", "com.ibm.security.verifysdk")
                    .build()
                chain.proceed(requestWithUserAgent)
            }

        NetworkHelper.customInterceptor = null
        assertEquals(null, NetworkHelper.customInterceptor)
        NetworkHelper.customInterceptor = interceptor
        assertEquals(interceptor, NetworkHelper.customInterceptor)
    }

    @Test
    fun test_certificatePinner() {

        val certificatePinner = CertificatePinner.Builder().build()

        NetworkHelper.certificatePinner = null
        assertEquals(null, NetworkHelper.certificatePinner)
        NetworkHelper.certificatePinner = certificatePinner
        assertEquals(certificatePinner, NetworkHelper.certificatePinner)
    }

    @Test
    fun test_readTimeOut() {
        NetworkHelper.readTimeOutMillis = 42
        assertEquals(42, NetworkHelper.readTimeOutMillis)
        NetworkHelper.readTimeOutMillis = 24
        assertEquals(24, NetworkHelper.readTimeOutMillis)
    }

    @Test
    fun test_connectionTimeOut() {
        NetworkHelper.connectTimeoutMillis = 42
        assertEquals(42, NetworkHelper.connectTimeoutMillis)
        NetworkHelper.connectTimeoutMillis = 24
        assertEquals(24, NetworkHelper.connectTimeoutMillis)
    }

    @Test
    fun getNetworkApi() {
        NetworkHelper.getInstance.hashCode()
    }
}