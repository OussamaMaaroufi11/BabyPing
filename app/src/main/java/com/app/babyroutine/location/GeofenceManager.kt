package com.app.babyroutine.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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

    private fun hasForegroundLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun hasRequiredLocationPermission(): Boolean {
        return hasForegroundLocationPermission() && hasBackgroundLocationPermission()
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
        val latitude = routine.latitude ?: run {
            Log.w(TAG, "Geofence ignorée : latitude absente pour ${routine.id}")
            return
        }

        val longitude = routine.longitude ?: run {
            Log.w(TAG, "Geofence ignorée : longitude absente pour ${routine.id}")
            return
        }

        if (!routine.notificationsEnabled) {
            Log.d(TAG, "Geofence ignorée : notifications désactivées pour ${routine.id}")
            return
        }

        if (!hasRequiredLocationPermission()) {
            Log.w(TAG, "Permissions de localisation insuffisantes pour ${routine.id}")
            return
        }

        val safeRadius = routine.radius.coerceIn(MIN_RADIUS_METERS, MAX_RADIUS_METERS)

        val geofence = Geofence.Builder()
            .setRequestId(routine.id)
            .setCircularRegion(latitude, longitude, safeRadius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(0)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient
            .addGeofences(geofencingRequest, geofencePendingIntent(routine))
            .addOnSuccessListener {
                Log.d(TAG, "Geofence ajoutée avec succès pour ${routine.id}")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Échec ajout geofence pour ${routine.id}", exception)
            }
    }

    fun removeGeofenceForRoutine(routineId: String) {
        geofencingClient
            .removeGeofences(listOf(routineId))
            .addOnSuccessListener {
                Log.d(TAG, "Geofence supprimée avec succès pour $routineId")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Échec suppression geofence pour $routineId", exception)
            }
    }

    fun refreshGeofenceForRoutine(routine: Routine) {
        removeGeofenceForRoutine(routine.id)
        addGeofenceForRoutine(routine)
    }

    companion object {
        private const val TAG = "GeofenceManager"
        private const val MIN_RADIUS_METERS = 50f
        private const val MAX_RADIUS_METERS = 500f
    }
}