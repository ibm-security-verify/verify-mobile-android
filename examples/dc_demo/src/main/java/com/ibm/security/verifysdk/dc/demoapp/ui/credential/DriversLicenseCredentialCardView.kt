/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */


package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'portrait' in payload")

        val authority = attributes.find { it.id == "issuing_authority" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'issuing_authority' in payload")

        val documentNumber = attributes.find { it.id == "document_number" }
            ?.value?.jsonPrimitive?.content
            ?: throw IllegalArgumentException("Missing 'document_number' in payload")

        val drivingPrivileges = attributes.find { it.id == "driving_privileges" }
            ?.value?.jsonArray?.map { json.decodeFromJsonElement<DrivingPrivilege>(it) }
            ?: throw IllegalArgumentException("Missing 'driving_privileges' in payload")

        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600)),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val isHorizontal = maxWidth > maxHeight
                val base64Data = portrait.substringAfter("base64,")
                val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                val bitmap =
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()

                if (isHorizontal) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(end = 8.dp)
                        ) {
                            HeaderTexts(firstName, lastName)
                            Spacer(modifier = Modifier.height(8.dp))
                            LabelValueInCard("DoB", birthDate)
                            Spacer(modifier = Modifier.height(8.dp))
                            LabelValueInCard(
                                "Vehicle Class",
                                drivingPrivileges.first().vehicleCategoryCode
                            )
                            LabelValueInCard("Issue Date", drivingPrivileges.first().issueDate)
                            LabelValueInCard("Expiry Date", drivingPrivileges.first().expiryDate)
                            Spacer(modifier = Modifier.height(24.dp)) // Reserve space for authority
                        }
                        PortraitBox(bitmap = bitmap)
                    }

                    Text(
                        text = authority,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    )

                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = documentNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.End)
                        )

                        Box {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 80.dp)
                            ) {
                                HeaderTexts(firstName, lastName)
                                Spacer(modifier = Modifier.height(8.dp))
                                LabelValueInCard("DoB", birthDate)
                                Spacer(modifier = Modifier.height(8.dp))
                                LabelValueInCard(
                                    "Vehicle Class",
                                    drivingPrivileges.first().vehicleCategoryCode
                                )
                                LabelValueInCard("Issue Date", drivingPrivileges.first().issueDate)
                                LabelValueInCard(
                                    "Expiry Date",
                                    drivingPrivileges.first().expiryDate
                                )
                            }
                            PortraitBox(
                                bitmap = bitmap,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }

                        Text(
                            text = authority,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderTexts(firstName: String, lastName: String, color : Color = Color.Black) {
    Text(
        text = "Drivers License",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "$firstName $lastName",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = color,
    )
}

@Composable
fun LabelValueInCard(label: String, value: String, color: Color = Color.Black) {
    Row(
        verticalAlignment = Alignment.Top, // Top align for better wrapping
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = color,
            modifier = Modifier.weight(1f),
            softWrap = true,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
fun PortraitBox(bitmap: ImageBitmap?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(70.dp, 110.dp)
            .background(Color.Gray, RoundedCornerShape(4.dp))
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Portrait",
                modifier = Modifier.matchParentSize()
            )
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