package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pillar.gizmogrokker.GrokkerBluetoothState.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var job: Job
    private val ioScope get() = CoroutineScope(Dispatchers.IO + job)
    private val uiScope get() = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
        findDevices.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View) {
        ioScope.launch {
            val bluetoothInterface: BluetoothInterface = bluetoothInterface()

            val deviceList: List<BloothDevice> = bluetoothInterface.checkBluetoothState()
                .run {
                    when (this) {
                        Ready -> bluetoothInterface.discoverize()
                        MustRequest -> listOf<BloothDevice>().also { println("must request") }
                        NoBlooth -> listOf<BloothDevice>().also { println("no blooth") }
                    }
                }

            withContext(uiScope.coroutineContext) {
                val deviceListFragment: DeviceListFragment = fragment as DeviceListFragment
                deviceListFragment.deviceList = deviceList
                findDevices.text = "Do it again!"
            }
        }
    }

    private suspend fun BluetoothInterface.discoverize() = BluetoothDiscotech(this)
        .also { println("Starting Discovery") }
        .discoverize()
        .also { println("Discovery complete, $it") }

    private fun bluetoothInterface() = object : BluetoothInterface() {
        override val context: Context get() = applicationContext
        override val adapter = BluetoothAdapter.getDefaultAdapter()
    }

    private fun BluetoothInterface.checkBluetoothState() = BluetoothPermissionChecker(
        this,
        PermissionProxy(applicationContext)
    )
        .checkBluetoothState()

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
