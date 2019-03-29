package com.pillar.gizmogrokker.instrumented

import android.Manifest
import android.bluetooth.BluetoothAdapter
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.pillar.gizmogrokker.DeviceListFragment
import com.pillar.gizmogrokker.MainActivity
import com.pillar.gizmogrokker.R
import com.schibsted.spain.barista.interaction.PermissionGranter
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BluetoothDeviceTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        assumeTrue(BluetoothAdapter.getDefaultAdapter() != null)
    }

    @Test
    fun successfullyFindsAtLeastOneBluetoothDevice() {
        onView(withId(R.id.findDevices)).perform(click())

        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION)

        val deviceListFragment = deviceListFragment()
        waitUntil(30_000) { deviceListFragment.deviceList.isNotEmpty() }

        onView(withId(R.id.fragment)).check(matches(hasMinimumChildCount(1)))
    }

    private fun deviceListFragment() = activityRule.activity.fragment as DeviceListFragment

    private fun waitUntil(waitTime: Int, shouldContinue: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (!shouldContinue() && duration(start) < waitTime) {
            Thread.yield()
        }
    }

    private fun duration(start: Long) = System.currentTimeMillis() - start
}