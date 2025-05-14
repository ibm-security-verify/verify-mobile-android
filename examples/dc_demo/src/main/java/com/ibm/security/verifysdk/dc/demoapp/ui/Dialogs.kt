/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AddUrlDialog(
    showDialog: Boolean,
    type: String,
    url: String,
    onUrlChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add $type") },
            text = {
                OutlinedTextField(
                    value = url,
                    onValueChange = onUrlChange,
                    label = { Text("$type URL") }
                )
            },
            confirmButton = { Button(onClick = onSubmit) { Text("Submit") } },
            dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
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