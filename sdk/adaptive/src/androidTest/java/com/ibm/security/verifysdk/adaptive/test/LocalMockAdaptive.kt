/*
 * Copyright contributors to the IBM Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive.test

import com.ibm.security.verifysdk.adaptive.AdaptiveDelegate
import com.ibm.security.verifysdk.adaptive.AdaptiveResult
import com.ibm.security.verifysdk.adaptive.AllowAssessmentResult
import com.ibm.security.verifysdk.adaptive.AllowedFactor
import com.ibm.security.verifysdk.adaptive.AssessmentStatusType
import com.ibm.security.verifysdk.adaptive.DenyAssessmentResult
import com.ibm.security.verifysdk.adaptive.EnrolledFactor
import com.ibm.security.verifysdk.adaptive.Factor
import com.ibm.security.verifysdk.adaptive.FactorEvaluation
import com.ibm.security.verifysdk.adaptive.GenerateResult
import com.ibm.security.verifysdk.adaptive.OtpGenerateResult
import com.ibm.security.verifysdk.adaptive.RequiresAssessmentResult
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LocalMockAdaptive(var testType: MockHelpers.MockAdaptiveTestType) : AdaptiveDelegate {
    override fun assessment(
        sessionId: String,
        evaluationContext: String,
        completion: (Result<AdaptiveResult>) -> Unit
    ) {
        val mockService = LocalMockAdaptiveService(returnType = this.testType)
        mockService.performAssessment(
            sessionId = sessionId,
            evaluationContext = evaluationContext
        ) { result ->
            result.onFailure { completion(Result.failure(it)) }
            result.onSuccess {
                val responseJson = JSONObject(it!!)

                completion(
                    when (responseJson.get("status")) {
                        AssessmentStatusType.allow -> {
                            val token = responseJson.get("token").toString()
                            Result.success(AllowAssessmentResult(token = token))
                        }
                        AssessmentStatusType.requires -> {
                            val transactionId = responseJson.get("transactionId").toString()

                            when {
                                responseJson.has("allowedFactors") -> {
                                    val factorsJson =
                                        responseJson.get("allowedFactors") as JSONArray
                                    val allowedFactors =
                                        List(factorsJson.length()) { factorString ->
                                            val factorType = Factor.from(
                                                factorsJson.getJSONObject(factorString).get("type")
                                                    .toString()
                                            )
                                            AllowedFactor(type = factorType)
                                        }
                                    Result.success(
                                        RequiresAssessmentResult(
                                            transactionId = transactionId,
                                            factors = allowedFactors
                                        )
                                    )
                                }
                                responseJson.has("enrolledFactors") -> {
                                    val factorsJson =
                                        responseJson.get("enrolledFactors") as JSONArray
                                    val allowedFactors =
                                        List(factorsJson.length()) { factorString ->
                                            val enrollment = factorsJson.getJSONObject(factorString)

                                            val factor = Factor.from(enrollment.getString("type"))
                                            val id = enrollment.getString("id")
                                            val validated = enrollment.getBoolean("validated")
                                            val enabled = enrollment.getBoolean("enabled")
                                            val attributes = enrollment.getJSONObject("attributes")
                                            val attributesMap = attributes.keys().asSequence()
                                                .associateWith { key ->
                                                    attributes[key] as String
                                                }
                                            EnrolledFactor(
                                                type = factor,
                                                id = id,
                                                validated = validated,
                                                enabled = enabled,
                                                attributes = attributesMap
                                            )
                                        }
                                    Result.success(
                                        RequiresAssessmentResult(
                                            transactionId = transactionId,
                                            factors = allowedFactors
                                        )
                                    )
                                }
                                else -> {
                                    Result.failure(JSONException("Neither \"allowedFactors\" or \"enrolledFactors\" found in requires response."))
                                }
                            }
                        }
                        else -> Result.success(DenyAssessmentResult())
                    }
                )
            }
        }
    }

    override fun generate(
        enrollmentId: String,
        transactionId: String,
        factor: Factor,
        completion: (Result<GenerateResult>) -> Unit
    ) {
        val mockService = LocalMockAdaptiveService(returnType = this.testType)
        mockService.performGeneration(
            enrollmentId = enrollmentId,
            transactionId = transactionId,
            factor = factor
        ) { result ->
            result.onFailure { completion(Result.failure(it)) }
            result.onSuccess {
                when (factor) {
                    Factor.EMAIL_OTP, Factor.SMS_OTP -> {
                        val responseJson = JSONObject(it!!)
                        val correlation = responseJson.get("correlation") as String
                        completion(Result.success(OtpGenerateResult(correlation = correlation)))
                    }
                    else -> completion(Result.success(OtpGenerateResult(correlation = null)))
                }
            }
        }
    }

    override fun evaluate(
        evaluation: FactorEvaluation,
        evaluationContext: String,
        completion: (Result<AdaptiveResult>) -> Unit
    ) {
        val mockService = LocalMockAdaptiveService(returnType = this.testType)
        mockService.performEvaluation(
            transactionId = evaluation.transactionId,
            evaluationContext = evaluationContext
        ) { result ->
            result.onFailure { completion(Result.failure(it)) }
            result.onSuccess {
                val responseJson = JSONObject(it!!)

                completion(
                    when (responseJson.get("status")) {
                        AssessmentStatusType.allow -> {
                            val token = responseJson.get("token").toString()
                            Result.success(AllowAssessmentResult(token = token))
                        }
                        AssessmentStatusType.requires -> {
                            val transactionId = responseJson.get("transactionId").toString()

                            when {
                                responseJson.has("allowedFactors") -> {
                                    val factorsJson =
                                        responseJson.get("allowedFactors") as JSONArray
                                    val allowedFactors =
                                        List(factorsJson.length()) { factorString ->
                                            val factorType = Factor.from(
                                                factorsJson.getJSONObject(factorString).get("type")
                                                    .toString()
                                            )
                                            AllowedFactor(type = factorType)
                                        }
                                    Result.success(
                                        RequiresAssessmentResult(
                                            transactionId = transactionId,
                                            factors = allowedFactors
                                        )
                                    )
                                }
                                responseJson.has("enrolledFactors") -> {
                                    val factorsJson =
                                        responseJson.get("enrolledFactors") as JSONArray
                                    val allowedFactors =
                                        List(factorsJson.length()) { factorString ->
                                            val enrollment = factorsJson.getJSONObject(factorString)

                                            val factor = Factor.from(enrollment.getString("type"))
                                            val id = enrollment.getString("id")
                                            val validated = enrollment.getBoolean("validated")
                                            val enabled = enrollment.getBoolean("enabled")
                                            val attributes = enrollment.getJSONObject("attributes")
                                            val attributesMap = attributes.keys().asSequence()
                                                .associateWith { key ->
                                                    attributes[key] as String
                                                }
                                            EnrolledFactor(
                                                type = factor,
                                                id = id,
                                                validated = validated,
                                                enabled = enabled,
                                                attributes = attributesMap
                                            )
                                        }
                                    Result.success(
                                        RequiresAssessmentResult(
                                            transactionId = transactionId,
                                            factors = allowedFactors
                                        )
                                    )
                                }
                                else -> {
                                    Result.failure(JSONException("Neither \"allowedFactors\" or \"enrolledFactors\" found in requires response."))
                                }
                            }
                        }
                        else -> Result.success(DenyAssessmentResult())
                    }
                )
            }
        }
    }
}

class LocalMockAdaptiveService(private val returnType: MockHelpers.MockAdaptiveTestType) {
    private val allowJSON = """
    { "status": "allow",
        "token" : {
            "issued_at": "1420262924658",
            "scope": "READ",
            "application_name": "ce1e94a2-9c3e-42fa-a2c6-1ee01815476b",
            "refresh_token_issued_at": "1420262924658",
            "expires_in": "1799",
            "token_type": "BearerToken",
            "refresh_token": "fYACGW7OCPtCNDEnRSnqFlEgogboFPMm",
            "client_id": "5jUAdGv9pBouF0wOH5keAVI35GBtx3dT",
            "access_token": "2l4IQtZXbn5WBJdL6EF7uenOWRsi",
            "organization_name": "My Happy Place",
            "refresh_token_expires_in": "86399"
        }
    }
    """
    private val denyJSON = """
    { "status": "deny" }
    """
    private val requiresEnrolledJSON = """
    { "status": "requires",
       "transactionId": "\(UUID().uuidString)",
       "enrolledFactors": [{
          "id": "61e39f0a-836b-48fa-b4c9-cface6a3ef5a",
          "userId": "60300035KP",
          "type": "fido2",
          "created": "2020-06-15T02:51:49.131Z",
          "updated": "2020-06-15T03:15:18.896Z",
          "attempted": "2020-07-16T04:30:14.066Z",
          "enabled": true,
          "validated": true,
          "attributes": {
            "emailAddress": "email@email.com"
          }
        },
        {
          "id": "61e39f0a-836b-48fa-b4c9-cface6a3ef5a",
          "userId": "60300035KP",
          "type": "multiFactor",
          "created": "2020-06-15T02:51:49.131Z",
          "updated": "2020-06-15T03:15:18.896Z",
          "attempted": "2020-07-16T04:30:14.066Z",
          "enabled": true,
          "validated": true,
          "attributes": {
            "emailAddress": "email@email.com",
            "biometry": "face"
          }
        }
      ]
    }
    """

    private val requiresAllowedJSON = """
    { "status": "requires",
       "transactionId": "\(UUID().uuidString)",
       "allowedFactors" : [{ "type": "qr" }, { "type": "fido2" }, { "type": "password" }]
    }
    """

    private val generateJSON = """
    {
       "correlation": "1234"
    }
    """

    private val assessmentResults = mapOf(
        MockHelpers.MockAdaptiveTestType.ALLOW to allowJSON,
        MockHelpers.MockAdaptiveTestType.DENY to denyJSON,
        MockHelpers.MockAdaptiveTestType.REQUIRES_ALLOWED to requiresAllowedJSON,
        MockHelpers.MockAdaptiveTestType.REQUIRES_ENROLLED to requiresEnrolledJSON
    )

    fun performAssessment(
        sessionId: String,
        evaluationContext: String,
        completion: (Result<String?>) -> Unit
    ) {
        completion(Result.success(assessmentResults[returnType]))
    }

    fun performEvaluation(
        transactionId: String,
        evaluationContext: String,
        completion: (Result<String?>) -> Unit
    ) {
        completion(Result.success(assessmentResults[returnType]))
    }

    fun performGeneration(
        enrollmentId: String,
        transactionId: String,
        factor: Factor,
        completion: (Result<String?>) -> Unit
    ) {
        when (factor) {
            Factor.EMAIL_OTP, Factor.SMS_OTP -> completion(Result.success(generateJSON))
            else -> completion(Result.success(""))
        }
    }
}
