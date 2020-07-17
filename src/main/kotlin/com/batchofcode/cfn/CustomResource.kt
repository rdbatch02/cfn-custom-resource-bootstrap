package com.batchofcode.cfn

import com.batchofcode.cfn.payload.CfnRequest
import com.batchofcode.cfn.payload.Response
import kotlinx.serialization.json.JsonObject

@Suppress("unused")
interface CustomResource {
    fun onCreate(request: CfnRequest, context: JsonObject): Response
    fun onUpdate(request: CfnRequest, context: JsonObject): Response
    fun onDelete(request: CfnRequest, context: JsonObject): Response
}