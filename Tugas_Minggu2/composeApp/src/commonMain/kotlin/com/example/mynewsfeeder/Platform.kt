package com.example.mynewsfeeder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform