package com.app.babyroutine.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.babyroutine.data.AppDatabase
import com.app.babyroutine.data.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        val routineId = intent.getStringExtra(NotificationHelper.EXTRA_ROUTINE_ID)
        if (routineId == null) {
            pendingResult.finish()
            return
        }

        val routineTitle = intent.getStringExtra(NotificationHelper.EXTRA_ROUTINE_TITLE)
            ?: "Routine BabyPing"

        val dateKey = todayKey()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(context.applicationContext)
                val repository = RoutineRepository(
                    routineDao = db.routineDao(),
                    routineDailyStateDao = db.routineDailyStateDao()
                )

                repository.markRoutineIgnoredByInfo(
                    routineId = routineId,
                    routineTitle = routineTitle,
                    dateKey = dateKey
                )
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun todayKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date())
    }
}