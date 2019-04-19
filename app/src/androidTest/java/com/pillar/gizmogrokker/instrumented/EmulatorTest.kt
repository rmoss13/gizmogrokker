package com.pillar.gizmogrokker.instrumented

import android.bluetooth.BluetoothAdapter
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.pillar.gizmogrokker.R
import com.pillar.gizmogrokker.list.DeviceListActivity
import com.zegreatrob.testmints.setup
import org.hamcrest.Matchers.not
import org.junit.Assume
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class EmulatorTest {

    private val findDevicesButton get() = onView(ViewMatchers.withId(R.id.findDevices))

    @get:Rule
    val activityRule = IntentsTestRule(DeviceListActivity::class.java)

    @Before
    fun onlyRunWhenNoBluetoothIsAvailable() {
        Assume.assumeTrue(BluetoothAdapter.getDefaultAdapter() == null)
    }

    @Test
    fun whenNoBluetoothShouldDisableFindDevicesButton(): Unit = setup(object {}) exercise {
        findDevicesButton.perform(ViewActions.click())
    } verify {
        waitUntilAssert(5_000) {
            findDevicesButton.check(matches(withText("Emulator n00b")))
        }

        findDevicesButton.check(matches(not(isEnabled())))
    }

}