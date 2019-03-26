package com.pillar.gizmogrokker

class BluetoothDiscotech(val bluetoothInterface: BluetoothInterface) {

    fun discoverize() {
        if(bluetoothInterface.isEnabled) {
            bluetoothInterface.startDiscovery()
        } else {
            bluetoothInterface.enable()
            bluetoothInterface.registerForEnabled(::onBluetoothEnabled)
        }
    }

    private fun onBluetoothEnabled() {
        bluetoothInterface.startDiscovery()
        bluetoothInterface.unregister(::onBluetoothEnabled)
    }

}
