package com.pillar.gizmogrokker

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
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

    private lateinit var viewModel: DeviceListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        viewModel = ViewModelProviders.of(this).get(DeviceListViewModel::class.java)

        updateViewState()
    }

    private fun updateViewState() {
        deviceListFragment.deviceList = viewModel.deviceList
        findDevices.apply {
            isEnabled = viewModel.buttonEnabled
            text = viewModel.buttonText
        }
    }

    private val deviceListFragment get() = fragment as DeviceListFragment

    fun View.onFindBloothDevicesClick() {
        ioScope.launch {
            bluetoothInterface.checkBluetoothState()
                .run {
                    when (this) {
                        Ready -> viewModel.deviceList = bluetoothInterface.discoverize()
                        MustRequest -> requestPermissions()
                        NoBlooth -> viewModel.run {
                            buttonEnabled = false
                            buttonText = getString(R.string.NoBloothText)
                        }
                    }
                }

            performUiUpdate { updateViewState() }
        }
    }

    private suspend fun performUiUpdate(block: suspend CoroutineScope.() -> Unit) {
        withContext(uiScope.coroutineContext, block)
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
                    PackageManager.PERMISSION_GRANTED -> permissionGranted()
                    else -> println("grant results ${grantResults[0]}")
                }

            }
        }
    }

    private fun permissionGranted() = ioScope.launch {
        viewModel.deviceList = bluetoothInterface.discoverize()
        performUiUpdate { updateViewState() }
    }

    private suspend fun BluetoothInterface.discoverize() = BluetoothDiscotech(this)
        .also { println("Starting Discovery") }
        .discoverize()
        .also { println("Discovery complete, $it") }

    private val bluetoothInterface by lazy {
        object : BluetoothInterface {
            override val bluetoothEnabled = DataEvent<Unit>()
            override val discoveryEnded = DataEvent<Unit>()
            override val deviceDiscovered = DataEvent<BloothDevice>()
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

class DeviceListViewModel(
    var deviceList: List<BloothDevice> = emptyList(),
    var buttonText: String = "Find Blooth Devices!",
    var buttonEnabled: Boolean = true
) : ViewModel()
