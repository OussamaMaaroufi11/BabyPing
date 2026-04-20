package com.app.babyroutine.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object LocationPermissionHelper {

    const val FOREGROUND_LOCATION_REQUEST_CODE = 1001
    const val BACKGROUND_LOCATION_REQUEST_CODE = 1002

    fun hasForegroundLocationPermission(context: Context): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    fun hasBackgroundLocationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun hasAllRequiredLocationPermissions(context: Context): Boolean {
        return hasForegroundLocationPermission(context) &&
                hasBackgroundLocationPermission(context)
    }

    fun requestForegroundLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            FOREGROUND_LOCATION_REQUEST_CODE
        )
    }

    fun requestBackgroundLocationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_REQUEST_CODE
            )
        }
    }

    fun requestAllLocationPermissions(activity: Activity) {
        if (!hasForegroundLocationPermission(activity)) {
            requestForegroundLocationPermission(activity)
            return
        }

        if (!hasBackgroundLocationPermission(activity)) {
            requestBackgroundLocationPermission(activity)
        }
    }

    fun shouldRequestBackgroundPermission(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                hasForegroundLocationPermission(context) &&
                !hasBackgroundLocationPermission(context)
    }
}