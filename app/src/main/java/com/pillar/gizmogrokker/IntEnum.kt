package com.pillar.gizmogrokker

interface IntEnum {
    val value: Int

    companion object {
        inline fun <reified T> fromInt(value: Int): T? where T : Enum<T>, T : IntEnum =
            enumValues<T>().firstOrNull {
                it.value == value
            }
    }
}