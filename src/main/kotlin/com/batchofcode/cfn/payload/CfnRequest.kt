package com.batchofcode.cfn.payload

import com.batchofcode.cfn.payload.RequestType.Create
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CfnRequest (
    val RequestType: RequestType = Create,
    val ResponseURL: String = "",
    val StackId: String = "",
    val RequestId: String = "",
    val ResourceType: String = "",
    val LogicalResourceId: String = "",
    val PhysicalResourceId: String = "",
    val ResourceProperties: JsonObject = JsonObject(emptyMap()),
    val OldResourceProperties: JsonObject? = null
)

enum class RequestType {
    Create, Update, Delete
}