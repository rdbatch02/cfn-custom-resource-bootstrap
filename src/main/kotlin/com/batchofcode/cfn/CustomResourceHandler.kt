package com.batchofcode.cfn

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.batchofcode.cfn.payload.CfnRequest
import com.batchofcode.cfn.payload.RequestType
import com.batchofcode.cfn.payload.Response
import com.batchofcode.cfn.responder.CfnResponder
import com.batchofcode.cfn.responder.HttpCfnResponder
import com.batchofcode.cfn.responder.OfflineCfnResponder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

const val HANDLER_CLASS_KEY = "CUSTOM_RESOURCE_HANDLER_CLASS"

@Suppress("unused")
class CustomResourceHandler(
    private val environment: Map<String, String> = System.getenv(),
    private val responder: CfnResponder = HttpCfnResponder()
): RequestStreamHandler {

    override fun handleRequest(input: InputStream, output: OutputStream, context: Context?) {
        val inputString = String(input.readBytes())
        val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        val request = json.decodeFromString(CfnRequest.serializer(), inputString).let {
            if (it.PhysicalResourceId.isEmpty()) {
                return@let it.copy(PhysicalResourceId = context!!.logStreamName)
            }
            return@let it
        }
        handle(request, context)
    }

    fun handle(request: CfnRequest, context: Context?) {
        try {
            val handler: CustomResource = CustomResourceLoader.load(environment[HANDLER_CLASS_KEY].toString())
            val response = invokeCustomResource(request, handler, context)
            responder.sendResponse(request.ResponseURL, response.toCfnResponse(request))
        }
        catch (ex: Exception) {
            println("Caught exception - ${ex.message}")
            responder.sendResponse(request.ResponseURL, Response.exceptionResponse(ex).toCfnResponse(request))
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

fun main() {
    val sampleRequest = CfnRequest(
        RequestType = RequestType.Create,
        ResponseURL = "localhost",
        StackId = "localstack",
        RequestId = "Request123",
        ResourceType = "LocalSample",
        LogicalResourceId = "LocalSample",
        PhysicalResourceId = "LocalSample"
    )
    val handler = CustomResourceHandler(
        environment = mapOf(HANDLER_CLASS_KEY to DefaultCustomResource::class.qualifiedName!!),
        responder = OfflineCfnResponder()
    )
    handler.handle(sampleRequest, null)
}