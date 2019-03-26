package com.pillar.gizmogrokker

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillar.gizmogrokker.GrokkerBluetoothState.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findDevices.setOnClickListener {
            val bluetoothInterface = object : BluetoothInterface() {
                override val context: Context get() = this@MainActivity
                override val adapter = BluetoothAdapter.getDefaultAdapter()
            }

            val grokkerBluetoothState = BluetoothPermissionChecker(
                bluetoothInterface,
                PermissionProxy(this)
            ).checkBluetoothState()
            println("state $grokkerBluetoothState")

            GlobalScope.launch(Dispatchers.IO) {
                when (grokkerBluetoothState) {
                    Ready -> BluetoothDiscotech(bluetoothInterface)
                        .apply { println("Starting Discovery") }
                        .discoverize()
                        .apply { println("Discovery complete, $this") }
                    MustRequest -> TODO()
                    NoBlooth -> TODO()
                }
            }
        }
    }
}
