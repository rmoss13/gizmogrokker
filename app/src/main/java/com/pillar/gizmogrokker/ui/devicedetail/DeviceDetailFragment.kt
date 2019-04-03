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
    private val noNameMessage = "Connect to this device to get its name"
    private val unknown = "Unknown"
    private val device get() = arguments?.getSerializable("device") as BloothDevice

    private lateinit var viewModel: DeviceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeviceDetailViewModel::class.java)
        viewModel.device = device
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.device_detail_fragment, container, false)
        .apply {
            viewModel.device?.run {
                device_mac_address.text = macAddress
                device_name.text = name()
                device_type.text = displayType()
                device_major_class.text = majorClass()
                device_minor_class.text = minorClass()
                device_services.text = services()
            }
        }

    private fun BloothDevice.name(): String = device.name ?: noNameMessage
    private fun BloothDevice.displayType(): String = device.type.displayName
    private fun BloothDevice.majorClass(): String = device.majorClass?.toString() ?: unknown
    private fun BloothDevice.minorClass(): String = device.minorClass?.displayName() ?: unknown
    private fun BloothDevice.services(): String =
        device.services.ifEmpty { listOf("None") }.joinToString(", ")

    companion object {
        fun create(device: BloothDevice) : DeviceDetailFragment =
            DeviceDetailFragment().apply {
                arguments = Bundle().apply { putSerializable("device", device) }
            }
    }
}
