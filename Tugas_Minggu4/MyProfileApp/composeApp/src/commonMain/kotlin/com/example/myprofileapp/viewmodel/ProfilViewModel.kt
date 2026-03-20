package com.example.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun enterEditMode() {
        _uiState.update { it.copy(isEditMode = true) }
    }

    fun saveProfile(newName: String, newBio: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = newName.trim().ifBlank { currentState.name },
                bio = newBio.trim().ifBlank { currentState.bio },
                isEditMode = false
            )
        }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(isEditMode = false) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }
}