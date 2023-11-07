/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive.test

import com.ibm.security.verifysdk.adaptive.RequiresAssessmentResult

object MockHelpers {
    /** A [RequiresAssessmentResult] instance to keep track of state between requests. */
    var assessmentResult: RequiresAssessmentResult? = null

    /** The vendor collection to be assigned to [AdaptiveContext.collectionService]. */
    val trusteerCollection = TrusteerAdaptiveCollection(
        vendorId = "<your_vendor_id>",
        clientId = "<your_client_id>",
        clientKey = "<your_client_key>"
    )

    /** A convenience enum to support different Adaptive responses for mock tests. */
    enum class MockAdaptiveTestType {
        ALLOW,
        DENY,
        REQUIRES_ALLOWED,
        REQUIRES_ENROLLED
    }
}
