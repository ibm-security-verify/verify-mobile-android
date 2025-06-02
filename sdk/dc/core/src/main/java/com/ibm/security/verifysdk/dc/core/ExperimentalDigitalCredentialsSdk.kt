/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.core

/**
 * Marks an API as experimental within the Digital Credentials SDK.
 *
 * Usage of this annotation indicates that the annotated class is part of an experimental feature
 * that may be subject to breaking changes in future releases. APIs marked with this annotation
 * require explicit opt-in by using `@OptIn(ExperimentalDigitalCredentialsSdk::class)`.
 *
 * This annotation enforces strict opt-in at the **ERROR** level, meaning that usage without
 * proper opt-in will result in a compilation error.
 *
 * ### How to Use:
 * To use an experimental API, you must explicitly opt in:
 * ```kotlin
 * @OptIn(ExperimentalDigitalCredentialsSdk::class)
 * fun myFunction() {
 *     // Experimental API usage
 * }
 * ```
 *
 * @see RequiresOptIn
 */
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS
)
annotation class ExperimentalDigitalCredentialsSdk