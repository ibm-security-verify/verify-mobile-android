package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToAgentTest {

    @Test
    fun initialize() {
        val toAgent = ToAgent(
            name = "name",
            id = "id",
            url = "http://agent.url"
        )

        assertEquals("name", toAgent.name)
        assertEquals("id", toAgent.id)
        assertEquals("http://agent.url", toAgent.url)
    }

    fun initialize_withEmptyStrings() {
        val toAgent = ToAgent(
            name = "",
            id = "",
            url = ""
        )

        assertEquals("", toAgent.name)
        assertEquals("", toAgent.id)
        assertEquals("", toAgent.url)
    }

    @Test
    fun initialize_withSpecialCharacters() {
        val toAgent = ToAgent(
            name = "Agent @#%",
            id = "ID-123_!",
            url = "https://agent.example.com/path?query=value"
        )

        assertEquals("Agent @#%", toAgent.name)
        assertEquals("ID-123_!", toAgent.id)
        assertEquals("https://agent.example.com/path?query=value", toAgent.url)
    }

    @Test
    fun initialize_withLongStrings() {
        val longName = "A".repeat(1000)
        val longId = "B".repeat(1000)
        val longUrl = "http://".plus("C".repeat(995))

        val toAgent = ToAgent(
            name = longName,
            id = longId,
            url = longUrl
        )

        assertEquals(longName, toAgent.name)
        assertEquals(longId, toAgent.id)
        assertEquals(longUrl, toAgent.url)
    }

    fun initialize_withInvalidUrl() {
        val toAgent = ToAgent(
            name = "name",
            id = "id",
            url = "invalid-url"
        )

        // The url is stored as-is, no URL validation is expected here.
        assertEquals("invalid-url", toAgent.url)
    }
}