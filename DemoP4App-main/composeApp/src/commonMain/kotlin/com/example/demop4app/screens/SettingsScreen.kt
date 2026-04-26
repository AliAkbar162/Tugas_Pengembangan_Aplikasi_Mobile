package com.example.demop4app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.settings.SettingsRepository
import com.example.demop4app.platform.DeviceInfo
import com.example.demop4app.platform.BatteryInfo
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository = koinInject(),
    deviceInfo: DeviceInfo = koinInject(),
    batteryInfo: BatteryInfo = koinInject()
) {
    val isDarkMode by settingsRepository.isDarkMode.collectAsState(initial = false)
    val sortOrder by settingsRepository.sortOrder.collectAsState(initial = "newest")
    val batteryLevel by batteryInfo.level.collectAsState()
    val isCharging by batteryInfo.isCharging.collectAsState()
    
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
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

            Text("Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Column {
                Text("Sort Order", style = MaterialTheme.typography.bodyLarge)
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

            HorizontalDivider()

            // Display hardware information from platform-specific APIs
            Text("Device Information", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            InfoRow(label = "Model", value = deviceInfo.getModel())
            InfoRow(label = "Manufacturer", value = deviceInfo.getManufacturer())
            InfoRow(label = "OS Version", value = deviceInfo.getOsVersion())
            
            HorizontalDivider()

            // Display battery status (Bonus Requirement)
            Text("Battery Status", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            InfoRow(label = "Level", value = "$batteryLevel%")
            InfoRow(label = "Status", value = if (isCharging) "Charging" else "Discharging")
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}
