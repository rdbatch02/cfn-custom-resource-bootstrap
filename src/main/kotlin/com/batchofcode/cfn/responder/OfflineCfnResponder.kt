package com.batchofcode.cfn.responder

import com.batchofcode.cfn.payload.CfnResponse

class OfflineCfnResponder: CfnResponder {
    override fun sendResponse(responseUrl: String, response: CfnResponse) {
        println("Offline Responder Active, not sending a response to ${responseUrl}")
        println(response)
    }
}