/*
 * Copyright contributors to the IBM Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.adaptive.AdaptiveContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * @since 3.0.0
 */
@RunWith(AndroidJUnit4::class)
class SessionIdTests {

    /**
     * Create a session ID object from string, and compare with original session ID.
     */
    @Test
    fun testCreateSessionIdAsUUID() {
        // Get session ID.
        val sessionId1 = AdaptiveContext.sessionId

        // Create UUID object from session ID.
        val sessionId2 = UUID.fromString(sessionId1)

        // Ensure UUID object was successfully created from session ID string.
        assertTrue("Could not create UUID object from $sessionId1", sessionId2 != null)

        // Ensure string representation of UUID object matches initial session ID.
        assertEquals(sessionId1, sessionId2.toString())
    }

    /**
     * Ensure multiple calls to [AdaptiveContext.sessionId] return the same session ID.
     */
    @Test
    fun testCompareSessionId() {
        // Get session ID.
        val sessionId1 = AdaptiveContext.sessionId

        // Get session ID.
        val sessionId2 = AdaptiveContext.sessionId

        assertEquals(sessionId1, sessionId2)
    }

    /**
     * Ensure session IDs are not the same after resetting the session.
     */
    @Test
    fun testResetSessionId() {
        // Get session ID.
        val sessionId1 = AdaptiveContext.sessionId

        // Renew session ID immediately.
        AdaptiveContext.renewSessionIdInterval = 0

        // Reset session.
        AdaptiveContext.resetSession()

        // Get session ID.
        val sessionId2 = AdaptiveContext.sessionId

        assertNotEquals(sessionId1, sessionId2)
    }
}
