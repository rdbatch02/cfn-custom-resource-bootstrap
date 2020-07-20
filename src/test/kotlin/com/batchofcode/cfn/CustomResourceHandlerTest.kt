package com.batchofcode.cfn

import com.batchofcode.cfn.payload.*
import com.batchofcode.cfn.responder.CfnResponder
import io.mockk.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class CustomResourceHandlerTest {
    private val mockResponder: CfnResponder = mockk(relaxed = true)
    private val mockResource: CustomResource = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        mockkObject(CustomResourceLoader)
        every { CustomResourceLoader.load(any()) } returns mockResource
    }

    @AfterTest
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `should handle CREATE request`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Create,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.SUCCESS,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onCreate(any(), any()) } returns responseObject

        handler.handle(requestJson, contextJson)
        verify { mockResource.onCreate(requestObject, contextJson) }
        verify { mockResponder.sendResponse(requestObject.ResponseURL, responseObject.toCfnResponse(requestObject)) }
    }

    @Test
    fun `should handle UPDATE request`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Update,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.SUCCESS,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onUpdate(any(), any()) } returns responseObject

        handler.handle(requestJson, contextJson)
        verify { mockResource.onUpdate(requestObject, contextJson) }
        val response = responseObject.toCfnResponse(requestObject)
        verify { mockResponder.sendResponse(requestObject.ResponseURL, response) }
    }

    @Test
    fun `should handle DELETE request`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Delete,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.SUCCESS,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onDelete(any(), any()) } returns responseObject

        handler.handle(requestJson, contextJson)
        verify { mockResource.onDelete(requestObject, contextJson) }
        verify { mockResponder.sendResponse(requestObject.ResponseURL, responseObject.toCfnResponse(requestObject)) }
    }

    @Test(expected = Exception::class)
    fun `should send an error response and throw exception when processing CREATE fails`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Create,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.FAILED,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onCreate(any(), any()) } throws Exception()

        handler.handle(requestJson, contextJson)
        verify { mockResource.onCreate(requestObject, contextJson) }
        verify { mockResponder.sendResponse(requestObject.ResponseURL, responseObject.toCfnResponse(requestObject)) }
    }

    @Test(expected = Exception::class)
    fun `should send an error response and throw exception when processing UPDATE fails`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Update,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.FAILED,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onUpdate(any(), any()) } throws Exception()

        handler.handle(requestJson, contextJson)
        verify { mockResource.onUpdate(requestObject, contextJson) }
        verify { mockResponder.sendResponse(requestObject.ResponseURL, responseObject.toCfnResponse(requestObject)) }
    }


    @Test(expected = Exception::class)
    fun `should send an error response and throw exception when processing DELETE fails`() {
        val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
        val handler = CustomResourceHandler(mapOf(), mockResponder)
        val requestObject = CfnRequest(
            RequestType = RequestType.Delete,
            ResponseURL = "http://test",
            StackId = "123TestStack",
            RequestId = "123TestRequest",
            ResourceType = "TEST",
            LogicalResourceId = "TEST",
            PhysicalResourceId = "TEST",
            ResourceProperties = JsonObject(mapOf()),
            OldResourceProperties = null
        )
        val responseObject = Response(
            Status = ResponseStatus.FAILED,
            Reason = null,
            NoEcho = false,
            Data = mapOf()
        )
        val requestJson = json.toJson(CfnRequest.serializer(), requestObject).jsonObject
        val contextJson = JsonObject(mapOf())
        every { mockResource.onDelete(any(), any()) } throws Exception()

        handler.handle(requestJson, contextJson)
        verify { mockResource.onDelete(requestObject, contextJson) }
        verify { mockResponder.sendResponse(requestObject.ResponseURL, responseObject.toCfnResponse(requestObject)) }
    }
}