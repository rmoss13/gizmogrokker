package com.pillar.gizmogrokker.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.databinding.DeviceDetailFragmentBinding
import java.io.Serializable

class DeviceDetailFragment : Fragment() {
    private var _binding: DeviceDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val unknown = "Unknown"
    private val device: BloothDevice? get() = arguments?.serializable("device")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeviceDetailFragmentBinding.inflate(inflater, container, false)
            .apply { updateUIElements() }
        val view = binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        binding.connectDevice.setOnClickListener { }
    }

    private fun updateUIElements() = device?.apply {
        binding.deviceMacAddress.text = macAddress()
        binding.deviceName.text = name()
        binding.deviceType.text = displayType()
        binding.deviceMajorClass.text = majorClass()
        binding.deviceMinorClass.text = minorClass()
        binding.deviceServices.text = services()
    }

    private fun BloothDevice.macAddress() = macAddress
    private fun BloothDevice.name(): String = name ?: unknown
    private fun BloothDevice.displayType(): String = type.displayName
    private fun BloothDevice.majorClass(): String = majorClass?.toString() ?: unknown
    private fun BloothDevice.minorClass(): String = minorClass?.displayName() ?: unknown
    private fun BloothDevice.services(): String =
        services.ifEmpty { listOf(unknown) }.joinToString(", ")

}

@Suppress("UNCHECKED_CAST")
private fun <T : Serializable> Bundle.serializable(s: String) = getSerializable(s) as T