package com.example.demop4app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.demop4app.navigation.AppNavigation

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color    = MaterialTheme.colorScheme.background
        ) {
            // Semua navigasi dikelola oleh AppNavigation
            AppNavigation()
        }
    }
}