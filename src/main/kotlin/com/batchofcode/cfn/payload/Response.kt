package com.batchofcode.cfn.payload

import com.batchofcode.cfn.json.toJsonObject

data class Response (
    val Status: ResponseStatus,
    val Reason: String? = null,
    val NoEcho: Boolean = false,
    val Data: Map<String, Any>? = null
) {
    companion object {
        fun emptySuccess(): Response {
            return Response(
                Status = ResponseStatus.SUCCESS
            )
        }

        fun exceptionResponse(exception: Exception): Response {
            return Response(
                Status = ResponseStatus.FAILED,
                Reason = "${exception.message}"
            )
        }
    }

    fun toCfnResponse(request: CfnRequest): CfnResponse {
        val jsonData = this.Data?.toJsonObject()
        return CfnResponse(
            Status = this.Status.toString(),
            Reason = this.Reason,
            PhysicalResourceId = request.PhysicalResourceId,
            StackId = request.StackId,
            RequestId = request.RequestId,
            LogicalResourceId = request.LogicalResourceId,
            NoEcho = this.NoEcho,
            Data = jsonData
        )
    }
}

enum class ResponseStatus {
    SUCCESS, FAILED
}