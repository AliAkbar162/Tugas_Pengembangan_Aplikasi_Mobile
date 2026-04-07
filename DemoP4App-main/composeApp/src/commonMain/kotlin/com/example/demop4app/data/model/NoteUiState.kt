package com.example.demop4app.data.model

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)