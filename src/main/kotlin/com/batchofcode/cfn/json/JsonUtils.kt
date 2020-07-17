package com.batchofcode.cfn.json

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun Map<String, Any>.toJsonObject(): JsonObject {
    return JsonObject(this.mapValues { it.toJsonElement() })
}

private fun Map.Entry<String, Any>.toJsonElement(): JsonElement {
    @Suppress("UNCHECKED_CAST")
    return when (this.value) {
        is String -> JsonPrimitive(this.value as String)
        is Number -> JsonPrimitive(this.value as Number)
        is Boolean -> JsonPrimitive(this.value as Boolean)
        is Map<*, *> -> return (this.value as Map<String, Any>).toJsonObject()
        else -> JsonPrimitive(this.value.toString())
    }
}