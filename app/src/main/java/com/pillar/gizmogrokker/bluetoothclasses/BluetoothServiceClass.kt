package com.pillar.gizmogrokker.bluetoothclasses

import android.bluetooth.BluetoothClass

enum class BluetoothServiceClass(val value: Int) {
    Audio(2097152),
    Capture(524288),
    Information(8388608),
    Limited(8192),
    Networking(131072),
    Transfer(1048576),
    Positioning(65536),
    Rendering(262144),
    Telephony(4194304);

    companion object {
        fun getAvailableServices(btClass: BluetoothClass): List<BluetoothServiceClass> =
            BluetoothServiceClass.values().fold(mutableListOf()) { acc, serviceClass ->
                acc.apply {
                    if (btClass.hasService(serviceClass.value)) {
                        add(serviceClass)
                    }
                }
            }
    }
}