/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.WalletViewModel
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel,
    innerPadding: PaddingValues,
    onCredentialsLoaded: (List<CredentialDescriptor>) -> Unit,
    onVerificationsLoaded: (List<VerificationInfo>) -> Unit,
    getWallet: () -> Unit
) {
    val wallets by viewModel.allWallets.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val wallet = wallets.firstOrNull()

    LaunchedEffect(wallet?.wallet?.credentials) {
        wallet?.wallet?.credentials?.let(onCredentialsLoaded)
        wallet?.wallet?.verifications?.let(onVerificationsLoaded)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wallet",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    wallet?.let {
                        IconButton(onClick = {
                            coroutineScope.launch { viewModel.delete(it) }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Wallet",
                                modifier = Modifier.size(48.dp))
                        }
                    } ?: IconButton(onClick = { getWallet() }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Wallet",
                            modifier = Modifier.size(48.dp))
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(32.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Agent",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
            )

            Spacer(modifier = Modifier.height(24.dp))

            wallet?.let { walletData ->
                WalletDetails(walletData)
            } ?: Text(
                text = "No wallet available. Please add a wallet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}