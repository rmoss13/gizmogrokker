package com.pillar.gizmogrokker

import com.pillar.gizmogrokker.bluetoothclasses.BluetoothMajorClass
import com.pillar.gizmogrokker.bluetoothclasses.BluetoothMinorClass
import com.pillar.gizmogrokker.bluetoothclasses.BluetoothServiceClass
import java.io.Serializable

data class BloothDevice(
    val name: String?,
    val macAddress: String,
    val type: DeviceType,
    val majorClass: BluetoothMajorClass?,
    val minorClass: BluetoothMinorClass?,
    val services: List<BluetoothServiceClass>
) : Serializable
