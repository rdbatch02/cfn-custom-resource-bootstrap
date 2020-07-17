package com.batchofcode.cfn.payload

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CfnResponse(
    val Status: String,
    val Reason: String?,
    val PhysicalResourceId: String,
    val StackId: String,
    val RequestId: String,
    val LogicalResourceId: String,
    val NoEcho: Boolean = false,
    val Data: JsonObject?
)