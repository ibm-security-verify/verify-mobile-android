/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.ExperimentalDigitalCredentialsSdk

@Composable
fun AgentDialog(
    onServerChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: (server: String, nickname: String, user: String, clientId: String, secret: String) -> Unit,
    onScanQRCode: () -> Unit
) {
    var server by remember { mutableStateOf("iviadcgw-default.ivia-dc-demo-560b083b6ae574bb5eb8ef2f0de647f7-0000.au-syd.containers.appdomain.cloud") }
    var nickname by remember { mutableStateOf("holder_1") }
    var user by remember { mutableStateOf("user_1") }
    var clientId by remember { mutableStateOf("onpremise_vcholders") }
    var secret by remember { mutableStateOf("secret") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Wallet") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = server,
                    onValueChange = onServerChanged,
                    label = { Text("Server") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("User") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = clientId,
                    onValueChange = { clientId = it },
                    label = { Text("Client ID") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = secret,
                    onValueChange = { secret = it },
                    label = { Text("Secret") },
                    singleLine = true
                )
                // Right-aligned Scan QR Code button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "QR code scanning is not yet supported.",
                                Toast.LENGTH_SHORT
                            ).apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }
                    ) {
                        Text("Scan QR Code")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    onSubmit(server, nickname, user, clientId, secret)
                }) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun AddUrlDialog(
    showDialog: Boolean,
    type: String,
    url: String,
    onUrlChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    onScanQrClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add $type") },
            text = {
                Column {
                    OutlinedTextField(
                        value = url,
                        onValueChange = onUrlChange,
                        label = { Text("$type URL") }
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = onScanQrClick) {
                            Text("Scan QR Code")
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = onSubmit) { Text("Submit") } },
            dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
        )
    }
}

@Composable
fun StatusDialog(
    showDialog: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
        )
    }
}