package com.example.demop4app.data.repository

import com.example.demop4app.data.GeminiService

interface AIRepository {
    suspend fun getSummary(text: String): Result<String>
}

class AIRepositoryImpl(private val geminiService: GeminiService) : AIRepository {
    override suspend fun getSummary(text: String): Result<String> {
        return try {
            val summary = geminiService.generateSummary(text)
            Result.success(summary)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
