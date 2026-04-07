package com.example.demop4app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint     = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Belum ada catatan favorit", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Tekan ♡ di catatan untuk menambah ke favorit",
                        style = MaterialTheme.typography.bodySmall
                    )
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