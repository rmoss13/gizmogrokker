package com.pillar.gizmogrokker.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.R

class DeviceListFragment : Fragment() {
    private val recyclerView get() = view as RecyclerView

    var deviceList: List<BloothDevice> = emptyList()
        set(value) {
            field = value
            recyclerView.adapter =
                DeviceRecyclerViewAdapter(deviceList)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflateView<RecyclerView>(inflater, container,
        R.layout.fragment_device_list
    )
        .apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DeviceRecyclerViewAdapter(deviceList)
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T : View> inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layout: Int
    ): T = inflater.inflate(layout, container, false) as T
}
