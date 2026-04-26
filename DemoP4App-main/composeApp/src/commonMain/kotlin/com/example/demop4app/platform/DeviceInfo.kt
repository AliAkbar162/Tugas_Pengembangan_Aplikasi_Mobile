package com.example.demop4app.platform

interface DeviceInfo {
    fun getName(): String
    fun getModel(): String
    fun getManufacturer(): String
    fun getOsVersion(): String
}

expect fun getDeviceInfo(): DeviceInfo
