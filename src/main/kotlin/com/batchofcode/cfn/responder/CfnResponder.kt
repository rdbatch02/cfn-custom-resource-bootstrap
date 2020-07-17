package com.batchofcode.cfn.responder

import com.batchofcode.cfn.payload.CfnResponse

interface CfnResponder {
    fun sendResponse(responseUrl: String, response: CfnResponse)
}