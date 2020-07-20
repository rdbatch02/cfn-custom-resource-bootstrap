package com.batchofcode.cfn

import com.amazonaws.services.lambda.runtime.Context
import com.batchofcode.cfn.payload.CfnRequest
import com.batchofcode.cfn.payload.Response

@Suppress("unused")
interface CustomResource {
    fun onCreate(request: CfnRequest, context: Context?): Response
    fun onUpdate(request: CfnRequest, context: Context?): Response
    fun onDelete(request: CfnRequest, context: Context?): Response
}