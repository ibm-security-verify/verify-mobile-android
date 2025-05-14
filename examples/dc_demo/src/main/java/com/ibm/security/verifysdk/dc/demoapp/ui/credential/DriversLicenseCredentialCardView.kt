/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */


package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import android.graphics.BitmapFactory
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class DriversLicenseCredentialCardView(override var jsonRepresentation: JsonElement) :
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
            ?.value?.jsonArray?.map {
                it.jsonPrimitive.int.toByte()
            }?.toByteArray()

        val authority = attributes.find { it.id == "issuing_authority" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'issuing _authority' in payload")

        val documentNumber = attributes.find { it.id == "document_number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'document_number' in payload")

        val drivingPrivileges = attributes.find { it.id == "driving_privileges" }
            ?.value?.jsonArray?.map { json.decodeFromJsonElement<DrivingPrivilege>(it) }
            ?: throw IllegalArgumentException("Missing 'driving_privileges' in payload")

        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .aspectRatio(1.585f), // Standard aspect ratio for a DL
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600)), // QLD Yellow
            elevation = CardDefaults.elevatedCardElevation(8.dp) // Elevation effect
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = documentNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                )

                Column(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "Drivers License",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
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
                        label = "Vehicle Class",
                        value = drivingPrivileges.first().vehicleCategoryCode
                    )
                    LabelValueInCard(
                        label = "Issue Date",
                        value = drivingPrivileges.first().issueDate
                    )
                    LabelValueInCard(
                        label = "Expiry Date",
                        value = drivingPrivileges.first().expiryDate
                    )
                }


                portrait?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
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

                Text(
                    text = authority,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
private data class DrivingPrivilege(
    @JsonNames("vehicle_category_code")
    val vehicleCategoryCode: String,
    @JsonNames("issue_date")
    val issueDate: String,
    @JsonNames("expiry_date")
    val expiryDate: String,
)