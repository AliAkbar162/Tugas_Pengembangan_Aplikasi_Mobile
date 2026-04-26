package com.example.demop4app.di

import com.example.demop4app.database.DriverFactory
import com.example.demop4app.database.NoteDatabase
import com.example.demop4app.platform.*
import com.example.demop4app.settings.SettingsRepository
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {
    single { NoteDatabase(DriverFactory(get()).createDriver()) }
    
    single<DeviceInfo> { AndroidDeviceInfo() }
    
    single<NetworkMonitor> { AndroidNetworkMonitor(get()) }
    
    single<BatteryInfo> { AndroidBatteryInfo(get()) }
    
    single {
        SettingsRepository(
            PreferenceDataStoreFactory.create(
                produceFile = { File(get<android.content.Context>().filesDir, "settings.preferences_pb") }
            )
        )
    }
}
