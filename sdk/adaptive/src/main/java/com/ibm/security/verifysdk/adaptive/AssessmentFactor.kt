/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

import java.io.Serializable

/**
 * The [AssessmentFactor] is used to represent factors in a [RequiresAssessmentResult.factors].
 *
 * @since 3.0.0
 */
interface AssessmentFactor : Serializable {
    /** The type of factor. */
    var type: Factor
}

/**
 * The [AllowedFactor] data class represents first-factors in a [RequiresAssessmentResult.factors]
 * returned from the [AdaptiveDelegate.assessment] method.
 *
 * These returned factors are JSON objects, containing the property `type`.
 *
 * @param type The type of first-factor. Options are [Factor.QR], [Factor.FIDO], [Factor.PASSWORD].
 *
 * @since 3.0.0
 */
data class AllowedFactor(
    /** The type of first-factor. */
    override var type: Factor
) : AssessmentFactor

/**
 * The [EnrolledFactor] data class represents first-factors in a [RequiresAssessmentResult.factors]
 * returned from the [AdaptiveDelegate.evaluate] method.
 *
 * These returned factors are JSON objects, containing the properties `type`, `id`, `enabled`,
 * `validated`, and `attributes`.
 *
 * @param type The type of second-factor. Options are [Factor.SMS_OTP], [Factor.EMAIL_OTP],
 * [Factor.TOTP], [Factor.QUESTIONS], [Factor.PUSH], [Factor.FIDO].
 * @param id The unique identifier of the factor enrollment.
 * @param enabled A flag to indicate the enrollment is enabled.
 * @param validated A flag to indicate the enrollment is validated.
 * @param attributes Any additional attributes which may be present in the enrolled factor.
 *
 * @since 3.0.0
 */
data class EnrolledFactor(
    /** The type of second-factor. */
    override var type: Factor,
    /** The unique identifier of the factor enrollment. */
    var id: String,
    /** A flag to indicate the enrollment is enabled. */
    var enabled: Boolean?,
    /** A flag to indicate the enrollment is validated. */
    var validated: Boolean?,
    /** Any additional attributes which may be present in the enrolled factor. */
    var attributes: Map<String, Serializable>
) : AssessmentFactor
