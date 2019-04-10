package com.pillar.gizmogrokker.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.R
import kotlinx.android.synthetic.main.device_detail_fragment.view.*

class DeviceDetailFragment : Fragment() {
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

    private fun name(): String = device.name ?: unknown
    private fun displayType(): String = device.type.displayName
    private fun majorClass(): String = device.majorClass?.toString() ?: unknown
    private fun minorClass(): String = device.minorClass?.displayName() ?: unknown
    private fun services(): String =
        device.services.ifEmpty { listOf("None") }.joinToString(", ")
}

class DeviceDetailViewModel(var device : BloothDevice? = null) : ViewModel()