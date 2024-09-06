package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CredentialInfoOfferTest {

    @Test
    fun initialize_withNullValues() {
        val credentialInfoOffer = CredentialInfoOffer()
        assertNull(credentialInfoOffer.jsonld)
        assertNull(credentialInfoOffer.attributes)
    }

    @Test
    fun initialize_withJsonLdOnly() {
        val jsonld: JsonElement = JsonPrimitive("Sample JSON-LD")
        val credentialInfoOffer = CredentialInfoOffer(jsonld = jsonld)

        assertEquals(jsonld, credentialInfoOffer.jsonld)
        assertNull(credentialInfoOffer.attributes)
    }

    @Test
    fun initialize_withMapAttributes() {
        val attributes: Map<String, JsonElement> = mapOf(
            "attribute1" to JsonPrimitive("value1"),
            "attribute2" to JsonPrimitive("value2")
        )
        val credentialInfoOffer = CredentialInfoOffer(attributes = attributes)

        assertNull(credentialInfoOffer.jsonld)
        assertEquals(attributes, credentialInfoOffer.attributes)
    }

    @Test
    fun initialize() {
        val jsonld: JsonElement = JsonPrimitive("Sample JSON-LD")
        val attributes: Map<String, JsonElement> = mapOf(
            "attribute1" to JsonPrimitive("value1"),
            "attribute2" to JsonPrimitive("value2")
        )
        val credentialInfoOffer = CredentialInfoOffer(jsonld = jsonld, attributes = attributes)

        assertEquals(jsonld, credentialInfoOffer.jsonld)
        assertEquals(attributes, credentialInfoOffer.attributes)
    }

    @Test
    fun initialize_withEmptyAttributes() {
        val attributes: Map<String, JsonElement> = emptyMap()
        val credentialInfoOffer = CredentialInfoOffer(attributes = attributes)

        assertNull(credentialInfoOffer.jsonld)
        assertEquals(attributes, credentialInfoOffer.attributes)
    }

    @Test
    fun initialize_withNestedJsonElements() {
        val jsonld: JsonElement = JsonPrimitive("Sample JSON-LD")
        val nestedJson: JsonObject = buildJsonObject {
            put("nestedKey", JsonPrimitive("nestedValue"))
        }
        val attributes: Map<String, JsonElement> = mapOf(
            "attribute1" to nestedJson
        )
        val credentialInfoOffer = CredentialInfoOffer(jsonld = jsonld, attributes = attributes)

        assertEquals(jsonld, credentialInfoOffer.jsonld)
        assertEquals(nestedJson, credentialInfoOffer.attributes?.get("attribute1"))
    }
}
