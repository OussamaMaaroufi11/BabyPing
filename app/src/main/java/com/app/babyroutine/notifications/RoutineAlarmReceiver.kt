package com.app.babyroutine.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RoutineAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val routineId = intent.getStringExtra(RoutineScheduler.EXTRA_ROUTINE_ID)
        val routineTitle = intent.getStringExtra(RoutineScheduler.EXTRA_ROUTINE_TITLE)
            ?: "Routine BabyPing"
        val routineMessage = intent.getStringExtra(RoutineScheduler.EXTRA_ROUTINE_MESSAGE)
            ?: "Il est temps d’effectuer votre routine."

        NotificationHelper.showRoutineNotification(
            context = context,
            title = routineTitle,
            message = routineMessage,
            routineId = routineId
        )
    }
}