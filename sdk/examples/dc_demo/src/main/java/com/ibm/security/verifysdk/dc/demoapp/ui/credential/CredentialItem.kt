/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import com.ibm.security.verifysdk.dc.model.CredentialDescriptor
import com.ibm.security.verifysdk.dc.serializer.CredentialSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@OptIn(ExperimentalSerializationApi::class)
private val json = Json {
    encodeDefaults = true
    explicitNulls = false
    ignoreUnknownKeys = true
    isLenient = true
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CredentialListItem(
    credential: CredentialDescriptor,
    navigator: ThreePaneScaffoldNavigator<Any>
) {
    val itemView = remember(credential.jsonRepresentation) {
        credential.jsonRepresentation?.let { json.decodeFromJsonElement<ViewDescriptor>(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    content = Json.encodeToString(CredentialSerializer, credential)
                )
            }
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        itemView?.ShowCredential(modifier = Modifier)
    }
}

@Composable
fun CredentialInfo(label: String, value: String) {
    LabelValueRow(label = label, value = value, valueTextSizeReduction = 2)
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        thickness = 1.dp,
        color = Color.Gray
    )
}

@Composable
fun CredentialPreviewDialog(
    showDialog: Boolean,
    jsonRepresentation: JsonElement?,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    if (showDialog) {
        val previewData = json.decodeFromJsonElement<ViewDescriptor>(
            jsonRepresentation ?: buildJsonObject { })
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Credential Preview") },
            text = { previewData.ShowCredential(Modifier) },
            confirmButton = { Button(onClick = onAccept) { Text("Accept") } },
            dismissButton = { Button(onClick = onReject) { Text("Cancel") } }
        )
    }
}

@Composable
internal fun LabelValueInCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
            color = textColor
        )
    }
}