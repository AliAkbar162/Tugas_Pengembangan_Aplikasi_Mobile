package com.example.demop4app.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidBatteryInfo(private val context: Context) : BatteryInfo {
    private val _level = MutableStateFlow(0)
    override val level: StateFlow<Int> = _level.asStateFlow()

    private val _isCharging = MutableStateFlow(false)
    override val isCharging: StateFlow<Boolean> = _isCharging.asStateFlow()

    init {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    
                    _level.value = (level * 100 / scale.toFloat()).toInt()
                    _isCharging.value = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
                }
            }
        }
        context.registerReceiver(receiver, intentFilter)
    }
}

actual fun getBatteryInfo(): BatteryInfo {
    throw Exception("Use Koin to inject BatteryInfo on Android")
}
