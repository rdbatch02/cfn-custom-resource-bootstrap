package com.batchofcode.cfn

import com.amazonaws.services.lambda.runtime.Context
import com.batchofcode.cfn.exception.InvalidCustomResourceException
import com.batchofcode.cfn.responder.HttpCfnResponder
import com.batchofcode.cfn.payload.CfnRequest
import com.batchofcode.cfn.payload.RequestType
import com.batchofcode.cfn.payload.Response
import com.batchofcode.cfn.responder.CfnResponder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject

const val HANDLER_CLASS_KEY = "CUSTOM_RESOURCE_HANDLER_CLASS"

@Suppress("unused")
class CustomResourceHandler(
    private val environment: Map<String, String> = System.getenv(),
    private val responder: CfnResponder = HttpCfnResponder()
) {
    fun handle(request: String, context: Context?) {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val parsedRequest = json.parse(CfnRequest.serializer(), request)

        try {
            val handler: CustomResource = CustomResourceLoader.load(environment[HANDLER_CLASS_KEY].toString())
            val response = invokeCustomResource(parsedRequest, handler, context)
            responder.sendResponse(parsedRequest.ResponseURL, response.toCfnResponse(parsedRequest))
        }
        catch (ex: Exception) {
            println("Caught exception - ${ex.message}")
            responder.sendResponse(parsedRequest.ResponseURL, Response.exceptionResponse(ex).toCfnResponse(parsedRequest))
            throw ex
        }
    }

    private fun invokeCustomResource(parsedRequest: CfnRequest, handler: CustomResource, context: Context?): Response {
        return when (parsedRequest.RequestType) {
            RequestType.Create -> handler.onCreate(parsedRequest, context)
            RequestType.Update -> handler.onUpdate(parsedRequest, context)
            RequestType.Delete -> handler.onDelete(parsedRequest, context)
        }
    }
}