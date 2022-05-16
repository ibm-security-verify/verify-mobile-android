/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * @since 3.0.0
 */
@RunWith(AndroidJUnit4::class)
class LocalMockAdaptiveTests {
    /**
     * Run after each unit test.
     */
    @After
    fun tearDown() {
        // Stop collection process.
        AdaptiveContext.stop()
    }

    /**
     * Perform an assessment, expecting an "allow" response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAssessmentAllows() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "allow" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.ALLOW)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Received non-allow assessment result.",
                    adaptiveResult is AllowAssessmentResult
                )
                println("[Local] Allow assessment response: $adaptiveResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "deny" response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAssessmentDeny() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "deny" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.DENY)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Received non-deny assessment result.",
                    adaptiveResult is DenyAssessmentResult
                )
                println("[Local] Deny assessment response: $adaptiveResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response that contains an array of "allowedFactors".
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAssessmentRequiresAllowed() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "requires" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.REQUIRES_ALLOWED)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Local] Requires assessment response: $adaptiveResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response that contains an array of "enrolledFactors".
     *
     * Note; in the wild, an assessment will never return a requires enrolled response (i.e. a requires
     * response with "enrolledFactors"). An assessment will always return "allowedFactors", whereas
     * evaluations will always return "enrolledFactors".
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAssessmentRequiresEnrolled() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "requires" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.REQUIRES_ENROLLED)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Local] Requires assessment response: $adaptiveResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response that contains an array of "allowedFactors".
     * Then perform a password evaluation, expecting a "requires" response that contains an array of "enrolledFactors".
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testEvaluationRequiresEnrolled() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "requires" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.REQUIRES_ALLOWED)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                assertTrue(
                    "[Local] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Local] Requires assessment response: $adaptiveResult")

                // Create username password evaluation.
                val evaluation = PasswordEvaluation(
                    transactionId = (adaptiveResult as RequiresAssessmentResult).transactionId,
                    username = "username",
                    password = "password"
                )

                // Expect a "requires" enrolled response.
                mock.testType = MockHelpers.MockAdaptiveTestType.REQUIRES_ENROLLED

                // Perform evaluation.
                mock.evaluate(evaluation = evaluation) { result ->
                    result.onSuccess { adaptiveResult ->
                        assertTrue(
                            "Received non-requires evaluation result.",
                            adaptiveResult is RequiresAssessmentResult
                        )
                        println("Requires evaluation response: $adaptiveResult")
                    }
                    result.onFailure { error ->
                        // Error on assessment.
                        fail(error.message)
                    }
                }
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response that contains an array of "allowedFactors".
     * Then perform a password evaluation, expecting an "allow" response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAdaptivePasswordEvaluationAllow() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "requires" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.REQUIRES_ALLOWED)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                assertTrue(
                    "[Local] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Local] Requires assessment response: $adaptiveResult")

                // Create username password evaluation.
                val evaluation = PasswordEvaluation(
                    transactionId = (adaptiveResult as RequiresAssessmentResult).transactionId,
                    username = "username",
                    password = "password"
                )

                // Expect an "allow" response.
                mock.testType = MockHelpers.MockAdaptiveTestType.ALLOW

                // Perform evaluation.
                mock.evaluate(evaluation = evaluation) { result ->
                    result.onSuccess { adaptiveResult ->
                        assertTrue(
                            "[Local] Received non-allow evaluation result.",
                            adaptiveResult is AllowAssessmentResult
                        )
                        println("[Local] Allow evaluation response: $adaptiveResult")
                    }
                    result.onFailure { error ->
                        // Error on assessment.
                        fail(error.message)
                    }
                }
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an assessment, expecting a "requires" response that contains an array of "allowedFactors".
     * Then perform an OTP evaluation, expecting an "allow" response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testAdaptiveOTPEvaluationAllow() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "requires" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.REQUIRES_ALLOWED)

        // Perform assessment.
        mock.assessment(AdaptiveContext.sessionId) { result ->
            result.onSuccess { adaptiveResult ->
                assertTrue(
                    "[Local] Received non-requires assessment result.",
                    adaptiveResult is RequiresAssessmentResult
                )
                println("[Local] Requires assessment response: $adaptiveResult")

                // Create username password evaluation.
                val evaluation = OneTimePasscodeEvaluation(
                    transactionId = (adaptiveResult as RequiresAssessmentResult).transactionId,
                    code = "12345",
                    type = OneTimePasscodeFactor.EMAIL_OTP
                )

                // Expect an "allow" response.
                mock.testType = MockHelpers.MockAdaptiveTestType.ALLOW

                // Perform evaluation.
                mock.evaluate(evaluation = evaluation) { result ->
                    result.onSuccess { adaptiveResult ->
                        assertTrue(
                            "[Local] Received non-allow evaluation result.",
                            adaptiveResult is AllowAssessmentResult
                        )
                        println("[Local] Allow evaluation response: $adaptiveResult")
                    }
                    result.onFailure { error ->
                        // Error on assessment.
                        fail(error.message)
                    }
                }
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an email OTP generation, expecting a correlation in the response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testGenerationEmailOTP() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "allow" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.ALLOW)

        val assessmentFactor = EnrolledFactor(
            type = Factor.EMAIL_OTP,
            id = "20bc91e2-aefb-47e9-919a-c04561f64066",
            enabled = true,
            validated = true,
            attributes = mapOf()
        )

        // Perform email OTP generation.
        mock.generate(
            enrollmentId = assessmentFactor.id,
            transactionId = "9b3e96c8-851b-4b5b-93c5-efcaae3e35c3",
            factor = assessmentFactor.type
        ) { result ->
            result.onSuccess { otpGenerateResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Didn't receive OTP generation result.",
                    otpGenerateResult is OtpGenerateResult
                )
                val otpGenerateResult = otpGenerateResult as OtpGenerateResult
                assertTrue(
                    "[Local] Didn't receive OTP correlation in generation result.",
                    otpGenerateResult.correlation != null
                )
                println("[Local] Generation response: $otpGenerateResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform an SMS OTP generation, expecting a correlation in the response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testGenerationSMSOTP() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "allow" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.ALLOW)

        val assessmentFactor = EnrolledFactor(
            type = Factor.SMS_OTP,
            id = "20bc91e2-aefb-47e9-919a-c04561f64066",
            enabled = true,
            validated = true,
            attributes = mapOf()
        )

        // Perform email OTP generation.
        mock.generate(
            enrollmentId = assessmentFactor.id,
            transactionId = "9b3e96c8-851b-4b5b-93c5-efcaae3e35c3",
            factor = assessmentFactor.type
        ) { result ->
            result.onSuccess { otpGenerateResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Didn't receive OTP generation result.",
                    otpGenerateResult is OtpGenerateResult
                )
                val otpGenerateResult = otpGenerateResult as OtpGenerateResult
                assertTrue(
                    "[Local] Didn't receive OTP correlation in generation result.",
                    otpGenerateResult.correlation != null
                )
                println("[Local] Generation response: $otpGenerateResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }

    /**
     * Perform a push OTP generation, expecting no correlation in the response.
     */
    @Ignore("TAS process not initialized")
    @Test
    fun testGenerationVoid() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Create mock server, specifying that we'd like an "allow" response.
        val mock = LocalMockAdaptive(MockHelpers.MockAdaptiveTestType.ALLOW)

        val assessmentFactor = EnrolledFactor(
            type = Factor.PUSH,
            id = "20bc91e2-aefb-47e9-919a-c04561f64066",
            enabled = true,
            validated = true,
            attributes = mapOf()
        )

        // Perform email OTP generation.
        mock.generate(
            enrollmentId = assessmentFactor.id,
            transactionId = "9b3e96c8-851b-4b5b-93c5-efcaae3e35c3",
            factor = assessmentFactor.type
        ) { result ->
            result.onSuccess { otpGenerateResult ->
                // Successful assessment.
                assertTrue(
                    "[Local] Didn't receive OTP generation result.",
                    otpGenerateResult is OtpGenerateResult
                )
                val otpGenerateResult = otpGenerateResult as OtpGenerateResult
                assertTrue(
                    "[Local] Received OTP correlation in generation result.",
                    otpGenerateResult.correlation == null
                )
                println("[Local] Generation response: $otpGenerateResult")
            }
            result.onFailure { error ->
                // Error on assessment.
                fail(error.message)
            }
        }
    }
}
