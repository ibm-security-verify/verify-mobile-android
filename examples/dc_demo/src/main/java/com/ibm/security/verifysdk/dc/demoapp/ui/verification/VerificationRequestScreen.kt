/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.ibm.security.verifysdk.dc.cloud.model.VerificationPreviewInfo
import com.ibm.security.verifysdk.dc.core.ExperimentalDigitalCredentialsSdk
import com.ibm.security.verifysdk.dc.demoapp.MainActivity.Screen
import com.ibm.security.verifysdk.dc.demoapp.data.WalletManager
import com.ibm.security.verifysdk.dc.demoapp.ui.StatusDialog
import com.ibm.security.verifysdk.dc.demoapp.ui.WalletViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDigitalCredentialsSdk::class)
@Composable
fun VerificationRequestScreen(walletViewModel: WalletViewModel, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorTitle by remember { mutableStateOf("") }

    val wallets by walletViewModel.allWallets.collectAsState()
    val wallet = wallets.firstOrNull()
    val walletManager = wallet?.let { WalletManager(it, walletViewModel) }

    val verificationPreviewJson = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("verificationPreview")

    val verificationPreviewInfo = verificationPreviewJson?.let {
        Json.decodeFromString<VerificationPreviewInfo>(it)
    }

    val company = verificationPreviewInfo?.label ?: "Unknown company"

    val name =
        verificationPreviewInfo?.jsonRepresentation?.jsonObject?.get("presentation_definition")
            ?.jsonObject?.get("input_descriptors")?.jsonArray?.get(0)?.jsonObject?.get("name")?.jsonPrimitive?.content
            ?: "Unknown name"

    val purpose =
        verificationPreviewInfo?.jsonRepresentation?.jsonObject?.get("presentation_definition")
            ?.jsonObject?.get("input_descriptors")?.jsonArray?.get(0)?.jsonObject?.get("purpose")?.jsonPrimitive?.content
            ?: "No purpose provided"

    val credential =
        verificationPreviewInfo?.jsonRepresentation?.jsonObject?.get("presentation_definition")
            ?.jsonObject?.get("input_descriptors")?.jsonArray?.get(0)?.jsonObject?.get("id")?.jsonPrimitive?.content
            ?: "No credential provided"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Verification Request",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LabelValueRow("Company", company)
            LabelValueRow("Name", name)
            LabelValueRow("Purpose", purpose)
            LabelValueRow("Credential", credential)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    buildAnnotatedString {
                        append("If you want to generate the requested identity details, tap ")
                        appendLine()
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        append("Preview Request Details")
                        pop()
                    },
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 1.5.em),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = {
                    coroutineScope.launch {
                        verificationPreviewInfo?.let {
                            walletManager?.previewVerification(it)
                                ?.onSuccess { verificationInfo ->
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.apply {
                                            set(
                                                "verificationInfo",
                                                Json.encodeToString(verificationInfo)
                                            )
                                            set(
                                                "verificationPreviewInfo",
                                                Json.encodeToString(verificationPreviewInfo)
                                            )
                                        }
                                    navController.navigate(Screen.VerificationIdentityDetails.route)
                                }
                                ?.onFailure { error ->
                                    errorTitle = "Error"
                                    errorMessage = "Failed to fetch data: ${error.message}"
                                    showErrorDialog = true
                                }
                        }
                    }

                }) {
                Text("Preview Request Details")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        StatusDialog(
            showErrorDialog,
            title = errorTitle,
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}