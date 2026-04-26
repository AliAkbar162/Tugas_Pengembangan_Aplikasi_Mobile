package com.example.demop4app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demop4app.navigation.AppNavigation
import com.example.demop4app.settings.SettingsRepository
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinContext {
        val settingsRepository: SettingsRepository = koinInject()
        val isDarkModeState = settingsRepository.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
        val isDark = isDarkModeState.value

        val colorScheme = if (isDark) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }

        MaterialTheme(colorScheme = colorScheme) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color    = MaterialTheme.colorScheme.background
            ) {
                AppNavigation()
            }
        }
    }
}
