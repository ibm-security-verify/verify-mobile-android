/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.verification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibm.security.verifysdk.dc.model.VerificationInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSerializationApi::class)
@Composable
fun VerificationListItem(
    verification: VerificationInfo,
    navigator: ThreePaneScaffoldNavigator<Any>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    content = Json.encodeToString(
                        VerificationInfo.serializer(),
                        verification
                    )
                )
            }
    ) {
        VerificationDetailsDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Done,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp),
                contentDescription = "Credential verified"
            )

            Column(modifier = Modifier.weight(1f)) {
                LabelValueRow(
                    "Name",
                    verification.proofRequest?.mdoc?.presentationDefinition?.inputDescriptors?.first()?.name
                        ?: "unknown"
                )
                LabelValueRow(
                    "Purpose",
                    verification.proofRequest?.mdoc?.presentationDefinition?.inputDescriptors?.first()?.purpose
                        ?: "unknown"
                )

                verification.info?.jsonObject?.get("validityInfo")?.jsonObject?.get("signed")?.jsonPrimitive?.content?.let {
                    val parsed = ZonedDateTime.parse(it)
                    val localTime = parsed.withZoneSameInstant(ZoneId.systemDefault())
                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a")
                    val readable = localTime.format(formatter)
                    LabelValueRow("Signed on", readable)
                }
            }
        }
    }
}

@Composable
fun LabelValueRow(label: String, value: String,
                  textStyle: TextStyle = MaterialTheme.typography.titleLarge,
                  valueTextSizeReduction: Int = 0 ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val reducedFontSize = textStyle.fontSize.value - valueTextSizeReduction

        Text(
            text = "$label:",
            style = textStyle.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = textStyle.copy(fontWeight = FontWeight.Medium, fontSize = reducedFontSize.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}