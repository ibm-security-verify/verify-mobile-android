/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class MedicareCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credentialOffer: MedicareCredentialOffer
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {
        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "Medicare card",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Card number",
                value = credentialOffer.number,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Valid to",
                value = credentialOffer.validTo,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Holders", style = MaterialTheme.typography.bodyLarge)
            LazyColumn {
                itemsIndexed(credentialOffer.holders) { _, holder ->
                    LabelValueRow(
                        label = holder.irn,
                        value = "${holder.givenName} ${holder.surname}",
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Serializable
private data class MedicareCredentialOffer(
    val number: String,
    @JsonNames("valid_to")
    val validTo: String,
    val holders: List<MedicareHolder>
)

@Serializable
internal data class MedicareHolder(
    val irn: String,
    val surname: String,
    @JsonNames("given_name")
    val givenName: String
)