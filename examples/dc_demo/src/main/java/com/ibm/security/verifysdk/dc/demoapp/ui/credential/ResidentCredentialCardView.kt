/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */
package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
class ResidentCredentialCardView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: ResidentCredentialCard
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Blue, Color.Red, Color.White),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    LabelValueInCard(
                        label = "Type",
                        value = "Resident card",
                        modifier = Modifier.padding(bottom = 8.dp),
                        textColor = Color.White
                    )

                    LabelValueInCard(
                        label = "First name",
                        value = credential.credentialSubject.givenName,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textColor = Color.White
                    )

                    LabelValueInCard(
                        label = "Last name",
                        value = credential.credentialSubject.familyName,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textColor = Color.White
                    )

                    LabelValueInCard(
                        label = "Birth country",
                        value = credential.credentialSubject.birthCountry,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textColor = Color.White
                    )

                    val parsed = ZonedDateTime.parse(credential.issuanceDate)
                    val localTime = parsed.withZoneSameInstant(ZoneId.systemDefault())
                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a")
                    val readable = localTime.format(formatter)

                    LabelValueInCard(
                        label = "Issued",
                        value = readable,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textColor = Color.White
                    )
                }
            }
        }
    }
}

internal typealias ResidentCredentialCard = ResidentCredentialOffer
