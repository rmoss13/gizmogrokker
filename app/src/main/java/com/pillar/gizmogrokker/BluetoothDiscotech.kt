package com.pillar.gizmogrokker

import kotlinx.coroutines.CompletableDeferred

class BluetoothDiscotech(private val bluetoothInterface: BluetoothInterface) {

    suspend fun discoverize() = beginDiscovery()
        .run { collectDevices() }

    private fun beginDiscovery() = if (bluetoothInterface.isEnabled) {
        bluetoothInterface.startDiscovery()
    } else {
        bluetoothInterface.enable()
        bluetoothInterface.bluetoothEnabled + ::onBluetoothEnabled
    }

    private suspend fun collectDevices() = mutableListOf<BloothDevice>()
        .also { devices ->
            bluetoothInterface.deviceDiscovered + { devices += it }
            waitForDiscoveryToFinish()
        }

    private suspend fun waitForDiscoveryToFinish() = CompletableDeferred<Unit>()
        .apply { bluetoothInterface.discoveryEnded + { complete(Unit) } }
        .await()

    private fun onBluetoothEnabled(unit: Unit) {
        bluetoothInterface.startDiscovery()
        bluetoothInterface.bluetoothEnabled - ::onBluetoothEnabled
    }

}
