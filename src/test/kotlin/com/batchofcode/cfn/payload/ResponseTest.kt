package com.batchofcode.cfn.payload

import com.batchofcode.cfn.json.toJsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTest {
    @Test
    fun `exceptionResponse includes exception message in payload`() {
        val testException = Exception("test message")
        val testResponse = Response.exceptionResponse(testException)
        assertEquals(testException.message, testResponse.Reason)
    }

    @Test
    fun `constructs a CfnResponse payload from a CfnRequest`() {
        val testRequest = CfnRequest(
            RequestType = RequestType.Create,
            ResponseURL = "http://test.com",
            StackId = "123test",
            RequestId = "123",
            ResourceType = "CustomResource",
            LogicalResourceId = "123",
            PhysicalResourceId = "123",
            ResourceProperties = mapOf("key" to JsonPrimitive("value")),
            OldResourceProperties = null
        )
        val response = Response(
            Status = ResponseStatus.SUCCESS,
            Reason = "",
            NoEcho = false,
            Data = mapOf("responseData" to "responseValue")
        )
        val expectedCfnResponse = CfnResponse(
            Status = ResponseStatus.SUCCESS.toString(),
            Reason = "",
            PhysicalResourceId = testRequest.PhysicalResourceId,
            StackId = testRequest.StackId,
            RequestId = testRequest.RequestId,
            LogicalResourceId = testRequest.LogicalResourceId,
            NoEcho = false,
            Data = mapOf("responseData" to "responseValue").toJsonObject()
        )

        assertEquals(expectedCfnResponse, response.toCfnResponse(testRequest))
    }
}