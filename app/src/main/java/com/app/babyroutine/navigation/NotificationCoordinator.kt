package com.app.babyroutine.notifications

import android.content.Context
import com.app.babyroutine.location.GeofenceManager
import com.app.babyroutine.model.Routine

class NotificationCoordinator(
    private val context: Context,
    private val geofenceManager: GeofenceManager
) {

    fun syncRoutine(routine: Routine) {
        syncTimeNotification(routine)
        syncLocationTrigger(routine)
    }

    fun syncAll(routines: List<Routine>) {
        routines.forEach { routine ->
            syncRoutine(routine)
        }
    }

    fun syncTimeNotification(routine: Routine) {
        if (routine.notificationsEnabled) {
            RoutineScheduler.scheduleRoutineNotification(context, routine)
        } else {
            RoutineScheduler.cancelRoutineNotification(context, routine)
        }
    }

    fun syncLocationTrigger(routine: Routine) {
        if (routine.notificationsEnabled && routine.hasLocationTrigger) {
            geofenceManager.addGeofenceForRoutine(routine)
        } else {
            geofenceManager.removeGeofenceForRoutine(routine.id)
        }
    }

    fun onRoutineCreated(routine: Routine) {
        syncRoutine(routine)
    }

    fun onRoutineUpdated(oldRoutine: Routine, newRoutine: Routine) {
        RoutineScheduler.cancelRoutineNotification(context, oldRoutine)
        geofenceManager.removeGeofenceForRoutine(oldRoutine.id)
        syncRoutine(newRoutine)
    }

    fun onRoutineDeleted(routine: Routine) {
        RoutineScheduler.cancelRoutineNotification(context, routine)
        geofenceManager.removeGeofenceForRoutine(routine.id)
    }

    fun onNotificationsToggled(updatedRoutine: Routine) {
        syncRoutine(updatedRoutine)
    }
}