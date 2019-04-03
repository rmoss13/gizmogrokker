package com.pillar.gizmogrokker.bluetoothclasses

import com.pillar.gizmogrokker.IntEnum

enum class BluetoothMajorClass(override val value: Int) : IntEnum {
    AV(1024),
    Computer(256),
    Health(2304),
    Imaging(1536),
    Misc(0),
    Networking(768),
    Peripheral(1280),
    Phone(512),
    Toy(2048),
    Uncategorized(7936),
    Wearable(1792);

    companion object {
        fun fromInt(value: Int): BluetoothMajorClass? =
            IntEnum.fromInt(value)
    }
}