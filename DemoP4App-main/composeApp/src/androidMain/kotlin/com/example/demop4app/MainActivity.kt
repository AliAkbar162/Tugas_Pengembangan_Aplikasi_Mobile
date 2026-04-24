package com.example.demop4app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.demop4app.data.repository.NoteRepository
import com.example.demop4app.database.DriverFactory
import com.example.demop4app.database.NoteDatabase
import com.example.demop4app.settings.SettingsRepository
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val driver = DriverFactory(applicationContext).createDriver()
        val database = NoteDatabase(driver)
        val noteRepository = NoteRepository(database)
        
        // Proper DataStore initialization for Android
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(applicationContext.filesDir, "settings.preferences_pb") }
        )
        val settingsRepository = SettingsRepository(dataStore)

        setContent {
            App(
                settingsRepository = settingsRepository,
                noteRepository = noteRepository
            )
        }
    }
}
