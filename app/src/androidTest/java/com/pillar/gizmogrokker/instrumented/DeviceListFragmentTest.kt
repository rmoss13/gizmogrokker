package com.pillar.gizmogrokker.instrumented

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.DeviceListFragment
import com.pillar.gizmogrokker.R
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DeviceListFragmentTest {

    @Test
    fun startsWithEmptyList() {
        val fragmentArgs = Bundle()
        launchFragmentInContainer<DeviceListFragment>(fragmentArgs)
        onView(withId(R.id.list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun canBeUpdatedWithPopulatedList() {
        val fragmentArgs = Bundle()
        val scenario = launchFragmentInContainer<DeviceListFragment>(fragmentArgs)
        val deviceList: List<BloothDevice> = listOf(
            BloothDevice(name = "Natalie's Awesome Headphones", macAddress = "Heaven"),
            BloothDevice(name = null, macAddress = "Hell")
        )

        scenario.onFragment { it.setDeviceList(deviceList) }
        onView(withId(R.id.list)).check(matches(hasChildCount(deviceList.size)))
    }
}