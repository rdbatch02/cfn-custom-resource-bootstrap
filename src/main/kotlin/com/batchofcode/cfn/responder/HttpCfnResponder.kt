package com.batchofcode.cfn.responder

import com.batchofcode.cfn.payload.CfnResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.http.ContentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class HttpCfnResponder(private val httpClient: HttpClient = HttpClient()): CfnResponder {
    override fun sendResponse(responseUrl: String, response: CfnResponse) {
        val responsePayload = Json(JsonConfiguration.Stable).stringify(CfnResponse.serializer(), response)
        runBlocking {
            httpClient.put<Unit> {
                header("Content-Type", ContentType.Application.Json)
                body = responsePayload
            }
        }
    }
}