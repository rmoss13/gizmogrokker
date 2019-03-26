package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter

typealias DeviceCallback = (BloothDevice) -> Unit
typealias EnabledCallback = () -> Unit
typealias DiscoveryEndedCallback = () -> Unit

interface BluetoothInterface {

    val adapter: BluetoothAdapter?
    val hasBluetoothSupport: Boolean get() = adapter != null
    val isEnabled: Boolean get() = adapter?.isEnabled ?: false

    fun enable() {
    }

    fun startDiscovery() {
    }

    fun registerEnabled(callback: EnabledCallback) {
    }

    fun unregisterEnabled(callback: EnabledCallback) {
    }

    fun registerDeviceDiscovered(callback: DeviceCallback) {
    }

    fun registerDiscoveryEnded(callback: DiscoveryEndedCallback) {
    }

}
