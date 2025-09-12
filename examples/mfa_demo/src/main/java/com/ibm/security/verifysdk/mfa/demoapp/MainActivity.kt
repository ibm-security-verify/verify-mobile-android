/*
 * Copyright contributors to the IBM Security Verify SDK for Android project
 */
package com.ibm.security.verifysdk.mfa.demoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.ibm.security.verifysdk.core.extension.threadInfo
import com.ibm.security.verifysdk.core.helper.ContextHelper
import com.ibm.security.verifysdk.mfa.EnrollableType
import com.ibm.security.verifysdk.mfa.FactorType
import com.ibm.security.verifysdk.mfa.MFAAuthenticatorDescriptor
import com.ibm.security.verifysdk.mfa.MFARegistrationController
import com.ibm.security.verifysdk.mfa.MFAServiceController
import com.ibm.security.verifysdk.mfa.MFAServiceDescriptor
import com.ibm.security.verifysdk.mfa.UserAction
import com.ibm.security.verifysdk.mfa.completeTransaction
import com.ibm.security.verifysdk.mfa.model.cloud.CloudAuthenticator
import com.ibm.security.verifysdk.mfa.model.onprem.OnPremiseAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {

    private val log: Logger = LoggerFactory.getLogger(javaClass.name)
    private val requestCameraPermissionCode = 88
    private lateinit var mfaAuthenticatorDescriptor: MFAAuthenticatorDescriptor
    private lateinit var mfaService: MFAServiceDescriptor

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startQRCodeScanning()
            } else {
                log.debug("Permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextHelper.init(applicationContext)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_scan_qr).setOnClickListener {
            requestCamera()
        }
        findViewById<Button>(R.id.btn_check_transactions).setOnClickListener {
            checkPendingTransaction()
        }
        findViewById<Button>(R.id.btn_approve_transactions).setOnClickListener {
            completeTransaction(UserAction.VERIFY)
        }
        findViewById<Button>(R.id.btn_deny_transactions).setOnClickListener {
            completeTransaction(UserAction.DENY)
        }
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


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCameraPermissionCode -> {
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null && intentResult.contents != null) {
            val qrCode = intentResult.contents
            log.info("data: $qrCode")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    log.threadInfo()
                    val result =
                        MFARegistrationController(qrCode).initiate("Carsten's Test account", false)
                            .onSuccess {
                                val mfaRegistrationProvider = it
                                log.info("Success: ${mfaRegistrationProvider.accountName}")
                            }
                            .onFailure {
                                log.error("Failure: $it")
                            }
                    val mfaRegistrationProvider = result.getOrNull()
                    log.threadInfo()
                    log.info("Counter " + mfaRegistrationProvider?.countOfAvailableEnrollments)

                    var nextEnrollment = mfaRegistrationProvider?.nextEnrollment()
                    while (nextEnrollment != null) {
                        if (nextEnrollment.enrollableType != EnrollableType.FACE) { // not supported
                            mfaRegistrationProvider?.enroll()
                        }
                        nextEnrollment = mfaRegistrationProvider?.nextEnrollment()
                    }
                    mfaRegistrationProvider?.finalize()
                        ?.onSuccess {
                            log.info("Success: $it")
                            mfaAuthenticatorDescriptor = it
                        }
                        ?.onFailure {
                            log.error("Failure: $it")
                        }

                }
            }
        }
    }

    private fun checkPendingTransaction() {

        mfaService = MFAServiceController(mfaAuthenticatorDescriptor).initiate()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                mfaService.nextTransaction()
                    .onSuccess { nextTransactionInfo ->
                        log.info("Success: $nextTransactionInfo")
                    }
                    .onFailure {
                        log.info("Failure: $it")
                    }
            }
        }
    }

    private fun completeTransaction(userAction: UserAction) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (mfaAuthenticatorDescriptor) {
                    is CloudAuthenticator -> {
                        mfaAuthenticatorDescriptor.allowedFactors.firstOrNull { it.id == mfaService.currentPendingTransaction?.factorID }
                            ?.let { factorType ->
                                mfaService.completeTransaction(
                                    userAction,
                                    factorType
                                )
                                    .onSuccess {
                                        log.info("Success: ${mfaService.currentPendingTransaction?.message}")
                                    }
                                    .onFailure {
                                        log.error("Failure: $it")
                                    }
                            }
                    }

                    is OnPremiseAuthenticator -> {
                        mfaAuthenticatorDescriptor.allowedFactors.filterIsInstance<FactorType.UserPresence>()
                            .firstOrNull() {
                                it.value.keyName.split(".")[1].equals(
                                    mfaService.currentPendingTransaction?.factorType,
                                    ignoreCase = true
                                )
                            }?.let {
                                processFactorType(userAction = userAction, factorType = it)
                            }

                        mfaAuthenticatorDescriptor.allowedFactors.filterIsInstance<FactorType.Fingerprint>()
                            .firstOrNull() {
                                it.value.keyName.split(".")[1].equals(
                                    mfaService.currentPendingTransaction?.factorType,
                                    ignoreCase = true
                                )
                            }?.let {
                                processFactorType(userAction = userAction, factorType = it)
                            }

                        mfaAuthenticatorDescriptor.allowedFactors.filterIsInstance<FactorType.Face>()
                            .firstOrNull() {
                                it.value.keyName.split(".")[1].equals(
                                    mfaService.currentPendingTransaction?.factorType,
                                    ignoreCase = true
                                )
                            }?.let {
                                processFactorType(userAction = userAction, factorType = it)
                            }
                    }
                }
            }
        }
    }

    private suspend fun processFactorType(
        userAction: UserAction,
        factorType: FactorType
    ) {
        mfaService.completeTransaction(
            userAction,
            factorType
        )
            .onSuccess {
                log.info("Success: ${mfaService.currentPendingTransaction?.message}")
            }
            .onFailure {
                log.error("Failure: $it")
            }
    }

}

