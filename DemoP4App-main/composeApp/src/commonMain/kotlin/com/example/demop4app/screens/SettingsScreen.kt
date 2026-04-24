package com.example.demop4app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.settings.SettingsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository
) {
    val isDarkMode by settingsRepository.isDarkMode.collectAsState(initial = false)
    val sortOrder by settingsRepository.sortOrder.collectAsState(initial = "newest")
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { 
                        scope.launch { settingsRepository.toggleDarkMode(it) }
                    }
                )
            }

            HorizontalDivider()

            Text("Sort Order", style = MaterialTheme.typography.titleMedium)
            
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = sortOrder == "newest",
                        onClick = { scope.launch { settingsRepository.setSortOrder("newest") } }
                    )
                    Text("Newest First")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = sortOrder == "oldest",
                        onClick = { scope.launch { settingsRepository.setSortOrder("oldest") } }
                    )
                    Text("Oldest First")
                }
            }
        }
    }
}
