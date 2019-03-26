package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
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

    val hasBluetoothSupport: Boolean get() = adapter != null
    open val isEnabled: Boolean get() = adapter?.isEnabled ?: false

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> unregisterReceiver(context)
                    .also {
                        discoveryEndedCallback?.invoke()
                    }
                BluetoothAdapter.ACTION_STATE_CHANGED -> enabledCallback?.invoke()
            }
        }
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
        context.registerReceiver(broadcastReceiver, filter)
    }


    open fun registerEnabled(callback: EnabledCallback) {
        this.enabledCallback = callback
    }

    open fun unregisterEnabled(callback: EnabledCallback) {
        this.enabledCallback = null
    }

    open fun registerDeviceDiscovered(callback: DeviceCallback) {
    }

    open fun registerDiscoveryEnded(callback: DiscoveryEndedCallback) {
        this.discoveryEndedCallback = callback
    }

}
