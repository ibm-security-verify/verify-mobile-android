package com.ibm.security.verifysdk.dc.model

import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
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

    @Test
    fun serialize_to_Json() {

        val instant = Instant.now().toEpochMilli()
        val timeStamp = TimeStamp(
            created = timestamp1,
            stated = instant,
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
    fun deserialize_from_Json() {
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
    fun deserialize_withRequiredAttributes() {
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
    fun deserialize_withNullValues() {
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