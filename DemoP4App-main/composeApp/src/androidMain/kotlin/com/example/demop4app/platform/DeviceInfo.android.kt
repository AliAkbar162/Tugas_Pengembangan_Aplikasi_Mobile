package com.example.demop4app.platform

import android.os.Build

class AndroidDeviceInfo : DeviceInfo {
    override fun getName(): String = "Android Device"
    override fun getModel(): String = Build.MODEL
    override fun getManufacturer(): String = Build.MANUFACTURER
    override fun getOsVersion(): String = Build.VERSION.RELEASE
}

actual fun getDeviceInfo(): DeviceInfo = AndroidDeviceInfo()
