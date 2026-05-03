package com.example.demop4app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFav: () -> Unit,
    onSummarize: () -> Unit
) {
    // Dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (note == null) {
        // Catatan tidak ditemukan
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Catatan tidak ditemukan", modifier = Modifier.padding(16.dp))
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Catatan", maxLines = 1) },
                navigationIcon = {
                    // Back button → popBackStack
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Tombol AI Summary
                    IconButton(onClick = onSummarize) {
                        Icon(
                            imageVector = Icons.Default.Info, // FIXED: Using Info which exists in material-icons-core
                            contentDescription = "AI Summary",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    // Tombol favorite
                    IconButton(onClick = onToggleFav) {
                        Icon(
                            imageVector = if (note.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (note.isFavorite) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    // Tombol edit → navigate ke EditNoteScreen dengan noteId
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    // Tombol hapus
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tampilkan noteId (bukti argument passing berhasil)
            Surface(
                color  = MaterialTheme.colorScheme.secondaryContainer,
                shape  = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text     = "Note ID: ${note.id}",
                    style    = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text  = note.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            if (note.createdAt.isNotBlank()) {
                Text(
                    text  = "Dibuat: ${note.createdAt}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(16.dp))
            }

            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text(
                text  = note.content.ifBlank { "(Tidak ada isi catatan)" },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    // Dialog konfirmasi hapus
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title   = { Text("Hapus Catatan?") },
            text    = { Text("Catatan \"${note.title}\" akan dihapus permanen.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }
}
