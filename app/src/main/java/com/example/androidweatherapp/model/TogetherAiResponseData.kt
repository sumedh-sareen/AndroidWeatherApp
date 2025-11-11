package com.example.androidweatherapp.model

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    val id: String,
    @SerializedName("object") val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage?
)

data class Choice(
    val index: Int,
    val message: MessageResponse,
    @SerializedName("finish_reason") val finishReason: String?
)

data class MessageResponse(
    val role: String,
    val content: String?,
    @SerializedName("tool_calls") val toolCalls: List<ToolCall>? = null,
    @SerializedName("function_call") val functionCall: FunctionCall? = null
)

data class ToolCall(
    val id: String,
    val type: String,
    val function: FunctionCall,
    val index: Int? = null
)

data class FunctionCall(
    val name: String,
    val arguments: String
)

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)