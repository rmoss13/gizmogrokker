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
        val device = minimumValidBlooth()
        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_mac_address)).check(matches(withText(device.macAddress)))
    }

    @Test
    fun showsDeviceName() {
        val device = stubBloothDevice(name = "Lucille")

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_name)).check(matches(withText(device.name)))
    }

    @Test
    fun whenNoNameShowsNoNameMessage() {
        val fragmentArgs = Bundle().apply { putSerializable("device", minimumValidBlooth()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_name)).check(matches(withText("Unknown")))
    }

    @Test
    fun showsDeviceType() {
        val device = stubBloothDevice(type = DeviceType.LE)

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_type)).check(matches(withText("Low Energy")))
    }

    @Test
    fun showsMajorClass() {
        val device = stubBloothDevice(majorClass = BluetoothMajorClass.Toy)

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_major_class)).check(matches(withText("Toy")))
    }

    @Test
    fun whenNoMajorClassShowUnknown() {
        val fragmentArgs = Bundle().apply { putSerializable("device", minimumValidBlooth()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_major_class)).check(matches(withText("Unknown")))
    }

    @Test
    fun showMinorClass() {
        val device = stubBloothDevice(minorClass = BluetoothMinorClass.ToyUncategorized)

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_minor_class)).check(matches(withText("Uncategorized")))
    }

    @Test
    fun whenNoMinorClassShowUnknown() {
        val fragmentArgs = Bundle().apply { putSerializable("device", minimumValidBlooth()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_minor_class)).check(matches(withText("Unknown")))
    }

    @Test
    fun whenServicesAreAvailableShowsDeviceServices() {
        val device = stubBloothDevice(
            services = listOf(BluetoothServiceClass.Audio, BluetoothServiceClass.Capture)
        )

        val fragmentArgs = Bundle().apply { putSerializable("device", device) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_services)).check(matches(withText("Audio, Capture")))
    }

    @Test
    fun whenNoDeviceServicesAreAvailableShowUnknown() {
        val fragmentArgs = Bundle().apply { putSerializable("device", minimumValidBlooth()) }
        launchFragmentInContainer<DeviceDetailFragment>(fragmentArgs)
        onView(withId(R.id.device_services)).check(matches(withText("Unknown")))
    }

    private fun minimumValidBlooth(): BloothDevice = BloothDevice(
        name = null,
        macAddress = "12:34:56:78",
        type = DeviceType.Classic,
        majorClass = null,
        minorClass = null,
        services = emptyList()
    )
}


fun stubBloothDevice(
    name: String? = "Buster",
    macAddress: String = "Big",
    type: DeviceType = DeviceType.LE,
    majorClass: BluetoothMajorClass? = BluetoothMajorClass.Toy,
    minorClass: BluetoothMinorClass? = BluetoothMinorClass.DollActionFigure,
    services: List<BluetoothServiceClass> = listOf(
        BluetoothServiceClass.Audio,
        BluetoothServiceClass.Capture
    )
): BloothDevice = BloothDevice(
    name = name,
    macAddress = macAddress,
    type = type,
    majorClass = majorClass,
    minorClass = minorClass,
    services = services
)