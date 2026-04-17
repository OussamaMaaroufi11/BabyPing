package com.app.babyroutine.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RoutineAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val routineId = intent.getStringExtra("routine_id")
        val title = intent.getStringExtra("routine_title") ?: "Routine BabyPing"
        val message = intent.getStringExtra("routine_message")
            ?: "Il est temps d’effectuer votre routine."

        NotificationHelper.showRoutineNotification(
            context = context,
            title = title,
            message = message,
            routineId = routineId
        )
    }
}