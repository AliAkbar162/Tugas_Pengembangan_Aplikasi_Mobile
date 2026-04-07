package com.example.demop4app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // FAB untuk navigasi ke AddNoteScreen
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
            }
        }
    ) { padding ->
        if (notes.isEmpty()) {
            // Tampilan kosong
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Belum ada catatan", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Tekan + untuk menambah catatan", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note             = note,
                        onClick          = { onNoteClick(note.id) },
                        onToggleFavorite = { onToggleFavorite(note.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = note.title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = note.content,
                    style    = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (note.createdAt.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = note.createdAt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            // Tombol favorit
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (note.isFavorite) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}