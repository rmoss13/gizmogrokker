package com.pillar.gizmogrokker

typealias DeviceCallback = (BloothDevice) -> Unit
typealias EnabledCallback = () -> Unit
typealias DiscoveryEndedCallback = () -> Unit

interface BluetoothInterface {

    fun startDiscovery() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun enable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registerEnabled(callback: EnabledCallback) {

    }

    fun unregisterEnabled(callback: EnabledCallback) {

    }

    val hasBluetoothSupport: Boolean get() = false
    val isEnabled: Boolean get() = false
    fun registerDeviceDiscovered(callback: DeviceCallback)
    fun registerDiscoveryEnded(callback: DiscoveryEndedCallback)
}
