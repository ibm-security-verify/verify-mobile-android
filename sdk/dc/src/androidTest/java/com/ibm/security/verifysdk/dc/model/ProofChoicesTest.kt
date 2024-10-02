package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProofChoicesTest {


    @Test
    fun initialize() {
        val attributes = mapOf(
            "attr1" to mapOf(
                "credential1" to AttrCredChoice(
                    listOf(
                        NameValue(
                            "someName1",
                            "someValue1"
                        )
                    ), "someSchema1", "someCredDefId1"
                )
            ),
            "attr2" to mapOf(
                "credential2" to AttrCredChoice(
                    listOf(
                        NameValue(
                            "someName2",
                            "someValue2"
                        )
                    ), "someSchema2", "someCredDefId2"
                )
            )
        )

        val predicates = mapOf(
            "pred1" to mapOf(
                "credential3" to PredCredChoicesValue(
                    "someCredDefId1",
                    "someSchemaId1",
                    "somePredicate1"
                )
            ),
            "pred2" to mapOf(
                "credential4" to PredCredChoicesValue(
                    "someCredDefId2",
                    "someSchemaId2",
                    "somePredicate2"
                )
            ),
        )

        val proofChoices = ProofChoices(attributes, predicates)
        val jsonString = json.encodeToString(ProofChoices.serializer(), proofChoices)
        val deserializedProofChoices = json.decodeFromString(ProofChoices.serializer(), jsonString)

        assertEquals(proofChoices, deserializedProofChoices)
    }

    @Test
    fun initialize_withEmptyMaps() {
        val proofChoices = ProofChoices(emptyMap(), emptyMap())
        val jsonString = json.encodeToString(ProofChoices.serializer(), proofChoices)
        val deserializedProofChoices = json.decodeFromString(ProofChoices.serializer(), jsonString)

        assertEquals(proofChoices, deserializedProofChoices)
    }

    @Test
    fun initialize_withNestedEmptyMaps() {
        val proofChoices = ProofChoices(
            attributes = mapOf("attributes" to emptyMap()),
            predicates = mapOf("predicates" to emptyMap())
        )
        val serialized = json.encodeToString(proofChoices)

        val expectedJson = """{"attributes":{"attributes":{}},"predicates":{"predicates":{}}}"""
        assertEquals(expectedJson, serialized)

        val deserialized = json.decodeFromString<ProofChoices>(serialized)
        assertTrue(deserialized.attributes["attributes"]?.isEmpty() == true)
        assertTrue(deserialized.predicates["predicates"]?.isEmpty() == true)
    }

    @Test
    fun initialize_withNullValues() {
        val proofChoices = ProofChoices(
            attributes = mapOf(
                "attr1" to mapOf(
                    "cred1" to AttrCredChoice(
                        listOf(
                            NameValue(
                                "someName1",
                                "someValue1"
                            )
                        ), "someSchema1", "someCredDefI"
                    )
                )
            ),
            predicates = mapOf()
        )

        val serialized = json.encodeToString(proofChoices)

        val expectedJson = """{
            "attributes": {
                "attr1": {"cred1": {"names": [{"name":"someName1", "value":"someValue1"}], "schema_id":"someSchema1", "cred_def_id":"someCredDefI"}
            }},
            "predicates": {}
        }""".trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson, serialized)
    }


    @Test
    fun deserialize_withInvalidString() {
        val invalidJson = """{
            "attributes": "invalidType",
            "predicates": {}
        }""".trimIndent().replace("\n", "").replace(" ", "")

        try {
            json.decodeFromString<ProofChoices>(invalidJson)
            fail("Expected a JsonDecodingException to be thrown due to invalid data")
        } catch (e: SerializationException) {
            // Test passes if a SerializationException is thrown
        } catch (e: Exception) {
            fail("Expected a SerializationException but got ${e::class.simpleName}")
        }
    }

    @Test
    fun deserialize_withEmptyAttributesAndPredicates() {
        val jsonString = """{
        "attributes": {},
        "predicates": {}
    }""".trimIndent()

        val deserialized = json.decodeFromString<ProofChoices>(jsonString)

        assertTrue(deserialized.attributes.isEmpty())
        assertTrue(deserialized.predicates.isEmpty())
    }
}