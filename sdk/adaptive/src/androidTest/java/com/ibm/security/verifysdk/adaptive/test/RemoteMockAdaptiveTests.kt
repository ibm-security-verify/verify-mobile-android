/*
 * Copyright contributors to the IBM Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ibm.security.verifysdk.adaptive.AdaptiveContext
import com.ibm.security.verifysdk.adaptive.Factor
import com.ibm.security.verifysdk.adaptive.PasswordEvaluation
import com.ibm.security.verifysdk.adaptive.RequiresAssessmentResult
import org.junit.AfterClass
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

    
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * @since 3.0.0
 */
@RunWith(AndroidJUnit4::class)
class RemoteMockAdaptiveTests {
    companion object {
        /**
         * Run after all unit tests have finished.
         */
        @AfterClass
        fun tearDown() {
            // Stop collection process.
            AdaptiveContext.stop()
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response.
     *
     * Timeout is 20 seconds.
     */
    @Ignore("License required")
    @Test(timeout = 20_000)
    fun testAssessmentRequires() {
        // Create count down latch to wait for response.
        val latch = CountDownLatch(1)

        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create remote Adaptive delegate.
        val mock = RemoteMockAdaptive()

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful assessment.
                assertTrue(
                    "[Remote] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Remote] Requires assessment response: $adaptiveResult")

                MockHelpers.assessmentResult = adaptiveResult as RequiresAssessmentResult

                when {
                    adaptiveResult.factors.any { assessmentFactor -> assessmentFactor.type == Factor.QR } -> {
                        println("[Remote] Assessment factor is QR login")
                    }
                    adaptiveResult.factors.any { assessmentFactor -> assessmentFactor.type == Factor.PASSWORD } -> {
                        println("[Remote] Assessment factor is password")
                    }
                    adaptiveResult.factors.any { assessmentFactor -> assessmentFactor.type == Factor.FIDO } -> {
                        println("[Remote] Assessment factor is FIDO")
                    }
                }
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
            // Request completed.
            latch.countDown()
        }

        // Wait for requests to complete.
        latch.await()
    }

    /**
     * Perform an evaluation following the assessment above, expecting a "requires" response.
     *
     * Timeout is 20 seconds.
     */
    @Ignore("TAS process not initialized")
    @Test(timeout = 20_000)
    fun testEvaluationRequires() {
        // Create count down latch to wait for response.
        val latch = CountDownLatch(1)

        // Create username password evaluation using transaction ID from previous assessment.
        val evaluation = PasswordEvaluation(
            transactionId = MockHelpers.assessmentResult?.let {
                return@let it.transactionId
            } ?: run {
                ""
            },
            username = "<username>",
            password = "<password>"
        )

        // Create remote Adaptive delegate.
        val mock = RemoteMockAdaptive()

        // Perform evaluation.
        mock.evaluate(evaluation) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful evaluation.
                println("[Remote] Evaluation response: $adaptiveResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
            // Request completed.
            latch.countDown()
        }

        // Wait for requests to complete.
        latch.await()
    }
}
