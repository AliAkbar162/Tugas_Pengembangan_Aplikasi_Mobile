package com.example.demop4app.di

import com.example.demop4app.data.GeminiService
import com.example.demop4app.data.repository.AIRepository
import com.example.demop4app.data.repository.AIRepositoryImpl
import com.example.demop4app.viewmodel.AIViewModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aiModule = module {
    single(named("AiClient")) {
        HttpClient {
            expectSuccess = false
            install(ContentNegotiation) {
                json(Json { 
                    ignoreUnknownKeys = true 
                    isLenient = true 
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
        }
    }

    single { GeminiService(get(named("AiClient"))) }
    single<AIRepository> { AIRepositoryImpl(get()) }
    viewModelOf(::AIViewModel)
}
