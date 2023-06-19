package com.ibm.security.verifysdk.authentication.test

import android.os.Bundle
import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.authentication.shouldRefresh
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class TokenInfoTestOnPrem {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun constructor_happyPath_shouldReturnObject() {

        val oAuthToken = Json.decodeFromString<TokenInfo>(onpremTokenDefaultWithAdditionalData)
        assertTrue((System.currentTimeMillis() - oAuthToken.createdOn.toEpochMilliseconds()) < 1000) // token was created within the last second

        val additionalData = oAuthToken.additionalData
        assertEquals(true, additionalData["ISV_push_enabled"])
        assertTrue(additionalData.size == 2)    // grant_id
    }

    @Test
    fun decodeAndEncodeInstance_shouldBeEqual() {

        val tokenInfo = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        val tokenInfoSerialized = json.encodeToString(tokenInfo)
        val tokenInfoDeserialized = json.decodeFromString<TokenInfo>(tokenInfoSerialized)

        assertTrue("Encoding/decoding failed", tokenInfo == tokenInfoDeserialized)
    }

    @Test
    fun copyInstance_shouldBeEqual() {

        val tokenInfoOne = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        val tokenInfoTwo = tokenInfoOne.copy()
        assertTrue("Instances not equal", tokenInfoOne == tokenInfoTwo)
    }

    @Test
    fun toJsonWithHumanReadableTimeStamp_shouldReturnJson() {

        val tokenInfo = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        val tokenInfoJson = tokenInfo.toJson(true)

        assertTrue(tokenInfoJson.get("createdOn").toString().startsWith("202"))
        assertTrue(tokenInfoJson.get("expiresOn").toString().startsWith("202"))
        assertTrue(tokenInfoJson.get("createdOn").toString().length > 15)
        assertTrue(tokenInfoJson.get("expiresOn").toString().length > 15)
    }

    @Test
    fun tokenRefresh_useDefaultParameter_shouldReturnFalse() {
        val tokenInfo = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        assertFalse(tokenInfo.shouldRefresh())
    }

    @Test
    fun tokenRefresh_markAsExpired_shouldReturnTrue() {
        val tokenInfo = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        assertTrue(tokenInfo.shouldRefresh(-10))
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    @Test
    fun parcelizeInstance_shouldBeEqual() {

        val tokenInfo = json.decodeFromString<TokenInfo>(onpremTokenDefault)
        val bundle = Bundle()
        bundle.putParcelable("foo", tokenInfo)

        val parcel = Parcel.obtain()
        bundle.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        Thread.sleep(1000)

        val newBundle = Bundle.CREATOR.createFromParcel(parcel)
        newBundle.classLoader = TokenInfo.Companion::class.java.classLoader
        val tokenDeparcelized = newBundle.getParcelable<TokenInfo>("foo")

        assertTrue(tokenInfo == tokenDeparcelized)
    }

    private val onpremTokenDefault =
        """
       {
          "token_type" : "Bearer",
          "scope" : "name age",
          "refreshToken" : "h5j6i7k8",
          "grant_id" : "b49cf0c8add0",
          "accessToken" : "a1b2c3d4",
          "expires_in" : 7200
        } 
    """.trimIndent()

    private val onpremTokenDefaultWithAdditionalData =
        """
       {
          "token_type" : "Bearer",
          "scope" : "name age",
          "refreshToken" : "h5j6i7k8",
          "grant_id" : "b49cf0c8add0",
          "accessToken" : "a1b2c3d4",
          "expires_in" : 7200,
          "ISV_push_enabled": true
        } 
    """.trimIndent()
}