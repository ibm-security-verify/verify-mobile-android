package com.ibm.security.verifysdk.dc.cloud.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConstraintsTest {

    @Test
    fun serializeDeserialize_withoutAnyValues() {
        val constraints = Constraints()

        val jsonString = json.encodeToString(constraints)
        val deserializedConstraints = json.decodeFromString<Constraints>(jsonString)

        assertEquals(constraints, deserializedConstraints)
        assertEquals("""{}""", jsonString)
    }

    @Test
    fun initialize_withAllFields() {

        val expectedJson = """{
               "limit_disclosure":"required",
               "statuses":{
                  
               },
               "fields":[
                  {
                     "path":[
                        "field1"
                     ]
                  },
                  {
                     "path":[
                        "field2"
                     ]
                  }
               ],
               "subject_is_issuer":"preferred",
               "is_holder":[
                  {
                     "field_id":[
                        "holder1"
                     ],
                     "directive":"required"
                  }
               ],
               "same_subject":[
                  {
                     "field_id":[
                        "holder2"
                     ],
                     "directive":"preferred"
                  }
               ]
            }""".trimIndent().replace("\n", "").replace(" ", "")

        val constraints = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            statuses = Statuses(),
            fields = listOf(Field(listOf("field1")), Field(listOf("field2"))),
            subjectIsIssuer = Optionality.PREFERRED,
            isHolder = listOf(HolderSubject(fieldId = listOf("holder1"), directive = Optionality.REQUIRED)),
            sameSubject = listOf(HolderSubject(fieldId = listOf("holder2"), directive = Optionality.PREFERRED))
        )

        val jsonString = json.encodeToString(constraints)
        val deserializedConstraints = json.decodeFromString<Constraints>(jsonString)

        assertEquals(constraints, deserializedConstraints)
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun initialize_withEmptyLists() {
        val constraints = Constraints(
            fields = emptyList(),
            isHolder = emptyList(),
            sameSubject = emptyList()
        )

        val jsonString = json.encodeToString(constraints)
        val deserializedConstraints = json.decodeFromString<Constraints>(jsonString)

        assertEquals(constraints, deserializedConstraints)
        assertEquals("""{"fields":[],"is_holder":[],"same_subject":[]}""", jsonString)
    }

    @Test
    fun initialize_withLimitDisclosureAndSubjectIssuer() {

        val expectedJson = """{"limit_disclosure":"required","subject_is_issuer":"preferred"}"""

        val constraints = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            subjectIsIssuer = Optionality.PREFERRED
        )

        val jsonString = json.encodeToString(constraints)
        val deserializedConstraints = json.decodeFromString<Constraints>(jsonString)

        assertEquals(constraints, deserializedConstraints)
        assertEquals(expectedJson, jsonString)
    }

    @Test
    fun equal() {
        val constraints1 = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            statuses = Statuses(),
            fields = listOf(Field(listOf("field1")), Field(listOf("field2"))),
            subjectIsIssuer = Optionality.REQUIRED,
            isHolder = listOf(HolderSubject(fieldId = listOf("holder1"), directive = Optionality.REQUIRED)),
            sameSubject = listOf(HolderSubject(fieldId = listOf("holder2"), directive = Optionality.PREFERRED))
        )

        val constraints2 = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            statuses = Statuses(),
            fields = listOf(Field(listOf("field1")), Field(listOf("field2"))),
            subjectIsIssuer = Optionality.REQUIRED,
            isHolder = listOf(HolderSubject(fieldId = listOf("holder1"), directive = Optionality.REQUIRED)),
            sameSubject = listOf(HolderSubject(fieldId = listOf("holder2"), directive = Optionality.PREFERRED))
        )

        assertEquals(constraints1, constraints2)
    }

    @Test
    fun notEqual() {
        val constraints1 = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            fields = listOf(Field(listOf("field1"))),
            subjectIsIssuer = Optionality.REQUIRED
        )

        val constraints2 = Constraints(
            limitDisclosure = Optionality.PREFERRED,
            fields = listOf(Field(listOf("field2"))),
            subjectIsIssuer = Optionality.REQUIRED
        )

        assertNotEquals(constraints1, constraints2)
    }

    // Test case for missing required field (in this case, all fields are optional)
    @Test
    fun deserialize_withNoValues() {
        val jsonString = """{}"""
        val constraints = json.decodeFromString<Constraints>(jsonString)

        assertNull(constraints.limitDisclosure)
        assertNull(constraints.statuses)
        assertNull(constraints.fields)
        assertNull(constraints.subjectIsIssuer)
        assertNull(constraints.isHolder)
        assertNull(constraints.sameSubject)
    }

    // Test case for invalid data types (expect failure in deserialization)
    @Test(expected = kotlinx.serialization.SerializationException::class)
    fun deserialize_withInvalidData() {
        val invalidJson = """{"limit_disclosure": 123}"""
        json.decodeFromString<Constraints>(invalidJson)
    }

    // Test case for round-trip serialization and deserialization with populated values
    @Test
    fun serialize_deserialize() {
        val constraints = Constraints(
            limitDisclosure = Optionality.REQUIRED,
            fields = listOf(Field(listOf("field1"))),
        )

        val jsonString = json.encodeToString(constraints)
        val deserializedConstraints = json.decodeFromString<Constraints>(jsonString)

        assertEquals(constraints, deserializedConstraints)
    }
}