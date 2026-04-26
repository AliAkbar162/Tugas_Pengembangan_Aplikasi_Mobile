package com.example.demop4app.platform

import platform.UIKit.UIDevice

class IosDeviceInfo : DeviceInfo {
    override fun getName(): String = UIDevice.currentDevice.name
    override fun getModel(): String = UIDevice.currentDevice.model
    override fun getManufacturer(): String = "Apple"
    override fun getOsVersion(): String = UIDevice.currentDevice.systemVersion
}

actual fun getDeviceInfo(): DeviceInfo = IosDeviceInfo()
