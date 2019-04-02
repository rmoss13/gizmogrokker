package com.pillar.gizmogrokker.ui.devicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.R
import kotlinx.android.synthetic.main.device_detail_fragment.view.*

class DeviceDetailFragment : Fragment() {
    private val device get() = arguments?.getSerializable("device") as BloothDevice

    companion object {
        const val NO_NAME_MSG = "Connect to this device to get its name"
        const val UNKNOWN = "Unknown"
    }

    private lateinit var viewModel: DeviceDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.device_detail_fragment, container, false)
        .apply {
            device.run {
                device_mac_address.text = macAddress
                device_name.text = name()
                device_type.text = displayType()
                device_major_class.text = majorClass()
                device_minor_class.text = minorClass()
                device_services.text = services()
            }
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeviceDetailViewModel::class.java)
    }

    fun BloothDevice.name() : String = device.name ?: NO_NAME_MSG
    fun BloothDevice.displayType() : String = device.type.displayName
    fun BloothDevice.majorClass() : String = device.majorClass?.toString() ?: UNKNOWN
    fun BloothDevice.minorClass() : String = device.minorClass?.displayName() ?: UNKNOWN
    fun BloothDevice.services() : String = device.services.ifEmpty { listOf("None") }.joinToString(", ")
}
