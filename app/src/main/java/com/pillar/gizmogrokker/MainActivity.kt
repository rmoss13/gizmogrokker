package com.pillar.gizmogrokker

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pillar.gizmogrokker.GrokkerBluetoothState.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

const val REQUEST_BLUETOOTH_PERMISSIONS = 1

class MainActivity : AppCompatActivity() {

    companion object {
        private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private lateinit var job: Job
    private val ioScope get() = CoroutineScope(Dispatchers.IO + job)
    private val uiScope get() = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
    }

    fun View.onFindBloothDevicesClick() {
        ioScope.launch {
            bluetoothInterface.checkBluetoothState()
                .run {
                    when (this) {
                        Ready -> bluetoothInterface.discoverizeAndUpdateUi()
                        MustRequest -> requestPermissions()
                        NoBlooth -> performUiUpdate {
                            findDevices.run {
                                isEnabled = false
                                text = getString(R.string.NoBloothText)
                            }
                        }
                    }
                }
        }
    }

    private fun BluetoothInterface.discoverizeAndUpdateUi() = ioScope.launch {
        val deviceList = discoverize()
        performUiUpdate {
            showDiscoveredDevices(deviceList)
        }
    }

    private suspend fun performUiUpdate(block: suspend CoroutineScope.() -> Unit) {
        withContext(uiScope.coroutineContext, block)
    }

    private fun showDiscoveredDevices(deviceList: MutableList<BloothDevice>) {
        val deviceListFragment: DeviceListFragment = fragment as DeviceListFragment
        deviceListFragment.deviceList = deviceList
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            REQUEST_BLUETOOTH_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_BLUETOOTH_PERMISSIONS -> {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> bluetoothInterface.discoverizeAndUpdateUi()
                    else -> println("grant results ${grantResults[0]}")
                }
            }
        }
    }

    private suspend fun BluetoothInterface.discoverize() = BluetoothDiscotech(this)
        .also { println("Starting Discovery") }
        .discoverize()
        .also { println("Discovery complete, $it") }

    private val bluetoothInterface by lazy {
        object : BluetoothInterface() {
            override val context: Context get() = applicationContext
            override val adapter = BluetoothAdapter.getDefaultAdapter()
        }
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
