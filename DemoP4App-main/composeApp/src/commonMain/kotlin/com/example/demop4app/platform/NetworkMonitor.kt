package com.example.demop4app.platform

import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    val isOnline: StateFlow<Boolean>
}

expect fun getNetworkMonitor(): NetworkMonitor
