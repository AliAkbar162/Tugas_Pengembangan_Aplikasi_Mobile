package com.example.demop4app.platform

import kotlinx.coroutines.flow.StateFlow

interface BatteryInfo {
    val level: StateFlow<Int>
    val isCharging: StateFlow<Boolean>
}

expect fun getBatteryInfo(): BatteryInfo
