package com.app.babyroutine.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.app.babyroutine.model.Routine
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceManager(
    private val context: Context
) {

    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    private fun hasRequiredLocationPermission(): Boolean {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasBackgroundLocation =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }

        return hasFineLocation && hasBackgroundLocation
    }

    private fun geofencePendingIntent(routine: Routine): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java).apply {
            putExtra(GeofenceBroadcastReceiver.EXTRA_ROUTINE_TITLE, routine.title)
        }

        return PendingIntent.getBroadcast(
            context,
            routine.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun addGeofenceForRoutine(routine: Routine) {
        val latitude = routine.latitude ?: return
        val longitude = routine.longitude ?: return

        if (!routine.notificationsEnabled) return
        if (!hasRequiredLocationPermission()) return

        val safeRadius = routine.radius.coerceIn(50f, 500f)

        val geofence = Geofence.Builder()
            .setRequestId(routine.id)
            .setCircularRegion(latitude, longitude, safeRadius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(
            geofencingRequest,
            geofencePendingIntent(routine)
        )
    }

    fun removeGeofenceForRoutine(routineId: String) {
        geofencingClient.removeGeofences(listOf(routineId))
    }

    private fun getAllPendingIntents(): List<PendingIntent> {
        return emptyList()
    }
}