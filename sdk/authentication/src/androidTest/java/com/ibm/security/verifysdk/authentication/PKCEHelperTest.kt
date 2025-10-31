/*
 * Copyright contributors to the IBM Verify SDK for Android project
 */

package com.ibm.security.verifysdk.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
internal class PKCEHelperTest {

    @Test
    fun generateCodeVerifier_happyPath() {
        val codeVerifier = PKCEHelper.generateCodeVerifier()
        assert(codeVerifier.isEmpty().not())
    }

    @Test
    fun generateCodeChallenge_happyPath() {
        val codeVerifier = PKCEHelper.generateCodeVerifier()
        assert(PKCEHelper.generateCodeChallenge(codeVerifier).isEmpty().not())
    }
}