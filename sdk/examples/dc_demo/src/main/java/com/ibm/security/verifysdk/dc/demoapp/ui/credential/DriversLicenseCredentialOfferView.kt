/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

@file:OptIn(ExperimentalSerializationApi::class)

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class DriversLicenseCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: DriversLicenseCredentialOffer
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "Drivers License",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Family name",
                value = credential.familyName,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Given name",
                value = credential.givenName,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Issue date",
                value = credential.issueDate,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Issuer",
                value = credential.issuer,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Document number",
                value = credential.documentNumber,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Serializable
private data class DriversLicenseCredentialOffer(
    @JsonNames("given_name")
    val givenName: String,
    @JsonNames("family_name")
    val familyName: String,
    @JsonNames("issue_date")
    val issueDate: String,
    @JsonNames("issuing_authority")
    val issuer: String,
    @JsonNames("document_number")
    val documentNumber: String
)
