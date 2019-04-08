package com.pillar.gizmogrokker.instrumented

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.DeviceType
import com.pillar.gizmogrokker.R
import com.pillar.gizmogrokker.bluetoothclasses.BluetoothMajorClass
import com.pillar.gizmogrokker.bluetoothclasses.BluetoothMinorClass
import com.pillar.gizmogrokker.bluetoothclasses.BluetoothServiceClass
import com.pillar.gizmogrokker.detail.DeviceDetailFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DeviceDetailFragmentTest {

    @Test
    fun showsDeviceMacAddress() {
        val device = limitedDevice()
        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_mac_address)).check(matches(withText(device.macAddress)))
    }

    @Test
    fun showsDeviceName() {
        val device = BloothDevice(
            name = "Lucille",
            macAddress = "12:34:56:78",
            type = DeviceType.Classic,
            majorClass = null,
            minorClass = null,
            services = emptyList()
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_name)).check(matches(withText(device.name)))
    }

    @Test
    fun whenNoNameShowsNoNameMessage() {
        val fragmentArgs = Bundle().apply { putSerializable("device", limitedDevice()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_name)).check(matches(withText("Unknown")))
    }

    @Test
    fun showsDeviceType() {
        val device = BloothDevice(
            name = null,
            macAddress = "12:34:56:78",
            type = DeviceType.LE,
            majorClass = null,
            minorClass = null,
            services = emptyList()
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_type)).check(matches(withText("Low Energy")))
    }

    @Test
    fun showsMajorClass() {
        val device = BloothDevice(
            name = null,
            macAddress = "12:34:56:78",
            type = DeviceType.Classic,
            majorClass = BluetoothMajorClass.Toy,
            minorClass = null,
            services = emptyList()
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_major_class)).check(matches(withText("Toy")))
    }

    @Test
    fun whenNoMajorClassShowUnknown() {
        val fragmentArgs = Bundle().apply { putSerializable("device", limitedDevice()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_major_class)).check(matches(withText("Unknown")))
    }

    @Test
    fun showMinorClass() {
        val device = BloothDevice(
            name = null,
            macAddress = "12:34:56:78",
            type = DeviceType.Classic,
            majorClass = null,
            minorClass = BluetoothMinorClass.ToyUncategorized,
            services = emptyList()
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_minor_class)).check(matches(withText("Uncategorized")))
    }

    @Test
    fun whenNoMinorClassShowUnknown() {
        val fragmentArgs = Bundle().apply { putSerializable("device", limitedDevice()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_minor_class)).check(matches(withText("Unknown")))
    }

    @Test
    fun showsDeviceServices() {
        val device = BloothDevice(
            name = null,
            macAddress = "12:34:56:78",
            type = DeviceType.Classic,
            majorClass = null,
            minorClass = null,
            services = listOf(BluetoothServiceClass.Audio, BluetoothServiceClass.Capture)
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_services)).check(matches(withText("Audio, Capture")))
    }

    @Test
    fun whenNoDeviceServicesShowNone() {
        val fragmentArgs = Bundle().apply { putSerializable("device", limitedDevice()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_services)).check(matches(withText("None")))
    }

    private fun limitedDevice(): BloothDevice = BloothDevice(
        name = null,
        macAddress = "12:34:56:78",
        type = DeviceType.Classic,
        majorClass = null,
        minorClass = null,
        services = emptyList()
    )
}