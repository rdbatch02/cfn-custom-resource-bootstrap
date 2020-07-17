package com.batchofcode.cfn.payload

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CfnRequest (
    val RequestType: RequestType,
    val ResponseURL: String,
    val StackId: String,
    val RequestId: String,
    val ResourceType: String,
    val LogicalResourceId: String,
    val PhysicalResourceId: String,
    val ResourceProperties: JsonObject,
    val OldResourceProperties: JsonObject?
)

enum class RequestType {
    Create, Update, Delete
}