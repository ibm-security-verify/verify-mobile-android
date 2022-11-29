/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class RemoteMockAdaptive : AdaptiveDelegate {
    private val address: String = "http://<your_ip_address>:3000"

    override fun assessment(
        sessionId: String,
        evaluationContext: String,
        completion: (Result<AdaptiveResult>) -> Unit
    ) {
        val bodyJson =
            "{\"sessionId\": \"$sessionId\", \"evaluationContext\": \"$evaluationContext\"}"

        "$address/assessments".httpPost().body(bodyJson).header("Content-Type", "application/json")
            .response { request, response, result ->
                result.failure { completion(Result.failure(it)) }

                result.success {
                    val responseJson = JSONObject(String(response.data))

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
                                                    factorsJson.getJSONObject(factorString)
                                                        .get("type").toString()
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
                                                val enrollment =
                                                    factorsJson.getJSONObject(factorString)

                                                val factor =
                                                    Factor.from(enrollment.getString("type"))
                                                val id = enrollment.getString("id")
                                                val validated = enrollment.getBoolean("validated")
                                                val enabled = enrollment.getBoolean("enabled")
                                                val attributes =
                                                    enrollment.getJSONObject("attributes")
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
//        val mockService = LocalMockAdaptiveService(returnType = this.testType);
//        mockService.performGeneration(factor = factor.type, transactionId = transactionId) { result ->
//            result.onFailure { completion(Result.failure(it)) }
//            result.onSuccess {
//                when (factor.type) {
//                    Factor.EMAIL_OTP, Factor.SMS_OTP -> {
//                        val responseJson = JSONObject(it!!)
//                        val correlation = responseJson.get("correlation") as String
//                        completion(Result.success(OtpGenerateResult(correlation = correlation)))
//                    }
//                    else -> completion(Result.success(OtpGenerateResult(correlation = null)))
//                }
//            }
//        }
    }

    override fun evaluate(
        evaluation: FactorEvaluation,
        evaluationContext: String,
        completion: (Result<AdaptiveResult>) -> Unit
    ) {
        if (evaluation !is PasswordEvaluation) {
            completion(Result.failure(Exception("Remote mock test only supports password evaluation.")))
        }
        val passwordEvaluation = evaluation as PasswordEvaluation

        val bodyJson =
            "{\"sessionId\": \"${AdaptiveContext.sessionId}\", \"evaluationContext\": \"$evaluationContext\", \"transactionId\":\"${passwordEvaluation.transactionId}\", \"username\":\"${passwordEvaluation.username}\", \"password\":\"${passwordEvaluation.password}\"}"

        "$address/evaluations/password".httpPost().body(bodyJson)
            .header("Content-Type", "application/json").response { request, response, result ->
                result.failure { completion(Result.failure(it)) }

                result.success {
                    val responseJson = JSONObject(String(response.data))

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
