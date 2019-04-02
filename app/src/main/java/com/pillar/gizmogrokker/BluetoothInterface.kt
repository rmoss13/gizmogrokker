package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

typealias DeviceCallback = (BloothDevice) -> Unit
typealias EnabledCallback = () -> Unit
typealias DiscoveryEndedCallback = () -> Unit

abstract class BluetoothInterface {

    abstract val adapter: BluetoothAdapter?
    abstract val context: Context
    private var enabledCallback: EnabledCallback? = null
    private var discoveryEndedCallback: DiscoveryEndedCallback? = null
    private var deviceDiscoveredCallback: DeviceCallback? = null

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
                .also { discoveryEndedCallback?.invoke() }
        }

        private fun handleStateChanged() {
            enabledCallback?.invoke()
        }

        private fun Intent.handleFound() {
            bluetoothDevice()
                .bloothDevice()
                .let { deviceDiscoveredCallback?.invoke(it) }
        }

        private fun BluetoothDevice.bloothDevice() = BloothDevice(
            name = name,
            macAddress = address,
            type = DeviceType.fromInt(type),
            majorClass = MajorClass.fromInt(bluetoothClass.majorDeviceClass),
            minorClass = MinorClass.fromInt(bluetoothClass.deviceClass),
            services = DeviceService.getAvailableServices(bluetoothClass)
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
        if (adapter?.isDiscovering == true) {
            adapter?.cancelDiscovery()
        }

        adapter?.startDiscovery()
        val stateChanged = BluetoothAdapter.ACTION_STATE_CHANGED
        val filter = IntentFilter(stateChanged)
            .apply {
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        context.registerReceiver(broadcastReceiver, filter)
    }


    open fun registerEnabled(callback: EnabledCallback) {
        this.enabledCallback = callback
    }

    open fun unregisterEnabled(callback: EnabledCallback) {
        this.enabledCallback = null
    }

    open fun registerDeviceDiscovered(callback: DeviceCallback) {
        this.deviceDiscoveredCallback = callback
    }

    open fun registerDiscoveryEnded(callback: DiscoveryEndedCallback) {
        this.discoveryEndedCallback = callback
    }

}

