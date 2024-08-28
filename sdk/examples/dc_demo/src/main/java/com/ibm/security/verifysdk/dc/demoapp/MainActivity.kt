package com.ibm.security.verifysdk.dc.demoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.ibm.security.verifysdk.authentication.api.OAuthProvider
import com.ibm.security.verifysdk.core.helper.ContextHelper
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.QrCode
import com.ibm.security.verifysdk.dc.api.CredentialsApi
import com.ibm.security.verifysdk.dc.api.InvitationsApi
import com.ibm.security.verifysdk.dc.demoapp.ui.theme.IBMSecurityVerifySDKTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext

class MainActivity : ComponentActivity() {

    private val log: Logger = LoggerFactory.getLogger(javaClass.name)
    private val requestCameraPermissionCode = 88

    private val json =  Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    private val host = ""
    private val hostUrl = URL("https://$host")
    private val accessToken = ""

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextHelper.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            IBMSecurityVerifySDKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Greeting()
                }
            }
        }

        val trustManager = NetworkHelper.insecureTrustManager()
        NetworkHelper.trustManager = trustManager
        NetworkHelper.sslContext = SSLContext.getInstance("TLS").apply {
            init(
                null,
                arrayOf(trustManager),
                java.security.SecureRandom()
            )
        }
        NetworkHelper.hostnameVerifier = HostnameVerifier { _, _ -> true }

        NetworkHelper.initialize()

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startQRCodeScanning()
        } else {
            log.debug("Permission denied")
        }
    }

    private fun requestCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.CAMERA
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
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCameraPermissionCode -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun getCredentials() {

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
//                CredentialsApi(hostUrl).getAll(accessToken = accessToken)
//                    .onSuccess {
//                        log.info(it.toString())
//                    }
//                    .onFailure {
//                        log.error(it.message)
//                    }

                CredentialsApi(hostUrl).getOne(accessToken = accessToken, id = "b3f798b4-2b15-45a8-8118-e5d59a838996")
                    .onSuccess {
                        log.info(it.toString())
                    }
                    .onFailure {
                        log.info(it.toString())
                    }
            }
        }
    }

        @OptIn(ExperimentalSerializationApi::class)
        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val intentResult: IntentResult? =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (intentResult != null && intentResult.contents != null) {

                log.info("qrCode: " + intentResult.contents)
                val qrCode = json.decodeFromString<QrCode>(intentResult.contents)
                log.info(qrCode.toString())

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        OAuthProvider(qrCode.data?.clientId ?: "", null).let { oAuthProvider ->

                            oAuthProvider.authorize(
                                url = qrCode.data?.tokenEndpoint ?: URL("https://localhost"),
                                username = qrCode.data?.name ?: "",
                                password = "secret",
                                scope = null,
                                NetworkHelper.getInstance
                            ).onSuccess {
                                log.info(it.toString())

                                InvitationsApi(hostUrl).getAll(
                                    NetworkHelper.getInstance,
                                    accessToken = it.accessToken
                                )
                                    .onSuccess { invitationList ->
                                        log.info(invitationList.toString())
                                    }
                                    .onFailure { throwable ->
                                        log.error(throwable.message)
                                    }
                            }.onFailure {
                                log.error(it.message)
                            }
                        }
                    }
                }

            }
        }

        @Composable
        fun Greeting() {
            ButtonComposableQrScan()
        }

        @Composable
        fun ButtonComposableQrScan() {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { startQRCodeScanning() },
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Text("Scan QR code")
                }

                Button(
                    onClick = { getCredentials() },
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Text("Get Credentials")
                }
            }
        }

        @Preview(showBackground = true)
        @Composable
        fun GreetingPreview() {
            IBMSecurityVerifySDKTheme {
                Greeting()
            }
        }
    }
