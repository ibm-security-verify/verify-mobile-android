/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */
package com.ibm.security.verifysdk.dc.demoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.ibm.security.verifysdk.core.helper.ContextHelper
import com.ibm.security.verifysdk.core.helper.NetworkHelper
import com.ibm.security.verifysdk.dc.core.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.cloud.WalletProvider
import com.ibm.security.verifysdk.dc.demoapp.data.WalletEntity
import com.ibm.security.verifysdk.dc.demoapp.data.WalletManager
import com.ibm.security.verifysdk.dc.demoapp.ui.WalletViewModel
import com.ibm.security.verifysdk.dc.demoapp.ui.WalletViewModelFactory
import com.ibm.security.verifysdk.dc.demoapp.ui.credential.CredentialScreen
import com.ibm.security.verifysdk.dc.demoapp.ui.theme.IBMSecurityVerifySDKTheme
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.VerificationDoneScreen
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.VerificationIdentityDetailsScreen
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.VerificationRequestScreen
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.VerificationScreen
import com.ibm.security.verifysdk.dc.demoapp.ui.wallet.WalletScreen
import com.ibm.security.verifysdk.dc.core.CredentialDescriptor
import com.ibm.security.verifysdk.dc.cloud.model.VerificationInfo
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dns
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.security.SecureRandom
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext

@ExperimentalDigitalCredentialsSdk
class MainActivity : ComponentActivity() {

    private val log: Logger = LoggerFactory.getLogger(javaClass.name)
    private val requestCameraPermissionCode = 88

    private val walletViewModel: WalletViewModel by viewModels {
        WalletViewModelFactory((application as DcApplication).repository)
    }

    // Change this to the IP of the diagency service.
    private val hostLocal = "iviadcgw-default.ivia-dc-demo-560b083b6ae574bb5eb8ef2f0de647f7-0000.au-syd.containers.appdomain.cloud"
    private lateinit var walletManager: WalletManager

    // Change this to the data this is presented in the QR code.
    private var agentQrCodeData = """
            {
                    "name":"holder_1",
                    "id":"cn=user_1,ou=users,dc=ibm,dc=com",
                    "serviceBaseUrl":"https://$hostLocal",
                    "clientId":"onpremise_vcholders",
                    "aznCode": "12345ABCDE",
                    "oauthBaseUrl": "https://$hostLocal/oauth2/token"
            }
            """.trimIndent().replace("\n", "").replace(" ", "")

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextHelper.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            IBMSecurityVerifySDKTheme {
                MyApp(walletViewModel)
            }
        }

        val trustManager = NetworkHelper.insecureTrustManager()
        NetworkHelper.trustManager = trustManager
        NetworkHelper.sslContext = SSLContext.getInstance("TLS").apply {
            init(
                null,
                arrayOf(trustManager),
                SecureRandom()
            )
        }
        NetworkHelper.hostnameVerifier = HostnameVerifier { _, _ -> true }
        NetworkHelper.customDnsResolver = object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                return if (hostname == "diagency") {
                    listOf(InetAddress.getByName(hostLocal))
                } else {
                    InetAddress.getAllByName(hostname).toList()
                }
            }
        }
        NetworkHelper.initialize()
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
                }
                return
            }
            // Add other 'when' lines to check for other permissions this app might request.
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
        integrator.setBeepEnabled(true)
        integrator.setCaptureActivity(CaptureActivity::class.java)
        integrator.initiateScan()
    }

    private fun getWallet() {

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val walletProvider =
                        WalletProvider(jsonData = agentQrCodeData, ignoreSSLCertificate = true)
                    val wallet = walletProvider.initiate("John", "user_1", "secret")
                    val walletEntity = WalletEntity(wallet = wallet)
                    walletViewModel.insert(walletEntity)
                    walletManager =
                        WalletManager(walletEntity = walletEntity, viewModel = walletViewModel)
                }
            } catch (e: Exception) {
                showErrorDialog("Failed to initiate wallet", e.message ?: e.toString())
            }
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        AlertDialog.Builder(this) // Use `requireContext()` if in a Fragment
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null && intentResult.contents != null) {
            agentQrCodeData = intentResult.contents
            log.info("agentQrCodeData: $agentQrCodeData")
        }
    }

    @Composable
    fun MyApp(walletViewModel: WalletViewModel) {
        val navController = rememberNavController()
        var credentialList by remember { mutableStateOf<List<CredentialDescriptor>>(emptyList()) }
        var verificationList by remember { mutableStateOf<List<VerificationInfo>>(emptyList()) }

        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Wallet.route
            ) {
                composable(BottomNavItem.Wallet.route) {
                    WalletScreen(
                        viewModel = walletViewModel,
                        innerPadding = innerPadding,
                        onCredentialsLoaded = { credentialList = it },
                        onVerificationsLoaded = { verificationList = it },
                        getWallet = this@MainActivity::getWallet
                    )
                }
                composable(BottomNavItem.Credential.route) {
                    CredentialScreen(
                        walletViewModel = walletViewModel,
                        innerPadding = innerPadding
                    )
                }
                composable(BottomNavItem.Verification.route) {
                    VerificationScreen(
                        walletViewModel = walletViewModel,
                        innerPadding = innerPadding,
                        navController = navController
                    )
                }
                composable(Screen.VerificationRequest.route) {
                    VerificationRequestScreen(walletViewModel, navController)
                }
                composable(Screen.VerificationIdentityDetails.route) {
                    VerificationIdentityDetailsScreen(walletViewModel, navController)
                }
                composable(Screen.VerificationDone.route) {
                    VerificationDoneScreen(navController)
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = BottomNavItem.entries
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentDestination?.route == item.route,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    }

    enum class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
        Wallet("wallet", "Wallet", Icons.Default.Home),
        Credential("credential", "Credential", Icons.Default.Lock),
        Verification("verification", "Verification", Icons.Default.CheckCircle)
    }

    enum class Screen(val route: String) {
        VerificationRequest("verification_request"),
        VerificationIdentityDetails("verification_processing"),
        VerificationDone("verification_done");
    }
}