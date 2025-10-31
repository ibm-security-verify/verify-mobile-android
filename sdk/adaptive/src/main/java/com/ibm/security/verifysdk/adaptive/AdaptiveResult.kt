/*
 * Copyright contributors to the IBM Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

import org.json.JSONArray
import java.io.Serializable

/**
 * A placeholder interface to represent the result of an assessment operation.
 *
 * @since 3.0.0
 */
interface AdaptiveResult : Serializable

/**
 * A placeholder interface to represent the result of a generate operation.
 *
 * @since 3.0.0
 */
interface GenerateResult : Serializable

/**
 * A structure representing a [AssessmentStatusType.deny] assessment result.
 *
 * Useful for returning from the [AdaptiveDelegate.assessment] and [AdaptiveDelegate.evaluate]
 * methods.
 *
 * Example usage:
 * ```
 * val denyResult = DenyAssessmentResult()
 * ```
 *
 * @since 3.0.0
 */
data class DenyAssessmentResult(
    /** The status of the assessment result. */
    private val status: String = AssessmentStatusType.deny
) : AdaptiveResult

/**
 * A structure representing a [AssessmentStatusType.allow] assessment result.
 *
 * Useful for returning from the [AdaptiveDelegate.assessment] and [AdaptiveDelegate.evaluate]
 * methods.
 *
 * @param token The JSON token string received in the HTTP body following a
 * [AssessmentStatusType.allow] result from a [AdaptiveDelegate.assessment] or
 * [AdaptiveDelegate.evaluate] request.
 *
 * Example usage:
 * ```
 * val allowResult = AllowAssessmentResult(token = "{ \"token\": ... }")
 * ```
 *
 * @since 3.0.0
 */
data class AllowAssessmentResult(
    /** The status of the assessment result. */
    private val status: String = AssessmentStatusType.allow,
    /** JSON string representing an OAuth token received after a successful assessment. */
    val token: String
) : AdaptiveResult

/**
 * A structure representing a [AssessmentStatusType.requires] assessment result.
 *
 * Useful for returning from the [AdaptiveDelegate.assessment] and [AdaptiveDelegate.evaluate]
 * methods.
 *
 * @param transactionId The identifier of the transaction received in the HTTP body following a
 * [AssessmentStatusType.requires] result from a [AdaptiveDelegate.assessment] or
 * [AdaptiveDelegate.evaluate]
 * request.
 * @param factors The list of [AssessmentFactor]s representing the received factors following a
 * [AssessmentStatusType.requires] result from a [AdaptiveDelegate.assessment] or
 * [AdaptiveDelegate.evaluate]
 * request.
 *
 * Example usage:
 * ```
 * val assessmentFactors = listOf(
 *     AllowedFactor(type = Factor.from("emailotp")),
 *     AllowedFactor(type = Factor.from("smsotp"))
 * )
 * val requiresResult = RequiresAssessmentResult(transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e", factors = assessmentFactors)
 * ```
 *
 * @since 3.0.0
 */
data class RequiresAssessmentResult(
    /** The status of the assessment result. */
    private val status: String = AssessmentStatusType.requires,
    /** Transaction identifier used to associate an evaluation. */
    val transactionId: String,
    /** The list of [AssessmentFactor]s that can be used for authentication. */
    val factors: List<AssessmentFactor>
) : AdaptiveResult

/**
 * Represents a one-time password (OTP) generate result.
 *
 * Useful for returning from the [AdaptiveDelegate.generate] methods when generating an SMS OTP or
 * an email OTP.
 *
 * @param correlation The correlation returned after generating the SMS or email OTP. This
 * correlation is a 4 digit number that will be prefixed to the SMS or email OTP, and is useful
 * for identifying the OTP generated.
 *
 * Example usage:
 * ```
 * val otpGenerateResult = OtpGenerateResult(correlation = "1234")
 * ```
 *
 * @since 3.0.0
 */
data class OtpGenerateResult(
    /** The prefix correlation of the one-time password. */
    val correlation: String?
) : GenerateResult

/**
 * Represents a knowledge questions generate result.
 *
 * Useful for returning from the [AdaptiveDelegate.generate] methods when generating a knowledge
 * questions.
 *
 * @param questions The [Map] of `questions` associated to their `questionsKey`s returned after
 * generating the knowledge questions.
 *
 * Example usage:
 * ```
 * val questions = mapOf("bestFriend" to "What is the first name of your best friend?", "firstHouseStreet" to "What was the street name of the first house you ever lived in?") // Received from `AdaptiveDelegate.generate`
 * val questionsGenerateResult = KnowledgeQuestionsGenerateResult(questions = questions)
 * ```
 *
 * @since 3.0.0
 */
data class KnowledgeQuestionsGenerateResult(
    val questions: Map<String, String>
) : GenerateResult

/**
 * Represents a FIDO generate result.
 *
 * Useful for returning from the [AdaptiveDelegate.generate] methods when generating a FIDO
 * verification.
 *
 * @param rpId The relying party ID.
 * @param challenge The unique challenge used as part of this authentication attempt.
 * @param userVerification The extent to which the user must verify.
 * @param timeout The time for the client to wait for user interaction.
 * @param allowCredentials The credentials allowed to perform authentication. Can be empty when
 * performing login without a username. Should be a JSONArray of objects in the form:
 * ```
 * {
 *   "type": "public-key", // The type of the credential. It must be 'public-key' for FIDO.
 *   "id": "SSBhbSBhIGNyZWRlbnRpYWwK" // The credential identifier.
 * }
 * ```
 *
 * @since 3.0.0
 */
data class FIDOGenerateResult(
    /** The relying party ID. */
    val rpId: String,
    /** The unique challenge used as part of this authentication attempt. */
    val challenge: String,
    /** The extent to which the user must verify. */
    val userVerification: String?,
    /** The time for the client to wait for user interaction. */
    val timeout: Int,
    /** The credentials allowed to perform authentication. Can be empty when performing login
     * without a username. */
    val allowCredentials: JSONArray?
) : GenerateResult

/**
 * An interface to perform Adaptive authenticator operations.
 *
 * @since 3.0.0
 */
interface AdaptiveDelegate {
    /**
     * Executes an assessment against a endpoint that integrates against Cloud Identity Policy Driven Authentication services.
     *
     * @param sessionId The session identifier for the hosting application (from [AdaptiveContext.sessionId]).
     * @param evaluationContext The stage in the application for which to perform an evaluation.
     * (Used for continuous assessment throughout the application.) Different "stages" or "contexts"
     * will result in different evaluation results, as configured in the sub-policies of the tenant
     * application's policy. Possible options are "login" (default), "landing", "profile", "resume",
     * "highassurance", "other".
     * @param completion A [Result]<[AdaptiveResult]> callback variable.
     *
     * Example usage:
     * ```
     * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
     * adaptive.assessment(sessionId = AdaptiveContext.sessionId, operation = "login") { result ->
     *     result.onFailure { error ->
     *         // Handle `error` of type `Throwable`.
     *     }
     *     result.onSuccess { adaptiveResult ->
     *         // Handle `adaptiveResult` of type `AdaptiveResult`.
     *     }
     * }
     * ```
     */
    fun assessment(
        sessionId: String,
        evaluationContext: String = "login",
        completion: (Result<AdaptiveResult>) -> Unit
    )

    /**
     * Generates a factor against an endpoint that integrates against Cloud Identity Policy Driven Authentication services.
     *
     * @param enrollmentId The identifier of the enrollment to generate a verification for.
     * @param transactionId A transaction identifier received in a [AssessmentStatusType.requires]
     * response from a [AdaptiveDelegate.assessment] or [AdaptiveDelegate.evaluate] request.
     * @param factor A [Factor].
     * @param completion A [Result]<[GenerateResult]?> callback variable.
     *
     * Example usage:
     * ```
     * val assessmentFactor = AllowedFactor(type = Factor.EMAIL_OTP)
     * val transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e" // Received from previous `AdaptiveDelegate.`assessment or `AdaptiveDelegate.evaluate` calls.
     *
     * val adaptive = Adaptive() // Where `Adaptive` is an implementation of the `AdaptiveDelegate` interface.
     * adaptive.generate(factor = assessmentFactor, transactionId = transactionId) { result ->
     *     result.onFailure { error ->
     *         // Handle `error` of type `Throwable`.
     *     }
     *     result.onSuccess { generationResult ->
     *         // Handle `generationResult` of type `GenerateResult?`.
     *     }
     * }
     * ```
     */
    fun generate(
        enrollmentId: String,
        transactionId: String,
        factor: Factor,
        completion: (Result<GenerateResult>) -> Unit
    )

    /**
     * Evaluates a factor against an endpoint that integrates against Cloud Identity Policy Driven Authentication services.
     *
     * @param evaluation A [PasswordEvaluation] or [OneTimePasscodeEvaluation] instance.
     * @param evaluationContext The stage in the application for which to perform an evaluation.
     * (Used for continuous assessment throughout the application.) Different "stages" or "contexts"
     * will result in different evaluation results, as configured in the sub-policies of the tenant
     * application's policy. Possible options are "login" (default), "landing", "profile", "resume",
     * "highassurance", "other".
     * @param completion A [Result]<[AdaptiveResult]> callback variable.
     *
     * Example usage:
     * ```
     * val transactionId = "36a101c7-7426-4f45-ab3c-55f8dc075c6e" // Received from previous `AdaptiveDelegate.assessment` or `AdaptiveDelegate.evaluate` calls.
     * val otp = "842035" // Received from a previous `AdaptiveDelegate.generate` call.
     * val oneTimePasscodeEvaluation = OneTimePasscodeEvaluation(transactionId = transactionId, code = otp) // Can be any instance of `FactorEvaluation`.
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
     */
    fun evaluate(
        evaluation: FactorEvaluation,
        evaluationContext: String = "login",
        completion: (Result<AdaptiveResult>) -> Unit
    ) // TODO: Pass session ID.
}
