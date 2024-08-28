package com.ibm.security.verifysdk.dc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.Instant

@OptIn(ExperimentalSerializationApi::class)
@RunWith(JUnit4::class)
class TimeStampTest {

    private val timestamp1 = 1234567800L
    private val timestamp2 = 1234567890L

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = false
        isLenient = true
    }

    @Test
    fun serialize_to_JSON() {

        val instant = Instant.now().toEpochMilli()
        val timeStamp = TimeStamp(
            created = timestamp1,
            stated =  instant,
            updated = timestamp2,
            deleted = instant
        )

        val jsonString = json.encodeToString(timeStamp)
        val expectedJson = """
                {
                    "created": $timestamp1,
                    "stated": $instant,
                    "updated": $timestamp2,
                    "deleted": $instant
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val instant = Instant.now().toEpochMilli()
        val jsonString = """
                {
                    "created": $timestamp1,
                    "stated": $instant,
                    "updated": $timestamp2,
                    "deleted": null
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val timeStamp = json.decodeFromString<TimeStamp>(jsonString)
        assertEquals(timestamp1, timeStamp.created)
        assertEquals(instant, timeStamp.stated)
        assertEquals(timestamp2, timeStamp.updated)
        assertNull(timeStamp.deleted)
    }

    @Test
    fun deserialize_with_required_fields() {
        val instant = Instant.now().toEpochMilli()
        val jsonString = """
                {
                    "created": $timestamp1
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val timeStamp = json.decodeFromString<TimeStamp>(jsonString)
        assertEquals(timestamp1, timeStamp.created)
        assertNull(timeStamp.stated)
        assertNull(timeStamp.updated)
        assertNull(timeStamp.deleted)
    }

    @Test
    fun deserialize_with_null_values() {
        val jsonString = """
                {
                    "created": $timestamp1,
                    "stated": null,
                    "updated": null,
                    "deleted": null
                }
            """.trimIndent().replace("\n", "").replace(" ", "")

        val timeStamp = json.decodeFromString<TimeStamp>(jsonString)
        assertEquals(timestamp1, timeStamp.created)
        assertNull(timeStamp.stated)
        assertNull(timeStamp.updated)
        assertNull(timeStamp.deleted)
    }
}