package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ibm.security.verifysdk.testutils.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FilterTest {

    @Test
    fun initialize_withRequiredFieldsOnly() {
        val filter = Filter(type = "string")
        val jsonString = json.encodeToString(filter)
        val deserializedFilter = json.decodeFromString<Filter>(jsonString)

        assertEquals(filter, deserializedFilter)
        assertEquals("""{"type":"string"}""", jsonString)
    }

    @Test
    fun initialize_withAllFields() {
        val filter = Filter(
            type = "number",
            `const` = JsonPrimitive(100),
            `enum` = listOf(JsonPrimitive(1), JsonPrimitive(2), JsonPrimitive(3)),
            exclusiveMinimum = JsonPrimitive(5),
            exclusiveMaximum = JsonPrimitive(100),
            format = "float",
            minLength = 5.0,
            maxLength = 10.0,
            minimum = JsonPrimitive(1),
            maximum = JsonPrimitive(200),
            not = JsonPrimitive("notAllowed"),
            pattern = "^[a-z]+$"
        )

        val jsonString = json.encodeToString(filter)
        val deserializedFilter = json.decodeFromString<Filter>(jsonString)

        assertEquals(filter, deserializedFilter)
        assertEquals(
            """{"type":"number","const":100,"enum":[1,2,3],"exclusiveMinimum":5,"exclusiveMaximum":100,"format":"float","minLength":5.0,"maxLength":10.0,"minimum":1,"maximum":200,"not":"notAllowed","pattern":"^[a-z]+$"}""",
            jsonString
        )

        assertEquals("number", filter.type)
        assertEquals(JsonPrimitive(100), filter.const)
        assertEquals(listOf(JsonPrimitive(1), JsonPrimitive(2), JsonPrimitive(3)), filter.enum)
        assertEquals(JsonPrimitive(5), filter.exclusiveMinimum)
        assertEquals(JsonPrimitive(100), filter.exclusiveMaximum)
        assertEquals("float", filter.format)
        assertEquals(5.0, filter.minLength)
        assertEquals(10.0, filter.maxLength)
        assertEquals(JsonPrimitive(1), filter.minimum)
        assertEquals(JsonPrimitive(200), filter.maximum)
        assertEquals(JsonPrimitive("notAllowed"), filter.not)
        assertEquals( "^[a-z]+$", filter.pattern)
    }

    @Test
    fun initialize_withNullValues() {
        val filter = Filter(
            type = "string",
            `const` = null,
            `enum` = null,
            exclusiveMinimum = null,
            exclusiveMaximum = null,
            format = null,
            minLength = null,
            maxLength = null,
            minimum = null,
            maximum = null,
            not = null,
            pattern = null
        )

        val jsonString = json.encodeToString(filter)
        val deserializedFilter = json.decodeFromString<Filter>(jsonString)

        assertEquals(filter, deserializedFilter)
        assertEquals("""{"type":"string"}""", jsonString)
    }

    @Test(expected = kotlinx.serialization.SerializationException::class)
    fun initialize_withInvalidFields() {
        val invalidJson = """{"type":"string","enum":"invalid"}"""
        json.decodeFromString<Filter>(invalidJson)
    }

    @Test
    fun initialize_withMinMaxValues() {
        val filter = Filter(
            type = "number",
            minimum = JsonPrimitive(10),
            maximum = JsonPrimitive(100)
        )

        val jsonString = json.encodeToString(filter)
        val deserializedFilter = json.decodeFromString<Filter>(jsonString)

        assertEquals(filter, deserializedFilter)
        assertEquals("""{"type":"number","minimum":10,"maximum":100}""", jsonString)
    }

    @Test
    fun equal() {
        val filter1 = Filter(type = "string", format = "date")
        val filter2 = Filter(type = "string", format = "date")

        assertEquals(filter1, filter2)
    }

    @Test
    fun notEqual() {
        val filter1 = Filter(type = "string", format = "date")
        val filter2 = Filter(type = "number", format = "float")

        assertNotEquals(filter1, filter2)
    }

    @Test(expected = kotlinx.serialization.SerializationException::class)
    fun initialize_withoutRequiredField() {
        val invalidJson = """{"format":"date"}"""
        json.decodeFromString<Filter>(invalidJson)
    }

    @Test(expected = kotlinx.serialization.SerializationException::class)
    fun initialize_withInvalidValue() {
        val invalidJson = """{"type":"string","minLength":"invalid"}"""
        json.decodeFromString<Filter>(invalidJson)
    }

    @Test
    fun serialize_deserialize() {
        val filter = Filter(type = "number")
        val jsonString = json.encodeToString(filter)
        val deserializedFilter = json.decodeFromString<Filter>(jsonString)

        assertEquals(filter, deserializedFilter)
    }
}