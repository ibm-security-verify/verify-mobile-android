package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
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
}