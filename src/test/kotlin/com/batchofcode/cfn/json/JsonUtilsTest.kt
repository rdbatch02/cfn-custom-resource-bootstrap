package com.batchofcode.cfn.json

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonUtilsTest {
    @Test
    fun `converts map to json object`() {
        val map = mapOf(
            "stringKey" to "stringVal",
            "intKey" to 42,
            "boolKey" to true
        )
        val expectedObject = JsonObject(mapOf(
            "stringKey" to JsonPrimitive("stringVal"),
            "intKey" to JsonPrimitive(42),
            "boolKey" to JsonPrimitive(true)
        ))
        val convertedObject = map.toJsonObject()
        assertEquals(expectedObject, convertedObject)
    }
}