/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class IbmEmployeeCredentialCardView(override var jsonRepresentation: JsonElement) :
    ViewDescriptor() {

    private val attributes: List<CredentialAttribute>
        get() = json.decodeFromJsonElement<List<CredentialAttribute>>(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        val firstName = attributes.find { it.id == "given_name" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'given_name' in payload")

        val lastName = attributes.find { it.id == "family_name" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'family_name' in payload")

        val birthDate = attributes.find { it.id == "birth_date" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'birth_date' in payload")

        val portrait = attributes.find { it.id == "portrait" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'portrait' in payload")

        val documentNumber = attributes.find { it.id == "document_number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'document_number' in payload")

        val jobTitle = attributes.find { it.id == "job_title" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'job_title' in payload")

        val hireDate = attributes.find { it.id == "hire_date" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'hire_date' in payload")

        val baseSalary = attributes.find { it.id == "job_title" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'job_title' in payload")

        val emailAddress = attributes.find { it.id == "email_address" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'email_address' in payload")


        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1565C0), Color(0xFF64B5F6))
                        )
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = "IBM Employee Card",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$firstName $lastName",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LabelValueInCard(
                            label = "DoB",
                            value = birthDate
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LabelValueInCard(
                            label = "Job Title",
                            value = jobTitle
                        )
                        LabelValueInCard(
                            label = "Hire Date",
                            value = hireDate
                        )
                        LabelValueInCard(
                            label = "Email address",
                            value = emailAddress
                        )
                    }

                    portrait.let { portrait ->
                        val base64Data = portrait.substringAfter("base64,")
                        val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            ?.asImageBitmap()
                        Box(
                            modifier = Modifier
                                .size(70.dp, 110.dp)
                                .align(Alignment.CenterEnd)
                                .background(Color.Gray, RoundedCornerShape(4.dp))
                        ) {
                            bitmap?.let { image ->
                                Image(
                                    bitmap = image,
                                    contentDescription = "Portrait",
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}