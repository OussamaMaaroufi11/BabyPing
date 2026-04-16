package com.app.babyroutine.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.babyroutine.notifications.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) return

        val transitionType = geofencingEvent.geofenceTransition
        if (transitionType != Geofence.GEOFENCE_TRANSITION_ENTER) return

        val routineTitle = intent.getStringExtra(EXTRA_ROUTINE_TITLE)
            ?: "Routine BabyPing"

        NotificationHelper.showRoutineNotification(
            context = context,
            title = "Routine déclenchée",
            message = "Vous êtes arrivé dans la zone de : $routineTitle"
        )
    }

    companion object {
        const val EXTRA_ROUTINE_TITLE = "extra_routine_title"
    }
}