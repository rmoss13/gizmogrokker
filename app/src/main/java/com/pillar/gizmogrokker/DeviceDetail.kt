package com.pillar.gizmogrokker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pillar.gizmogrokker.ui.devicedetail.DeviceDetailFragment

class DeviceDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_detail_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DeviceDetailFragment())
                .commitNow()
        }
    }

}
