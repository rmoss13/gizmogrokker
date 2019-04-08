package com.pillar.gizmogrokker.instrumented

import android.Manifest
import android.bluetooth.BluetoothAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.pillar.gizmogrokker.*
import com.pillar.gizmogrokker.detail.DeviceDetailActivity
import com.pillar.gizmogrokker.list.DeviceListFragment
import com.pillar.gizmogrokker.list.DeviceListActivity
import com.schibsted.spain.barista.interaction.PermissionGranter
import kotlinx.android.synthetic.main.device_list_activity.*
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.equalTo
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BluetoothDeviceTest {
    @get:Rule
    val activityRule = IntentsTestRule(DeviceListActivity::class.java)

    @Before
    fun setUp() {
        assumeTrue(BluetoothAdapter.getDefaultAdapter() != null)

        onView(withId(R.id.findDevices)).perform(click())

        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION)

        deviceListFragment().run {
            waitUntil(30_000) { deviceList.isNotEmpty() }
        }
    }

    @Test
    fun successfullyFindsAtLeastOneBluetoothDevice() {
        onView(withId(R.id.fragment)).check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun whenRecyclerViewItemIsClickedDeviceDetailActivityShows() {
        onView(withId(R.id.fragment))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        intended(hasComponent(DeviceDetailActivity::class.java.name))
        intended(
            hasExtra(equalTo("device"), instanceOf<BloothDevice>(BloothDevice::class.java))
        )
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