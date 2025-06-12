/*
 * Copyright contributors to the IBM Verify Digital Credentials Sample App for Android project
 */

package com.ibm.security.verifysdk.dc.demoapp.ui.credential

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ibm.security.verifysdk.dc.demoapp.ui.ViewDescriptor
import com.ibm.security.verifysdk.dc.demoapp.ui.verification.LabelValueRow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
class IbmEmployeeCredentialOfferView(override var jsonRepresentation: JsonElement) : ViewDescriptor() {

    private val credential: IbmEmployeeCredential
        get() = json.decodeFromJsonElement(jsonRepresentation)

    @Composable
    override fun ShowCredential(modifier: Modifier) {
        Column(modifier = modifier) {
            LabelValueRow(
                label = "Type",
                value = "IBM Employee card",
                textStyle = MaterialTheme.typography.bodyLarge
            )
            LabelValueRow(
                label = "Name",
                value = credential.familyName,
                textStyle = MaterialTheme.typography.bodyLarge)
            LabelValueRow(
                label = "Given name",
                value = credential.givenName,
                textStyle = MaterialTheme.typography.bodyLarge)
            LabelValueRow(
                label = "Job title",
                value = credential.jobTitle,
                textStyle = MaterialTheme.typography.bodyLarge)
            LabelValueRow(
                label = "Email address",
                value = credential.emailAddress,
                textStyle = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Serializable
internal data class IbmEmployeeCredential(
    @SerialName("family_name")
    val familyName: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("portrait")
    val portrait: String,
    @SerialName("birth_date")
    val birthDate: String,
    @SerialName("document_number")
    val documentNumber: String,
    @SerialName("company_name")
    val companyName: String,
    @SerialName("job_title")
    val jobTitle: String,
    @SerialName("hire_date")
    val hireDate: String,
    @SerialName("base_salary")
    val baseSalary: String,
    @SerialName("email_address")
    val emailAddress: String,
)