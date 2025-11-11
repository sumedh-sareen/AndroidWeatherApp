package com.example.androidweatherapp.model

// Data classes
data class ChatRequest(
    val messages: List<Message>,
    val model: String,
    val max_tokens: Int? = null,
    val temperature: Double? = null,
    val stream: Boolean? = null
    // Add other parameters as needed
)

data class Message(
    val content: String,
    val role: String,
    val name: String? = null
)