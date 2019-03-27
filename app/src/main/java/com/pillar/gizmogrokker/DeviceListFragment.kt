package com.pillar.gizmogrokker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_device_list.*

class DeviceListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflateView<RecyclerView>(inflater, container, R.layout.fragment_device_list)
        .apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DeviceRecyclerViewAdapter(emptyList())
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T : View> inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layout: Int
    ): T = inflater.inflate(layout, container, false) as T

    fun setDeviceList(deviceList: List<BloothDevice>) {
        list.adapter = DeviceRecyclerViewAdapter(deviceList)
    }

}
