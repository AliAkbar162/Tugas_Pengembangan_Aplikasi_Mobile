package com.example.mynewsfeeder

enum class NewsCategory {
    TEKNOLOGI, OLAHRAGA, POLITIK, HIBURAN, KESEHATAN
}

data class NewsItem(
    val id: Int,
    val title: String,
    val summary: String,
    val category: NewsCategory
)

data class NewsDetail(
    val newsItem: NewsItem,
    val fullContent: String,
    val author: String,
    val readTimeMinutes: Int
)