package com.example.demop4app.data.repository

import com.example.demop4app.data.model.Article
import com.example.demop4app.data.model.NewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.random.Random

class NewsRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun getTopHeadlines(): List<Article> {
        return try {
            // Added random offset to ensure news changes when refreshing
            val offset = Random.nextInt(0, 100)
            val response: NewsResponse = client.get("https://api.spaceflightnewsapi.net/v4/articles/?limit=10&offset=$offset").body()
            response.results
        } catch (e: Exception) {
            throw e
        }
    }
}
