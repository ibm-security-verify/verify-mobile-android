//
// Copyright contributors to the IBM Security Verify Authentication SDK for Android project
//
package com.ibm.security.verifysdk.authentication

/**
 * A method used to derive code challenge.
 *
 * @since 3.0.0
 */
@Suppress("unused")
enum class CodeChallengeMethod {

    /**
     *  The plain transformation is for compatibility with existing deployments and for constrained
     *  environments that can't use the S256 transformation.
     */
    PLAIN,

    /**
     *  The S256 method protects against eavesdroppers observing or intercepting the {@code
     *  code_challenge}, because the challenge cannot be used without the verifier.
     */
    S256
}