package com.example.demop4app.di

import com.example.demop4app.data.repository.NoteRepository
import com.example.demop4app.data.repository.NewsRepository
import com.example.demop4app.viewmodel.NoteViewModel
import com.example.demop4app.viewmodel.NewsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration

// Centralized DI initialization
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        // Menambahkan aiModule ke dalam daftar modules
        modules(commonModule, aiModule, platformModule)
    }
}

// Shared dependencies across all platforms
val commonModule = module {
    single { NewsRepository() }
    single { NoteRepository(get()) }
    viewModelOf(::NoteViewModel)
    viewModelOf(::NewsViewModel)
}

expect val platformModule: Module
