package com.example.demop4app.di

import com.example.demop4app.database.DriverFactory
import com.example.demop4app.database.NoteDatabase
import com.example.demop4app.platform.*
import com.example.demop4app.settings.SettingsRepository
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import okio.Path.Companion.toPath

actual val platformModule: Module = module {
    single { NoteDatabase(DriverFactory().createDriver()) }
    
    single<DeviceInfo> { IosDeviceInfo() }
    
    // For iOS, we'll need basic implementations or placeholders if not fully implemented
    // single<NetworkMonitor> { IosNetworkMonitor() } 
    // single<BatteryInfo> { IosBatteryInfo() }

    single {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val path = documentDirectory?.path + "/settings.preferences_pb"
        
        SettingsRepository(
            PreferenceDataStoreFactory.createWithPath(
                producePath = { path.toPath() }
            )
        )
    }
}
