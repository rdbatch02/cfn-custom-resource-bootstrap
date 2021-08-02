package com.batchofcode.cfn.responder

import com.batchofcode.cfn.payload.CfnResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class HttpCfnResponder(private val httpClient: HttpClient = HttpClient()): CfnResponder {
    override fun sendResponse(responseUrl: String, response: CfnResponse) {
        val responsePayload = Json.encodeToString(CfnResponse.serializer(), response)
        runBlocking {
            httpClient.put<Unit> {
                url(Url(responseUrl))
                header("Content-Type", ContentType.Application.Json)
                body = responsePayload
            }
        }
    }
}