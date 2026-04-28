package com.app.babyroutine.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Routine
import java.util.Calendar

object RoutineScheduler {

    const val EXTRA_ROUTINE_ID = "routine_id"
    const val EXTRA_ROUTINE_TITLE = "routine_title"
    const val EXTRA_ROUTINE_MESSAGE = "routine_message"

    fun scheduleRoutineNotification(
        context: Context,
        routine: Routine
    ) {
        if (!routine.notificationsEnabled) return

        val parts = routine.time.split(":")
        if (parts.size != 2) return

        val hour = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        if (hour !in 0..23 || minute !in 0..59) return

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, RoutineAlarmReceiver::class.java).apply {
            putExtra(EXTRA_ROUTINE_ID, routine.id)
            putExtra(EXTRA_ROUTINE_TITLE, routine.title)
            putExtra(
                EXTRA_ROUTINE_MESSAGE,
                routine.description.ifBlank {
                    "Il est temps d’effectuer votre routine."
                }
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            routine.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        when (routine.frequency) {
            Frequency.DAILY -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }

            Frequency.SOME_DAYS,
            Frequency.ONCE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }
        }
    }

    fun cancelRoutineNotification(
        context: Context,
        routine: Routine
    ) {
        val intent = Intent(context, RoutineAlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            routine.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}