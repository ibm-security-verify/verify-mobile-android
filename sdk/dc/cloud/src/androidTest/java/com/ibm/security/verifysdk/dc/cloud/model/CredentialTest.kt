package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.dc.core.CredentialDescriptor
import com.ibm.security.verifysdk.dc.serializer.CredentialSerializer
import com.ibm.security.verifysdk.testutils.LogHelper
import com.ibm.security.verifysdk.testutils.json
import com.ibm.security.verifysdk.testutils.loadJsonFromRawResource
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CredentialTest() {

    @Test
    fun deserialize_indyCredential_happyPath_shouldReturnIndyCredential() {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_indy)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is IndyCredential)
    }

    @Test
    fun deserialize_mdocCredential_happyPath_shouldReturnMDocCredential() {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_mdoc)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is MDocCredential)
    }

    @Test
    fun deserialize_jsonLdCredential_happyPath_shouldReturnJsonLdCredential() {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_jsonld)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is JsonLdCredential)
    }

    @Test
    fun serialize_indyCredential_happyPath_shouldReturnJson()   {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_indy)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is IndyCredential)
        val credSerialized = json.encodeToJsonElement(CredentialSerializer, credential)
        LogHelper.largeLog(tag = "user_credential_indy", content = credSerialized.toString())
    }

    @Test
    fun serialize_mdocCredential_happyPath_shouldReturnJson()   {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_mdoc)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is MDocCredential)
        val credSerialized = json.encodeToJsonElement(CredentialSerializer, credential)
        LogHelper.largeLog(tag = "user_credential_mdoc", content = credSerialized.toString())
    }

    @Test
    fun serialize_jsonldCredential_happyPath_shouldReturnJson()   {

        val responseBody =
            loadJsonFromRawResource(com.ibm.security.verifysdk.dc.test.R.raw.user_credential_jsonld)

        val credential = json.decodeFromString<CredentialDescriptor>(responseBody.toString())
        assert(credential is JsonLdCredential)
        val credSerialized = json.encodeToJsonElement(CredentialSerializer, credential)
        LogHelper.largeLog(tag = "user_credential_jsonld", content = credSerialized.toString())
    }
}