package com.pillar.gizmogrokker.list


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.databinding.DeviceFragmentBinding

class DeviceRecyclerViewAdapter(
    private val deviceList: List<BloothDevice>
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = DeviceFragmentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]
        with(holder) {
            deviceLabel.text = device.name ?: device.macAddress
        }
    }

    override fun getItemCount(): Int = deviceList.size

    inner class DeviceViewHolder(deviceView: DeviceFragmentBinding) :
        RecyclerView.ViewHolder(deviceView.root) {
        val deviceLabel: TextView = deviceView.deviceLabel

        override fun toString(): String {
            return "${super.toString()} '${deviceLabel.text}'"
        }
    }
}
