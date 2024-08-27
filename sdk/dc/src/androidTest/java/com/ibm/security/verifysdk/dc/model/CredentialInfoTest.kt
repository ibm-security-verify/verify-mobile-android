package com.ibm.security.verifysdk.dc.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSerializationApi::class)
@RunWith(AndroidJUnit4::class)
class CredentialInfoTest {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
        ignoreUnknownKeys = false
        isLenient = true
    }

    @Test
    fun serialize_to_JSON() {
        val credentialInfo = CredentialInfo(
            id = "123",
            role = CredentialRole.ISSUER,
            state = CredentialState.ISSUED,
            issuerDid = "did:sov:123",
            format = "jsonld",
            connection = ConnectionInfo(
                id = "connId",
                state = "active",
                role = ConnectionRole.INVITER,
                local = Endpoint(name = "local", url = "https://localhost", pairwise = DidInfo(did = "did1", verkey = "verkey1"), ext = true),
                didExchange = true
            )
        )

        val jsonString = json.encodeToString(credentialInfo)
        val expectedJson = """
                {
                    "id":"123",
                    "role":"issuer",
                    "state":"issued",
                    "issuer_did":"did:sov:123",
                    "format":"jsonld",
                    "connection":{
                        "id":"connId",
                        "state":"active",
                        "role":"inviter",
                        "local":{
                            "name":"local",
                            "url":"https://localhost",
                            "pairwise":{
                                "did":"did1",
                                "verkey":"verkey1"
                            },
                            "ext":true
                        },
                        "did_exchange":true
                    }
                }
            """.trimIndent().replace("\n", "").replace(" ", "")
        assertEquals(expectedJson,jsonString)
    }

    @Test
    fun deserialize_from_JSON() {
        val jsonString = """
                {
                    "id":"123",
                    "role":"issuer",
                    "state":"issued",
                    "issuer_did":"did:sov:123",
                    "format":"jsonld",
                    "connection":{
                        "id":"connId",
                        "state":"active",
                        "role":"inviter",
                        "local":{
                            "name":"local",
                            "url":"https://localhost",
                            "pairwise":{
                                "did":"did1",
                                "verkey":"verkey1"
                            },
                            "ext":true
                        },
                        "did_exchange":true
                    }
                }
          """.trimIndent().replace("\n", "").replace(" ", "")

        val credentialInfo = json.decodeFromString<CredentialInfo>(jsonString)
        assertEquals("123", credentialInfo.id)
        assertEquals(CredentialRole.ISSUER, credentialInfo.role)
        assertEquals(CredentialState.ISSUED, credentialInfo.state)
        assertEquals("did:sov:123", credentialInfo.issuerDid)
        assertEquals("connId", credentialInfo.connection.id)
        assertEquals("active", credentialInfo.connection.state)
    }

    @Test
    fun serialize_with_optional_fields() {
        val jsonElement: JsonElement = buildJsonObject {
            put("key", "value")
        }

        val credentialInfo = CredentialInfo(
            id = "123",
            role = CredentialRole.ISSUER,
            state = CredentialState.ISSUED,
            issuerDid = "did:sov:123",
            format = "jsonld",
            connection = ConnectionInfo(
                id = "connId",
                state = "active",
                role = ConnectionRole.INVITER,
                local = Endpoint(name = "local", url = "https://localhost", pairwise = DidInfo(did = "did1", verkey = "verkey1"), ext = true),
                didExchange = true
            ),
            schemaName = "Test Schema",
            schemaVersion = "1.0",
            schemaId = "schema123",
            credDefId = "credDef123",
            credJson = jsonElement,
            timestamps = mapOf("created" to jsonElement)
        )

        val jsonString = json.encodeToString(credentialInfo)
        assert(jsonString.contains("\"schema_name\":\"Test Schema\""))
        assert(jsonString.contains("\"cred_def_id\":\"credDef123\""))
    }

    @Test
    fun deserialize_with_null_values() {
        val jsonString = """
                {
                    "id":"123",
                    "role":"issuer",
                    "state":"issued",
                    "issuer_did":"did:sov:123",
                    "format":"jsonld",
                    "connection":{
                        "id":"connId",
                        "state":"active",
                        "role":"inviter",
                        "local":{
                            "name":"local",
                            "url":"https://localhost",
                            "pairwise":{
                                "did":"did1",
                                "verkey":"verkey1"
                            },
                            "ext":true
                        },
                        "did_exchange":true
                    },
                "schema_name": null,
                "schema_version": null,
                "schema_id": null,
                "cred_def_id": null,
                "cred_json": null,
                "timestamps": null
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        val credentialInfo = json.decodeFromString<CredentialInfo>(jsonString)
        assertNull(credentialInfo.schemaName)
        assertNull(credentialInfo.schemaVersion)
        assertNull(credentialInfo.schemaId)
        assertNull(credentialInfo.credDefId)
        assertNull(credentialInfo.credJson)
        assertNull(credentialInfo.timestamps)
    }
}
