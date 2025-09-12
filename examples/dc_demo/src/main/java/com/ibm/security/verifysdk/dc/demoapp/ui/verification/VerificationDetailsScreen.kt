/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ibm.security.verifysdk.dc.demoapp.MainActivity.Screen
import com.ibm.security.verifysdk.dc.demoapp.data.WalletManager
import com.ibm.security.verifysdk.dc.demoapp.ui.StatusDialog
import com.ibm.security.verifysdk.dc.demoapp.ui.WalletViewModel
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import com.ibm.security.verifysdk.dc.model.VerificationPreviewInfo
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalMaterial3Api::class, ExperimentalDigitalCredentialsSdk::class)
@Composable
fun VerificationIdentityDetailsScreen(
    walletViewModel: WalletViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorTitle by remember { mutableStateOf("") }

    val wallets by walletViewModel.allWallets.collectAsState()
    val wallet = wallets.firstOrNull()
    val walletManager = wallet?.let { WalletManager(it, walletViewModel) }

    val verificationInfoJson = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("verificationInfo")
    val verificationInfo = verificationInfoJson?.let {
        Json.decodeFromString<VerificationInfo>(it)
    }

    val verificationPreviewInfoJson = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("verificationPreviewInfo")
    val verificationPreviewInfo = verificationPreviewInfoJson?.let {
        Json.decodeFromString<VerificationPreviewInfo>(it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Identity Details",
                        fontWeight = FontWeight.Bold
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

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                verificationInfo?.let {
                    ProofRequestPresentation(it)
                }

                Text(
                    buildAnnotatedString {
                        append("If you want to share identity details with ${verificationPreviewInfo?.label}, tap ")
                        appendLine()
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        append("Allow Verification")
                        pop()
                    },
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 1.5.em),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        verificationPreviewInfo?.let {
                            coroutineScope.launch {
                                walletManager?.processProofRequest(verificationPreviewInfo)
                                    ?.onSuccess { verificationInfo ->
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set(
                                                "label", verificationPreviewInfo.label
                                            )

                                        walletViewModel.updateVerification(
                                            walletManager.walletEntity,
                                            (walletManager.walletEntity.wallet.verifications + verificationInfo).toMutableList()
                                        )
                                        navController.navigate(Screen.VerificationDone.route)
                                    }
                                    ?.onFailure {
                                        errorTitle = "Error"
                                        errorMessage =
                                            "Something went wrong: ${it.message ?: "Unknown error"}"
                                        showErrorDialog = true
                                    }
                            }
                        }
                    }) {
                    Text("Allow Verification")
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
}

@Composable
private fun ProofRequestPresentation(
    verificationInfo: VerificationInfo,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 0.dp)
    ) {
        Column {
            verificationInfo.info?.jsonObject?.get("attributes")?.jsonArray?.forEach { attribute ->
                val obj = attribute.jsonObject
                when (val valueElement = obj["value"]) {
                    is JsonArray -> {
                        val values = obj["value"]?.jsonArray ?: return@forEach
                        values.forEach { value ->
                            val valueObj = value.jsonObject
                            valueObj.entries.forEach { (key, jsonElement) ->
                                LabelValueRow(
                                    label = key,
                                    value = jsonElement.jsonPrimitive.content,
                                )
                                VerificationDetailsDivider()
                            }
                        }
                    }

                    is JsonPrimitive -> {
                        val id = obj["id"]?.jsonPrimitive?.content ?: "value"
                        LabelValueRow(
                            label = id.split("_")
                                .joinToString(" ") { word ->
                                    word.lowercase().replaceFirstChar { it.uppercase() }
                                },
                            value = valueElement.content,
                        )
                        VerificationDetailsDivider()
                    }

                    else -> {
                        // Optionally handle other types (e.g., JsonObject or null)
                    }
                }
            }
        }
    }
}