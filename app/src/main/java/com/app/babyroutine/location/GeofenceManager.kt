package com.app.babyroutine.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val background = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine && background
    }

    private fun geofencePendingIntent(routineTitle: String): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java).apply {
            putExtra("routine_title", routineTitle)
        }

        return PendingIntent.getBroadcast(
            context,
            routineTitle.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun addGeofenceForRoutine(routine: Routine) {
        val latitude = routine.latitude ?: return
        val longitude = routine.longitude ?: return

        if (!hasLocationPermission()) return

        val geofence = Geofence.Builder()
            .setRequestId(routine.id)
            .setCircularRegion(latitude, longitude, routine.radius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(
            geofencingRequest,
            geofencePendingIntent(routine.title)
        )
    }

    fun removeGeofenceForRoutine(routineId: String) {
        geofencingClient.removeGeofences(listOf(routineId))
    }
}