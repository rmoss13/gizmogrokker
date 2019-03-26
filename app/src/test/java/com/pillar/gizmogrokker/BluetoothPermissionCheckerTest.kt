package com.pillar.gizmogrokker

import android.Manifest
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class BluetoothPermissionCheckerTest {

    @Test
    fun whenPermissionsAlreadyExistReturnReady() {
        val checker = BluetoothPermissionChecker(
            bluetoothInterface(hasBluetoothSupport = true),
            permissionProxy(hasFineAccess = true)
        )

        val state = checker.checkBluetoothState()

        assertEquals(GrokkerBluetoothState.Ready, state)
    }

    @Test
    fun whenPermissionDoesNotExistReturnMustRequest() {
        val checker = BluetoothPermissionChecker(
            bluetoothInterface(hasBluetoothSupport = true),
            permissionProxy(hasFineAccess = false)
        )
        val state = checker.checkBluetoothState()
        assertEquals(GrokkerBluetoothState.MustRequest, state)
    }

    @Test
    fun whenNoBluetoothSupportWillReturnNoBlooth() {
        val checker = BluetoothPermissionChecker(
            bluetoothInterface(hasBluetoothSupport = false),
            mock()
        )
        val state = checker.checkBluetoothState()
        assertEquals(GrokkerBluetoothState.NoBlooth, state)
    }

    private fun permissionProxy(hasFineAccess: Boolean): PermissionProxy = mock {
        on { hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) }
            .thenReturn(hasFineAccess)
    }

    private fun bluetoothInterface(hasBluetoothSupport: Boolean): BluetoothInterface = mock {
        on { this.hasBluetoothSupport }
            .thenReturn(hasBluetoothSupport)
    }
}
