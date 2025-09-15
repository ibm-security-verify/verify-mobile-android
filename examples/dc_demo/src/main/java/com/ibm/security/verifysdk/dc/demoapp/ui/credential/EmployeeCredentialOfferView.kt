/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class EmployeeCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: EmployeeCredential
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {

        val labelWidth = 100.dp

        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "Employee card",
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth
            )
            LabelValueRow(
                label = "Job title",
                value = credential.attributes.firstOrNull() { it.name == "JobTitle" }
                    ?.value ?: "N/A",
                textStyle = MaterialTheme.typography.bodyLarge,
                labelWidth = labelWidth)
        }
    }
}

@Serializable
internal data class EmployeeCredential(
    @SerialName("@type")
    val type: String,
    val attributes: List<EmployeeCredentialAttribute>
)

@Serializable
internal data class EmployeeCredentialAttribute(
    val name: String,
    val value: String
)