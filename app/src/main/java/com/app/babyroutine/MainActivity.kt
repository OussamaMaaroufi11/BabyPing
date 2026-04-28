package com.app.babyroutine

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.app.babyroutine.navigation.AppRoot
import com.app.babyroutine.notifications.NotificationHelper
import com.app.babyroutine.ui.theme.BabyRoutineAppTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private var openRoutineIdState by mutableStateOf<String?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        openRoutineIdState = intent?.getStringExtra(NotificationHelper.EXTRA_OPEN_ROUTINE_ID)

        NotificationHelper.createNotificationChannel(this)

        requestNotificationPermissionIfNeeded()
        requestExactAlarmPermissionIfNeeded()

        setContent {
            var isDarkMode by androidx.compose.runtime.remember {
                mutableStateOf(false)
            }

            BabyRoutineAppTheme(
                darkTheme = isDarkMode
            ) {
                AppRoot(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { enabled ->
                        isDarkMode = enabled
                    },
                    openRoutineId = openRoutineIdState,
                    onRoutineIntentConsumed = {
                        openRoutineIdState = null
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        openRoutineIdState = intent.getStringExtra(NotificationHelper.EXTRA_OPEN_ROUTINE_ID)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (alarmManager.canScheduleExactAlarms()) return

        runCatching {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }
}