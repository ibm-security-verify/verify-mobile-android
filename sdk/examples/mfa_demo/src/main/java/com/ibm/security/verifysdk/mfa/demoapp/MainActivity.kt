/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */

package com.ibm.security.verifysdk.mfa.demoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.ibm.security.verifysdk.core.ContextHelper
import com.ibm.security.verifysdk.core.threadInfo
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationController
import com.ibm.security.verifysdk.mfa.cloud.CloudAuthenticatorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level


class MainActivity : AppCompatActivity() {

    private val log: Logger = LoggerFactory.getLogger(javaClass.name)
    private val REQUESTCAMERAPERMISSIONCODE = 88
    private lateinit var mfaAuthenticatorDescriptor: MFAAuthenticatorDescriptor

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startQRCodeScanning()
            } else {
                log.debug("XXX Permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextHelper.init(applicationContext)
        setContentView(R.layout.activity_main)

//        NetworkHelper.customLoggingInterceptor =
//            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//        val log: Logger = LoggerFactory.getLogger(MainActivity::class.java)
        log.atLevel(Level.DEBUG).log("XXX DEBUG2")

        log.debug("XXX DEBUG")
        log.error("XXX ERROR")
        log.warn("XXX WARN")
        log.info("XXX INFO")
        log.trace("XXX TRACE")
        log.trace("XXX TRACE")

        findViewById<Button>(R.id.btn_check_transactions).setOnClickListener {
            checkPendingTransaction()
        }

        requestCamera()
    }

    private fun requestCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.CAMERA
            ) -> {
                startQRCodeScanning()
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUESTCAMERAPERMISSIONCODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    startQRCodeScanning()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun startQRCodeScanning() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan QR Code")
        integrator.setOrientationLocked(false)
        integrator.setTorchEnabled(false)
        integrator.initiateScan()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null && intentResult.contents != null) {
            val qrCode = intentResult.contents
            log.error("data: $qrCode")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    log.threadInfo()
                    val result =
                        MFARegistrationController(qrCode).initiate("Carsten's Test account")
                            .onSuccess {
                                val cloudRegistrationProvider = it
                            }
                            .onFailure {
                                val error = it
                            }

                    val cloudRegistrationProvider = result.getOrNull()
                    log.threadInfo()
                    log.info("Counter " + cloudRegistrationProvider?.countOfAvailableEnrollments)
                    cloudRegistrationProvider?.nextEnrollment()
                    cloudRegistrationProvider?.enroll()
                    cloudRegistrationProvider?.nextEnrollment()
                    cloudRegistrationProvider?.enroll()
                    cloudRegistrationProvider?.nextEnrollment()
                    cloudRegistrationProvider?.enroll()
                    cloudRegistrationProvider?.finalize()
                        ?.onSuccess {
                            log.info(it.toString())
                            mfaAuthenticatorDescriptor = it
                        }
                        ?.onFailure {
                            log.error(it.toString())
                        }

                }
            }
        }
    }

    private fun checkPendingTransaction() {

        val cloudAuthenticatorService = CloudAuthenticatorService(
            mfaAuthenticatorDescriptor.token.authorizationHeader(),
            mfaAuthenticatorDescriptor.refreshUri,
            mfaAuthenticatorDescriptor.transactionUri,
            mfaAuthenticatorDescriptor.id
        )

        lifecycleScope.launch {
            withContext((Dispatchers.IO)) {
                cloudAuthenticatorService.nextTransaction()
                    .onSuccess {
                        log.info("Success: $it")
                    }
                    .onFailure {
                        log.info("Failure: $it")
                    }
            }
        }
    }
}
