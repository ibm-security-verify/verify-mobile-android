/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class QrScanContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return IntentIntegrator(context as Activity).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Scan QR Code")
            setBeepEnabled(true)
            setOrientationLocked(false)
            setCaptureActivity(CaptureActivity::class.java)
        }.createScanIntent()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val result = IntentIntegrator.parseActivityResult(resultCode, intent)
        return result?.contents
    }
}