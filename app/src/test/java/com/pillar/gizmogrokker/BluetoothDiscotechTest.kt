package com.pillar.gizmogrokker

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BluetoothDiscotechTest {

    @Test
    fun whenBluetoothIsEnabledShouldStartDiscovery() {
        val bluetoothInterface = mock<BluetoothInterface> {
            on { isEnabled }.thenReturn(true)
        }

        val discotech = BluetoothDiscotech(bluetoothInterface)

        discotech.discoverize()

        verify(bluetoothInterface).startDiscovery()
    }

    @Test
    fun whenBluetoothIsDisabledShouldEnableAndStartDiscovery() {
        val bluetoothInterface = FakeBluetoothInterface()
        val discotech = BluetoothDiscotech(bluetoothInterface)
        discotech.discoverize()

        bluetoothInterface.apply {
            assertEquals(1, enableCount)
            assertEquals(0, discoCount)
            invokeCallbacks()
            assertEquals(1, discoCount)
        }
    }

    @Test
    fun afterEnablingWillNotDiscoverEachTimeBluetoothIsEnabled() {
        val bluetoothInterface = FakeBluetoothInterface()

        val discotech = BluetoothDiscotech(bluetoothInterface)
        discotech.discoverize()

        bluetoothInterface.apply {
            invokeCallbacks()
            invokeCallbacks()
            invokeCallbacks()
            invokeCallbacks()

            assertEquals(1, discoCount)
        }
    }
}

class FakeBluetoothInterface : BluetoothInterface {
    val callbackList = mutableListOf<() -> Unit>()
    var discoCount: Int = 0
    var enableCount: Int = 0

    override val isEnabled: Boolean = false

    override fun enable() {
        enableCount++
    }

    override fun registerForEnabled(callback: () -> Unit) {
        callbackList += callback
    }

    override fun unregister(callback: () -> Unit) {
        callbackList -= callback
    }

    fun invokeCallbacks() = callbackList.toList().forEach { it() }

    override fun startDiscovery() {
        discoCount++
    }
}