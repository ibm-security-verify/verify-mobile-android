/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

/**
 * A placeholder protocol to represent a factor evaluation.
 *
 * Useful for the [AdaptiveDelegate.evaluate] method.
 *
 * @since 3.0.0
 */
interface FactorEvaluation {
    /** Transaction identifier used to associate an evaluation. Received in a [RequiresAssessmentResult]. */
    val transactionId: String
}

/**
 * Represents a username and password evaluation.
 *
 * Example usage:
 * ```
 * val passwordEvaluation = PasswordEvaluation(transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e", username = "username", password = "password")
 *
 * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
 * adaptive.evaluate(evaluation = passwordEvaluation) { result ->
 *     result.onFailure { error ->
 *         // Handle `error` of type `Throwable`.
 *     }
 *     result.onSuccess { adaptiveResult ->
 *         // Handle `adaptiveResult` of type `AdaptiveResult`.
 *     }
 * }
 * ```
 *
 * @since 3.0.0
 */
data class PasswordEvaluation(
    /** Transaction identifier used to associate an evaluation. Received in a [RequiresAssessmentResult]. */
    override val transactionId: String,
    /** The username used for evaluation. */
    val username: String,
    /** The associated password for the username. */
    val password: String
) : FactorEvaluation

/**
 * Represents a one-time passcode evaluation.
 *
 * Example usage:
 * ```
 * val oneTimePasscodeEvaluation = OneTimePasscodeEvaluation(transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e", code = "842035")
 *
 * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
 * adaptive.evaluate(evaluation = oneTimePasscodeEvaluation) { result ->
 *     result.onFailure { error ->
 *         // Handle `error` of type `Throwable`.
 *     }
 *     result.onSuccess { adaptiveResult ->
 *         // Handle `adaptiveResult` of type `AdaptiveResult`.
 *     }
 * }
 * ```
 *
 * @since 3.0.0
 */
data class OneTimePasscodeEvaluation(
    /** Transaction identifier used to associate an evaluation. Received in a [RequiresAssessmentResult]. */
    override val transactionId: String,
    /** The code generated from the seed. */
    val code: String,
    /** The type of OTP factor, i.e. SMS OTP, email OTP, or TOTP. */
    val type: OneTimePasscodeFactor
) : FactorEvaluation

/**
 * Represents a knowledge question evaluation, with a list of knowledge questions answers.
 *
 * Example usage:
 * ```
 * val knowledgeQuestionsEvaluation = KnowledgeQuestionEvaluation(transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e", answers = answersJSONArray))
 *
 * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
 * adaptive.evaluate(evaluation = knowledgeQuestionsEvaluation) { result ->
 *     result.onFailure { error ->
 *         // Handle `error` of type `Throwable`.
 *     }
 *     result.onSuccess { adaptiveResult ->
 *         // Handle `adaptiveResult` of type `AdaptiveResult`.
 *     }
 * }
 * ```
 *
 * @since 3.0.0
 */
data class KnowledgeQuestionEvaluation(
    /** Transaction identifier used to associate an evaluation. Received in a [RequiresAssessmentResult]. */
    override val transactionId: String,
    /** The dictionary of question keys and associated answers. */
    val answers: Map<String, String>
) : FactorEvaluation

/**
 * Represents a FIDO evaluation, using FIDO verification data.
 *
 * Example usage:
 * ```
 * val fidoEvaluation = FIDOEvaluation(transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e", authenticatorData = "authenticatorData", userHandle = "userHandle", signature = "signature", clientDataJSON = "clientDataJSON")
 *
 * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
 * adaptive.evaluate(evaluation = fidoEvaluation) { result ->
 *     result.onFailure { error ->
 *         // Handle `error` of type `Throwable`.
 *     }
 *     result.onSuccess { adaptiveResult ->
 *         // Handle `adaptiveResult` of type `AdaptiveResult`.
 *     }
 * }
 * ```
 *
 * @since 3.0.0
 */
data class FIDOEvaluation(
    /** Transaction identifier used to associate an evaluation. Received in a [RequiresAssessmentResult]. */
    override val transactionId: String,
    /** The information about the authentication produced by the authenticator. */
    val authenticatorData: String,
    /** The identifier for the user who owns this authenticator. */
    val userHandle: String?,
    /** The received and signed FIDO challenge from [FIDOGenerateResult]. */
    val signature: String,
    /** The base64Url-encoded clientDataJson that was received from the WebAuthn client. */
    val clientDataJSON: String
) : FactorEvaluation
