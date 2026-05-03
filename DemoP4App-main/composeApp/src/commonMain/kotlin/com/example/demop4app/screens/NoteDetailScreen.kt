package com.example.demop4app.screens

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
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (note == null) {
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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onSummarize) {
                        Icon(
                            imageVector = Icons.Default.Info, // Menggunakan Info agar tidak Unresolved reference
                            contentDescription = "AI Summary",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onToggleFav) {
                        Icon(
                            imageVector = if (note.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (note.isFavorite) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
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
