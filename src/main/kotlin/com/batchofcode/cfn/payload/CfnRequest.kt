package com.batchofcode.cfn.payload

import com.batchofcode.cfn.payload.RequestType.Create
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CfnRequest (
    val RequestType: RequestType = Create,
    val ResponseURL: String = "",
    val StackId: String = "",
    val RequestId: String = "",
    val ResourceType: String = "",
    val LogicalResourceId: String = "",
    val PhysicalResourceId: String = "",
    val ResourceProperties: Map<String, JsonElement> = emptyMap(),
    val OldResourceProperties: Map<String, JsonElement>? = null
)

enum class RequestType {
    Create, Update, Delete
}