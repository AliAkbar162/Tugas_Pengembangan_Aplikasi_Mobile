package com.example.myprofileapp

// ============================================================
// ENTRY POINT — App.kt
// Tugas Praktikum Minggu 4: State Management & MVVM
// ============================================================

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myprofileapp.ui.EditProfileScreen
import com.example.myprofileapp.ui.ProfileScreen
import com.example.myprofileapp.viewmodel.ProfileViewModel

@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }

    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(
        colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        if (uiState.isEditMode) {
            EditProfileScreen(
                uiState  = uiState,
                onSave   = { name, bio -> viewModel.saveProfile(name, bio) },
                onCancel = { viewModel.cancelEdit() }
            )
        } else {
            ProfileScreen(
                uiState          = uiState,
                onEditClick      = { viewModel.enterEditMode() },
                onToggleDarkMode = { viewModel.toggleDarkMode() }
            )
        }
    }
}