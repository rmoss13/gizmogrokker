package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

typealias DeviceCallback = (BloothDevice) -> Unit
typealias EnabledCallback = (Unit) -> Unit
typealias DiscoveryEndedCallback = (Unit) -> Unit

abstract class BluetoothInterface {

    abstract val adapter: BluetoothAdapter?
    abstract val context: Context
    val bluetoothEnabled: Event<Unit, EnabledCallback> = DataEvent()
    open val discoveryEnded: Event<Unit, DiscoveryEndedCallback> = DataEvent()
    open val deviceDiscovered: Event<BloothDevice, DeviceCallback> = DataEvent()

    val hasBluetoothSupport: Boolean get() = adapter != null
    open val isEnabled: Boolean get() = adapter?.isEnabled ?: false

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> handleStateChanged()
                BluetoothDevice.ACTION_FOUND -> intent.handleFound()
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> context.handleDiscoveryFinished()
            }
        }

        private fun Context.handleDiscoveryFinished() {
            unregisterReceiver(this)
                .also { discoveryEnded.eventOccurred(Unit) }
        }

        private fun handleStateChanged() {
            bluetoothEnabled.eventOccurred(Unit)
        }

        private fun Intent.handleFound() {
            bluetoothDevice()
                .bloothDevice()
                .let { deviceDiscovered.eventOccurred(it) }
        }

        private fun BluetoothDevice.bloothDevice() = BloothDevice(
            name = name,
            macAddress = address
        )

        private fun Intent.bluetoothDevice(): BluetoothDevice =
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

    }

    private fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(broadcastReceiver)
    }

    open fun enable() {
        adapter?.enable()
    }

    open fun startDiscovery() {
        adapter?.startDiscovery()
        val stateChanged = BluetoothAdapter.ACTION_STATE_CHANGED
        val filter = IntentFilter(stateChanged)
            .apply { addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) }
            .apply { addAction(BluetoothDevice.ACTION_FOUND) }
        context.registerReceiver(broadcastReceiver, filter)
    }

}

interface Event<I, T : (I) -> Unit> {
    operator fun plus(listener: T)
    operator fun minus(listener: T)
    fun eventOccurred(input: I)
}

class DataEvent<I, T : (I) -> Unit> : Event<I, T> {

    private var listenerList = listOf<T>()

    override fun plus(listener: T) {
        listenerList = listenerList + listener
    }

    override fun minus(listener: T) {
        listenerList = listenerList - listener
    }

    override fun eventOccurred(input: I) {
        listenerList.toList().forEach { it.invoke(input) }
    }

}
