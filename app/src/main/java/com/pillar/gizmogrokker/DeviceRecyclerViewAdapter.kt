package com.pillar.gizmogrokker


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_device.view.*

class DeviceRecyclerViewAdapter(
    private val deviceList: List<BloothDevice>
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = deviceList[position]
        with(holder) {
            deviceLabel.text = device.name ?: device.macAddress
            deviceView.tag = device
        }
    }

    override fun getItemCount(): Int = deviceList.size

    inner class ViewHolder(val deviceView: View) : RecyclerView.ViewHolder(deviceView) {
        val deviceLabel: TextView = deviceView.deviceLabel

        override fun toString(): String {
            return "${super.toString()} '${deviceLabel.text}'"
        }
    }
}
