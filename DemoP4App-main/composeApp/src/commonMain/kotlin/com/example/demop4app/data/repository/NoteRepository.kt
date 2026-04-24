package com.example.demop4app.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.demop4app.data.model.Note
import com.example.demop4app.database.NoteDatabase
import com.example.demop4app.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(database: NoteDatabase) {
    private val queries = database.noteEntityQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.getAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toNote() }
            }
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.searchNotes(query, query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toNote() }
            }
    }

    fun getNoteById(id: Int): Note? {
        return queries.getNoteById(id.toLong()).executeAsOneOrNull()?.toNote()
    }

    fun insertNote(note: Note) {
        queries.insertNote(
            id = if (note.id == 0) null else note.id.toLong(),
            title = note.title,
            content = note.content,
            isFavorite = if (note.isFavorite) 1L else 0L,
            createdAt = note.createdAt
        )
    }

    fun deleteNote(id: Int) {
        queries.deleteNoteById(id.toLong())
    }

    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        queries.updateFavorite(if (isFavorite) 1L else 0L, id.toLong())
    }

    private fun NoteEntity.toNote(): Note {
        return Note(
            id = id.toInt(),
            title = title,
            content = content,
            isFavorite = isFavorite == 1L,
            createdAt = createdAt
        )
    }
}
