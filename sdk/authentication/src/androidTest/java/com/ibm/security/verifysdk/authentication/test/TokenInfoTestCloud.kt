package com.ibm.security.verifysdk.authentication.test

import android.os.Bundle
import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ibm.security.verifysdk.authentication.TokenInfo
import com.ibm.security.verifysdk.authentication.shouldRefresh
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class TokenInfoTestCloud {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun constructor_withHashmapOnly_shouldReturnObject() {
        val tokenInfo = TokenInfo(additionalData = HashMap())
        assertEquals("", tokenInfo.accessToken)
    }

    @Test
    fun constructor_withData_happyPath_shouldReturnObject() {
        val tokenInfo = TokenInfo(
            "accessToken", "refreshToken",
            Clock.System.now(), 60, Clock.System.now(), "scope", "type",
            HashMap()
        )

        assertEquals("accessToken", tokenInfo.accessToken)
        assertEquals(60, tokenInfo.expiresIn)
    }

    @Test
    fun constructor_generated_shouldReturnObject() {

        for (c in TokenInfo::class.java.constructors) {
            if (c.isSynthetic && c.parameterCount == 10) {
                val tokenInfo = c.newInstance(
                    "accessToken", "refreshToken",
                    Clock.System.now(), 60, Clock.System.now(), "scope", "type",
                    HashMap<String, Any>(), 42, null
                ) as TokenInfo

                assertEquals("accessToken", tokenInfo.accessToken)
            }
        }
    }

    @Test
    fun constructor_withSerializer_happyPath_shouldReturnObject() {

        val oAuthToken = Json.decodeFromString<TokenInfo>(cloudTokenDefault)
        assertTrue((System.currentTimeMillis() - oAuthToken.createdOn.toEpochMilliseconds()) < 1000) // token was created within the last second

        val additionalData = oAuthToken.additionalData
        assertTrue(additionalData.size == 1)  // grant_id
    }

    @Test
    fun decodeAndEncodeInstance_shouldBeEqual() {

        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val tokenInfoSerialized = json.encodeToString(tokenInfo)
        val tokenInfoDeserialized = json.decodeFromString<TokenInfo>(tokenInfoSerialized)

        assertTrue("Encoding/decoding failed", tokenInfo == tokenInfoDeserialized)
    }

    @Test
    fun copyInstance_shouldBeEqual() {

        val tokenInfoOne = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val tokenInfoTwo = tokenInfoOne.copy()
        assertTrue("Instances not equal", tokenInfoOne == tokenInfoTwo)
    }

    @Test
    fun toJson_withHumanReadableTimeStamp_isTrue_shouldReturnJson() {

        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val tokenInfoJson = tokenInfo.toJson(true)

        assertTrue(tokenInfoJson.get("createdOn").toString().startsWith("202"))
        assertTrue(tokenInfoJson.get("expiresOn").toString().startsWith("202"))
        assertTrue(tokenInfoJson.get("createdOn").toString().length > 15)
        assertTrue(tokenInfoJson.get("expiresOn").toString().length > 15)
    }

    @Test
    fun toJson_withHumanReadableTimeStamp_isFalse_shouldReturnJson() {

        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val tokenInfoJson = tokenInfo.toJson(false)

        assertTrue(tokenInfoJson.get("createdOn").toString().startsWith("166"))
        assertTrue(tokenInfoJson.get("expiresOn").toString().startsWith("166"))
        assertTrue(tokenInfoJson.get("createdOn").toString().length == 10)
        assertTrue(tokenInfoJson.get("expiresOn").toString().length == 10)
    }

    @Test
    fun toJson_shouldReturnJson() {

        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val tokenInfoJson = tokenInfo.toJson()

        assertTrue(tokenInfoJson.get("createdOn").toString().startsWith("166"))
        assertTrue(tokenInfoJson.get("expiresOn").toString().startsWith("166"))
        assertTrue(tokenInfoJson.get("createdOn").toString().length == 10)
        assertTrue(tokenInfoJson.get("expiresOn").toString().length == 10)
    }

    @Test
    fun tokenRefresh_useDefaultParameter_shouldReturnFalse() {
        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        assertFalse(tokenInfo.shouldRefresh())
    }

    @Test
    fun tokenRefresh_markAsExpired_shouldReturnTrue() {
        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        assertTrue(tokenInfo.shouldRefresh(-10))
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    @Test
    fun parcelizeInstance_shouldBeEqual() {

        val tokenInfo = json.decodeFromString<TokenInfo>(cloudTokenDefault)
        val bundle = Bundle()
        bundle.putParcelable(TokenInfo.Companion::class.java.name, tokenInfo)

        val parcel = Parcel.obtain()
        bundle.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        Thread.sleep(1000)

        val newBundle = Bundle.CREATOR.createFromParcel(parcel)
        newBundle.classLoader = TokenInfo.Companion::class.java.classLoader
        val tokenDeparcelized =
            newBundle.getParcelable<TokenInfo>(TokenInfo.Companion::class.java.name)

        assertTrue(tokenInfo == tokenDeparcelized)
    }


    @Test
    fun constructor_noAccessTokenValue_shouldUseDefault() {
        val oAuthToken = Json.decodeFromString<TokenInfo>(cloudTokenNoAccessToken)
        assertEquals("", oAuthToken.accessToken)
        assertTrue((System.currentTimeMillis() - oAuthToken.createdOn.toEpochMilliseconds()) < 1000) // token was created within the last second
    }

    private val cloudTokenDefault = """
           {
              "token_type" : "Bearer",
              "scope" : "name age",
              "refreshToken" : "h5j6i7k8",
              "grant_id" : "b49cf0c8add0",
              "accessToken" : "a1b2c3d4",
              "expires_in" : 7200
            } 
        """.trimIndent()

    private val cloudTokenNoAccessToken = """
           {
              "token_type" : "Bearer",
              "scope" : "name age",
              "refreshToken" : "h5j6i7k8",
              "grant_id" : "b49cf0c8add0",
              "expires_in" : 7200
            } 
        """.trimIndent()
}