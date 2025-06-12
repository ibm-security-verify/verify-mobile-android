/*
 * Copyright contributors to the IBM Verify Digital Credentials SDK for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class BankingCredentialCardView(override var jsonRepresentation: JsonElement) :
    ViewDescriptor() {

    @Serializable
    internal data class CredentialAttribute(
        val ns: String,
        val id: String,
        val value: JsonElement,
        val isValid: Boolean
    )

    private val attributes: List<CredentialAttribute>
        get() = json.decodeFromJsonElement<List<CredentialAttribute>>(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        val firstName = attributes.find { it.id == "given_name" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'given_name' in payload")

        val middleName = attributes.find { it.id == "middle_name" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'middle_name' in payload")

        val lastName = attributes.find { it.id == "family_name" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'family_name' in payload")

        val birthDate = attributes.find { it.id == "birth_date" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'birth_date' in payload")

        val accountNumber = attributes.find { it.id == "account_number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'account_number' in payload")

        val emailAddress = attributes.find { it.id == "email_address" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'email_address' in payload")

        val institutionNumber = attributes.find { it.id == "institution_number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'institution_number' in payload")

        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 360f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 15000, easing = LinearEasing), // 15 seconds per rotation
                repeatMode = RepeatMode.Restart
            )
        )

        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .aspectRatio(1.585f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF004D40)), // Financial tone
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 💲 Background dollar sign
                Text(
                    text = "$",
                    fontSize = 160.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White.copy(alpha = 0.05f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer {
                            rotationY = rotation
                        }
                )

                // Account number at the top-right
                Text(
                    text = accountNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.TopEnd)
                )

                // Main content
                Column(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "Bank Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$firstName $middleName $lastName".replace("  ", " ").trim(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LabelValueInCard(label = "Date of Birth", value = birthDate, textColor = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValueInCard(label = "Email", value = emailAddress, textColor = Color.White)
                }

                // Bottom right label
                Text(
                    text = "Financial Authority: $institutionNumber",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}