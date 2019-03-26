package com.pillar.gizmogrokker

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionProxy(val appContext: Context) {
    fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(appContext, permission) ==
                PackageManager.PERMISSION_GRANTED
}