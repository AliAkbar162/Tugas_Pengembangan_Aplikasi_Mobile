package com.example.demop4app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demop4app.data.model.Note
import com.example.demop4app.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getAllNotes().collect { notes ->
                _uiState.update { it.copy(notes = notes, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadNotes()
            } else {
                repository.searchNotes(query).collect { notes ->
                    _uiState.update { it.copy(notes = notes) }
                }
            }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val dateStr = "${now.dayOfMonth}/${now.monthNumber}/${now.year}"
            repository.insertNote(Note(0, title, content, false, dateStr))
        }
    }

    fun updateNote(id: Int, title: String, content: String) {
        viewModelScope.launch {
            val note = repository.getNoteById(id)
            if (note != null) {
                repository.insertNote(note.copy(title = title, content = content))
            }
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            val note = repository.getNoteById(id)
            if (note != null) {
                repository.toggleFavorite(id, !note.isFavorite)
            }
        }
    }

    fun getNoteById(id: Int): Note? {
        return _uiState.value.notes.find { it.id == id }
    }
}
