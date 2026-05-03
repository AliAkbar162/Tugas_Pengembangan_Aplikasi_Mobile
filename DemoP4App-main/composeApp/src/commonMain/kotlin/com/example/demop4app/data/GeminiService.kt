package com.example.demop4app.data

import com.example.demop4app.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class GeminiService(private val client: HttpClient) {

    suspend fun generateSummary(text: String): String {
        val apiKey = ApiConfig.GEMINI_API_KEY.trim()

        val models = listOf(
            "gemini-2.0-flash",
            "gemini-1.5-flash"
        )

        return tryModels(apiKey, models, text)
    }

    private suspend fun tryModels(
        apiKey: String,
        models: List<String>,
        text: String
    ): String {
        if (models.isEmpty()) throw AIError.Unknown("Semua model AI gagal diakses.")

        val currentModel = models.first()
        val prompt = "Ringkas catatan ini dalam 2 kalimat Bahasa Indonesia:\n\n$text"
        val requestBody = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )

        return try {
            val fullUrl = "https://generativelanguage.googleapis.com/v1beta/models/$currentModel:generateContent?key=$apiKey"

            val response: HttpResponse = client.post(fullUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (response.status == HttpStatusCode.OK) {
                val geminiResponse: GeminiResponse = response.body()
                geminiResponse.candidates
                    ?.firstOrNull()
                    ?.content?.parts
                    ?.firstOrNull()?.text
                    ?: throw AIError.Unknown("Respon AI kosong.")

            } else if (response.status == HttpStatusCode.NotFound && models.size > 1) {
                tryModels(apiKey, models.drop(1), text)

            } else {
                val errorBody = response.bodyAsText()
                throw AIError.Unknown("Error ${response.status.value}: $errorBody")
            }

        } catch (e: Exception) {
            if (e is AIError) throw e
            if (models.size > 1) tryModels(apiKey, models.drop(1), text)
            else throw AIError.Unknown("Gagal terhubung: ${e.message}")
        }
    }
}