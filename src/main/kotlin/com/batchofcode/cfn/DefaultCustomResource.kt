package com.batchofcode.cfn

import com.amazonaws.services.lambda.runtime.Context
import com.batchofcode.cfn.payload.CfnRequest
import com.batchofcode.cfn.payload.Response

class DefaultCustomResource: CustomResource {
    override fun onCreate(request: CfnRequest, context: Context?): Response {
        println("onCreate")
        return Response.emptySuccess()
    }

    override fun onDelete(request: CfnRequest, context: Context?): Response {
        println("onDelete")
        return Response.emptySuccess()
    }

    override fun onUpdate(request: CfnRequest, context: Context?): Response {
        println("onUpdate")
        return Response.emptySuccess()
    }
}