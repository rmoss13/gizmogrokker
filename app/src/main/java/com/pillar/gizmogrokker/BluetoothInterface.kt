package com.pillar.gizmogrokker

interface BluetoothInterface {

    fun startDiscovery() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun enable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registerForEnabled(callback: () -> Unit) {

    }

    fun unregister(callback: () -> Unit) {

    }

    val hasBluetoothSupport: Boolean get() = false
    val isEnabled: Boolean get() = false
}
