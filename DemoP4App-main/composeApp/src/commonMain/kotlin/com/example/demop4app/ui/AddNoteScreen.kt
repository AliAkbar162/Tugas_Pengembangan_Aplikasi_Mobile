package com.example.demop4app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onSave: (title: String, content: String) -> Unit,
    onBack: () -> Unit
) {
    var title   by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Catatan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Tombol simpan
                    IconButton(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                            } else {
                                onSave(title, content)
                            }
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
            OutlinedTextField(
                value         = title,
                onValueChange = {
                    title      = it
                    titleError = false
                },
                label        = { Text("Judul *") },
                isError      = titleError,
                supportingText = if (titleError) {{ Text("Judul tidak boleh kosong") }} else null,
                modifier     = Modifier.fillMaxWidth(),
                singleLine   = true
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
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onSave(title, content)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Catatan")
            }
        }
    }
}