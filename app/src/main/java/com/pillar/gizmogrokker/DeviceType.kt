package com.pillar.gizmogrokker

enum class DeviceType(override val value: Int, val displayName: String) : IntEnum {
    Unknown(0, "Unknown"),
    Classic(1, "Classic"),
    LE(2, "Low Energy"),
    Dual(3, "Dual");

    companion object {
        fun fromInt(value: Int): DeviceType =
            IntEnum.fromInt(value) ?: DeviceType.Unknown
    }
}