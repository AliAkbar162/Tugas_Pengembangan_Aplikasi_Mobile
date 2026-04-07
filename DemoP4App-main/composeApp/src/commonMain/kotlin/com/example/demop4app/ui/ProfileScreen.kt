package com.example.demop4app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(56.dp),
                    tint     = MaterialTheme.colorScheme.primary
                )
            }

            Text("Mahasiswa ITERA", style = MaterialTheme.typography.headlineSmall)
            Text("IF25-22017 | Pengembangan Aplikasi Mobile",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline)

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            // Info cards
            ProfileInfoRow(label = "Program Studi", value = "Teknik Informatika")
            ProfileInfoRow(label = "Institusi",      value = "Institut Teknologi Sumatera")
            ProfileInfoRow(label = "Mata Kuliah",    value = "Pengembangan Aplikasi Mobile")
            ProfileInfoRow(label = "Pertemuan",      value = "5 – Navigasi Antar Layar")
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}