package com.example.demop4app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(val contents: List<Content>)

@Serializable
data class Content(val parts: List<Part>)

@Serializable
data class Part(val text: String)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(val content: Content)

sealed class AIError(message: String? = null) : Exception(message) {
    data object InvalidApiKey : AIError("Kunci API tidak valid.")
    data object RateLimitExceeded : AIError("Limit penggunaan habis (Rate Limit).")
    data object ServerError : AIError("Server Google Gemini sedang bermasalah.")
    data object Timeout : AIError("Waktu permintaan habis.")
    data class Unknown(val errorDetail: String) : AIError(errorDetail)
}
