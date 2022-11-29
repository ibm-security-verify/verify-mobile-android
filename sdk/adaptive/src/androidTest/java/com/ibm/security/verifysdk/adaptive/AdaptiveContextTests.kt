/*
 * Copyright contributors to the IBM Security Verify Adaptive SDK for Android project
 */

package com.ibm.security.verifysdk.adaptive

import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
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
@UiThreadTest
class AdaptiveContextTests {
    /**
     * Run after each unit test.
     */
    @After
    fun tearDown() {
        try {
            // Assign an instance of AdaptiveCollection.
            AdaptiveContext.collectionService = MockHelpers.trusteerCollection

            // Stop collection process.
            AdaptiveContext.stop()
        } catch (e: TrusteerOperationException) {
            // Collection has not been started in preceding unit test, pass.
        }
    }

    /**
     * Start the collection of device information.
     */
    @Ignore("License required")
    @Test
    fun testStartCollection() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    /**
     * Stop the collection of device information.
     */
    @Ignore("License required")
    @Test
    fun testStopCollection() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        // Stop the collection process.
        AdaptiveContext.stop()
    }

    /**
     * Start the collection of device information when [AdaptiveContext.collectionService] is `null`.
     *
     * Expect exception to be thrown because [AdaptiveContext.collectionService] is `null`.
     */
    @Test(expected = Exception::class)
    fun testStartCollectionVendorCollectionIsNull() {
        // Don't assign anything to `vendorCollection`, expect an error.
        AdaptiveContext.collectionService = null

        // Start collection process, error is thrown.
        AdaptiveContext.start(InstrumentationRegistry.getInstrumentation().targetContext)

        fail()
    }

    /**
     * Stop the collection of device information when [AdaptiveContext.collectionService] is `null`.
     *
     * Expect exception to be thrown because [AdaptiveContext.collectionService] is `null`.
     */
    @Test(expected = Exception::class)
    fun testStopCollectionVendorCollectionIsNull() {
        // Don't assign anything to `vendorCollection`, expect an error.
        AdaptiveContext.collectionService = null

        // Stop collection process, error is thrown.
        AdaptiveContext.stop()

        fail()
    }

    /**
     * Stop the collection of device information before collection has started.
     *
     * Expect exception to be thrown because collection has not been started.
     */
    @Test(expected = TrusteerOperationException::class)
    @Ignore("Test class broken")
    fun testStopCollectionNotStarted() {
        // Assign an instance of AdaptiveCollection, otherwise we'll get an error.
        AdaptiveContext.collectionService = MockHelpers.trusteerCollection

        // Start collection process, error is thrown because collection hasn't been started.
        AdaptiveContext.stop()

        fail()
    }
}
