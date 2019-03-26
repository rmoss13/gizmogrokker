package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BluetoothDiscotechTest {

    @Test
    fun whenBluetoothIsEnabledShouldStartDiscovery() = runBlocking {
        val bluetoothInterface = mock<BluetoothInterface> {
            on { isEnabled }.thenReturn(true)
            on { registerDiscoveryEnded(any()) }
                .thenAnswer { it.getArgument<DiscoveryEndedCallback>(0).invoke() }
        }

        val discotech = BluetoothDiscotech(bluetoothInterface)

        discotech.discoverize()

        verify(bluetoothInterface).startDiscovery()
    }.let { Unit }

    @Test
    fun whenBluetoothIsDisabledShouldEnableAndStartDiscovery() = runBlocking {
        val bluetoothInterface = FakeBluetoothInterface()
        val discotech = BluetoothDiscotech(bluetoothInterface)
        discotech.discoverize()

        bluetoothInterface.apply {
            assertEquals(1, enableCount)
            assertEquals(0, discoCount)
            invokeEnabledCallbacks()
            assertEquals(1, discoCount)
        }
    }.let { Unit }

    @Test
    fun afterEnablingWillNotDiscoverEachTimeBluetoothIsEnabled() = runBlocking {
        val bluetoothInterface = FakeBluetoothInterface()

        val discotech = BluetoothDiscotech(bluetoothInterface)
        discotech.discoverize()

        bluetoothInterface.apply {
            invokeEnabledCallbacks()
            invokeEnabledCallbacks()
            invokeEnabledCallbacks()
            invokeEnabledCallbacks()

            assertEquals(1, discoCount)
        }
    }.let { Unit }

    @Test
    fun willReturnListContainingBluetoothDevices() = runBlocking {
        val bluetoothInterface = FakeBluetoothInterface(autoFinishDiscovery = false)

        val discotech = BluetoothDiscotech(bluetoothInterface)

        val discoveredDevicesDeferred = async { discotech.discoverize() }

        val devices = listOf(
            BloothDevice(name = "Banana", macAddress = "38-F9-D3-3E-3E-D3"),
            BloothDevice(name = null, macAddress = "10-94-BB-B5-F4-AA")
        )

        runBlocking {
            devices.forEach { bluetoothInterface.invokeDeviceCallbacks(it) }
            bluetoothInterface.invokeDiscoveryEnded()
        }

        val actualDevices = discoveredDevicesDeferred.await()
        assertEquals(devices, actualDevices)
    }.let { Unit }
}

class FakeBluetoothInterface(val autoFinishDiscovery: Boolean = true) : BluetoothInterface {
    override val adapter: BluetoothAdapter get() = throw NotImplementedError("Will not implement.")
    val enabledCallbackList = mutableListOf<EnabledCallback>()
    val deviceCallbackList = mutableListOf<DeviceCallback>()
    val discoveryEndedCallbackList = mutableListOf<DiscoveryEndedCallback>()
    var discoCount: Int = 0
    var enableCount: Int = 0

    override val isEnabled: Boolean = false

    override fun enable() {
        enableCount++
    }

    override fun registerEnabled(callback: EnabledCallback) {
        enabledCallbackList += callback
    }

    override fun unregisterEnabled(callback: EnabledCallback) {
        enabledCallbackList -= callback
    }

    override fun registerDeviceDiscovered(callback: DeviceCallback) {
        deviceCallbackList += callback
    }

    override fun registerDiscoveryEnded(callback: DiscoveryEndedCallback) {
        if (autoFinishDiscovery) {
            callback()
        } else {
            discoveryEndedCallbackList += callback
        }
    }

    fun invokeEnabledCallbacks() = enabledCallbackList.toList().forEach { it() }
    fun invokeDeviceCallbacks(newDevice: BloothDevice) =
        deviceCallbackList.toList().forEach { it(newDevice) }

    override fun startDiscovery() {
        discoCount++
    }

    fun invokeDiscoveryEnded() {
        discoveryEndedCallbackList.toList().forEach { it.invoke() }
    }
}