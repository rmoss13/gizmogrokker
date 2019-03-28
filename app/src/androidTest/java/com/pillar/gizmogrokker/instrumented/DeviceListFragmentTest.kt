package com.pillar.gizmogrokker.instrumented

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.pillar.gizmogrokker.BloothDevice
import com.pillar.gizmogrokker.DeviceListFragment
import com.pillar.gizmogrokker.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DeviceListFragmentTest {
    private lateinit var scenario: FragmentScenario<DeviceListFragment>

    @Before
    fun setUp() {
        val fragmentArgs = Bundle()
        scenario = launchFragmentInContainer<DeviceListFragment>(fragmentArgs)
    }

    @Test
    fun startsWithEmptyList() {
        onView(withId(R.id.list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun canBeUpdatedWithPopulatedList() {
        val deviceList: List<BloothDevice> = listOf(
            BloothDevice(name = "Natalie's Awesome Headphones", macAddress = "Heaven"),
            BloothDevice(name = null, macAddress = "Hell")
        )

        scenario.onFragment { it.deviceList = deviceList }
        onView(withId(R.id.list)).check(matches(hasChildCount(deviceList.size)))
    }

    @Test
    fun whenNameNotNullShowName() {
        val name = "Smart Sharpie"
        scenario.onFragment {
            it.deviceList = listOf(BloothDevice(name = name, macAddress = "???"))
        }
        onView(withText(name)).check(matches(isDisplayed()))
    }

    @Test
    fun whenNoNameShowMacAddress() {
        val macAddress = "Tron"
        scenario.onFragment {
            it.deviceList = listOf(BloothDevice(name = null, macAddress = macAddress))
        }
        onView(withText(macAddress)).check(matches(isDisplayed()))
    }
}