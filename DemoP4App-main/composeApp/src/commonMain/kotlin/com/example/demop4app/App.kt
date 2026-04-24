package com.example.demop4app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demop4app.navigation.AppNavigation
import com.example.demop4app.settings.SettingsRepository
import com.example.demop4app.data.repository.NoteRepository

@Composable
fun App(
    settingsRepository: SettingsRepository? = null,
    noteRepository: NoteRepository? = null
) {
    val isDarkModeState = settingsRepository?.isDarkMode?.collectAsState(initial = isSystemInDarkTheme())
    val isDark = isDarkModeState?.value ?: isSystemInDarkTheme()

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
            AppNavigation(
                noteRepository = noteRepository,
                settingsRepository = settingsRepository
            )
        }
    }
}
