package com.example.demop4app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (title: String, content: String) -> Unit,
    onBack: () -> Unit
) {
    var title   by remember(note) { mutableStateOf(note?.title   ?: "") }
    var content by remember(note) { mutableStateOf(note?.content ?: "") }
    var titleError by remember { mutableStateOf(false) }

    if (note == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Catatan tidak ditemukan", modifier = Modifier.padding(16.dp))
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Catatan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isBlank()) titleError = true
                            else onSave(title, content)
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Simpan")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color  = MaterialTheme.colorScheme.tertiaryContainer,
                shape  = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text     = "Mengedit Note ID: ${note.id}",
                    style    = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            OutlinedTextField(
                value         = title,
                onValueChange = {
                    title      = it
                    titleError = false
                },
                label         = { Text("Judul *") },
                isError       = titleError,
                supportingText = if (titleError) { { Text("Judul tidak boleh kosong") } } else null,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true
            )

            OutlinedTextField(
                value         = content,
                onValueChange = { content = it },
                label         = { Text("Isi Catatan") },
                modifier      = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines      = Int.MAX_VALUE
            )

            Button(
                onClick  = {
                    if (title.isBlank()) titleError = true
                    else onSave(title, content)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}
