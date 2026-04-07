package com.example.demop4app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demop4app.data.model.Note
import com.example.demop4app.data.model.NoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    private var nextId = 4

    init {
        // Sample data awal
        _uiState.update {
            it.copy(
                notes = listOf(
                    Note(1, "Belajar Kotlin", "Kotlin adalah bahasa pemrograman modern untuk Android.", false, "2025-04-01"),
                    Note(2, "Compose UI", "Jetpack Compose adalah toolkit UI deklaratif dari Google.", true, "2025-04-02"),
                    Note(3, "KMP Notes", "Kotlin Multiplatform memungkinkan berbagi kode antar platform.", false, "2025-04-03")
                )
            )
        }
    }

    fun getNoteById(noteId: Int): Note? {
        return _uiState.value.notes.find { it.id == noteId }
    }

    fun addNote(title: String, content: String) {
        if (title.isBlank()) return
        val newNote = Note(
            id = nextId++,
            title = title.trim(),
            content = content.trim(),
            isFavorite = false,
            createdAt = "2025-04-07"
        )
        _uiState.update { state ->
            state.copy(notes = state.notes + newNote)
        }
    }

    fun updateNote(noteId: Int, title: String, content: String) {
        if (title.isBlank()) return
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == noteId) note.copy(title = title.trim(), content = content.trim())
                    else note
                }
            )
        }
    }

    fun deleteNote(noteId: Int) {
        _uiState.update { state ->
            state.copy(notes = state.notes.filter { it.id != noteId })
        }
    }

    fun toggleFavorite(noteId: Int) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == noteId) note.copy(isFavorite = !note.isFavorite)
                    else note
                }
            )
        }
    }

    fun getFavoriteNotes(): List<Note> {
        return _uiState.value.notes.filter { it.isFavorite }
    }
}