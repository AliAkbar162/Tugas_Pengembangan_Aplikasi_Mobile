package com.example.demop4app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val results: List<Article> = emptyList()
)

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val summary: String? = null,
    val image_url: String? = null,
    val news_site: String? = null,
    val published_at: String? = null,
    val url: String? = null
)
