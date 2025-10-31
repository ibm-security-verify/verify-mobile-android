/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HOTPFactorInfoTest {

    private val secret = "ON6MJUIM4MXYVLN3"

    @Test
    fun generatePasscode_happyPath_shouldGeneratePasscode() {

        val values = listOf("488394","643724","156322","759595","629686","015146","844528","971941","855801","409719","233609")
        val factor =
            HOTPFactorInfo(secret = secret)

        for (i in 0 until 10) {
            val result = factor.generatePasscode(i.toLong())
            assertEquals(6, result.length)
            assertEquals(values[i], result)
        }
    }

    @Test
    fun generatePasscode_withInternalCounter_shouldGeneratePasscode() {

        val values = listOf("488394","643724","156322","759595","629686","015146","844528","971941","855801","409719","233609")
        val factor =
            HOTPFactorInfo(secret = secret)

        for (i in 0 until 10) {
            val result = factor.generatePasscode()
            assertEquals(6, result.length)
            assertEquals(values[i], result)
        }
    }

    @Test
    fun generatePasscode_withDigits3_shouldGeneratePasscodes() {

        val values = listOf("394","724","322","595","686","146","528","941","801","719","609")
        val factor =
            HOTPFactorInfo(secret = secret, digits = 3)

        for (i in 0 until 10) {
            val result = factor.generatePasscode(i.toLong())
            assertEquals(3, result.length)
            assertEquals(values[i], result)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun constructor_withDigits0_shouldThrowException() {
        val factor =
            HOTPFactorInfo(secret = secret, digits = 0)
    }

    @Test
    fun constructor_withDigits6_shouldBeAccepted() {
        val factor =
            HOTPFactorInfo(secret = secret, digits = 6)
        assertEquals(6, factor.digits)
    }

    @Test(expected = IllegalArgumentException::class)
    fun constructor_withDigits7_shouldThrowException() {
        val factor =
            HOTPFactorInfo(secret = secret, digits = 7)
    }

    @Test
    fun constructor_withDigits8_shouldBeAccepted() {
        val factor =
            HOTPFactorInfo(secret = secret, digits = 8)
        assertEquals(8, factor.digits)
    }

    @Test(expected = IllegalArgumentException::class)
    fun constructor_withDigits9_shouldBeAccepted() {
        val factor =
            HOTPFactorInfo(secret = secret, digits = 9)
    }
}