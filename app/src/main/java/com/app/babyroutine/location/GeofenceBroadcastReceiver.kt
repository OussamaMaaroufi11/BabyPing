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

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val routineTitle = intent.getStringExtra("routine_title") ?: "Routine"

            NotificationHelper.showRoutineNotification(
                context = context,
                title = "Routine déclenchée",
                message = "Vous êtes entré dans la zone de : $routineTitle"
            )
        }
    }
}