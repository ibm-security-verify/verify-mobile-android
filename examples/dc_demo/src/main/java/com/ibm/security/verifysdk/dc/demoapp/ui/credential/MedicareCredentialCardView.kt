/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class MedicareCredentialCardView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: List<CredentialAttribute>
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        val number = credential.find { it.id == "number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'number' in payload")

        val validTo = credential.find { it.id == "valid_to" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'valid_to' in payload")

        val attributeHolders = credential.find { it.id == "holders" }
            ?.value?.jsonArray?.map { json.decodeFromJsonElement<MedicareHolder>(it) }
            ?: throw IllegalArgumentException("Missing 'holders' in payload")

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF127C33)), // Medicare Green
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .aspectRatio(1.585f) // Approximate ratio of Medicare card (85.6mm x 54mm)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Medicare text (Top Right)
                Text(
                    text = "Medicare",
                    color = Color.Yellow,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.align(Alignment.TopEnd)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 36.dp)
                ) {
                    Text(
                        text = number.chunked(4).joinToString(" "),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFeatureSettings = "tnum"
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 32.dp) // Add bottom padding to match gap to "valid to"
                ) {
                    attributeHolders.forEach { holder ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${holder.irn}   ${holder.givenName} ${holder.surname}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontFeatureSettings = "tnum"
                                ),
                                modifier = Modifier.weight(1f) // Ensure this is stretched
                            )
                        }
                    }
                }
                Text(
                    text = "Valid to: $validTo",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Serializable
internal data class CredentialAttribute(
    val ns: String,
    val id: String,
    val value: JsonElement,
    val isValid: Boolean
)
