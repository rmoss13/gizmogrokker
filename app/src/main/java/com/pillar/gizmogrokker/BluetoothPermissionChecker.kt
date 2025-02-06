package com.pillar.gizmogrokker

import android.Manifest

class BluetoothPermissionChecker(
    private val bluetoothInterface: BluetoothInterface,
    private val permissionProxy: PermissionProxy
) {
    fun checkBluetoothState() = when {
        hasBluetoothSupport() -> checkPermission()
        else -> GrokkerBluetoothState.NoBlooth
    }

    private fun hasBluetoothSupport() = bluetoothInterface.hasBluetoothSupport

    private fun checkPermission() =
        if (hasAccessFineLocation() && hasBluetoothScan() && hasBluetoothConnect()) {
            GrokkerBluetoothState.Ready
        } else {
            GrokkerBluetoothState.MustRequest
        }

    private fun hasAccessFineLocation() =
        permissionProxy.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun hasBluetoothScan() =
        permissionProxy.hasPermission(Manifest.permission.BLUETOOTH_SCAN)

    private fun hasBluetoothConnect() =
        permissionProxy.hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
}

