package com.app.babyroutine

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.app.babyroutine.navigation.AppRoot
import com.app.babyroutine.notifications.NotificationHelper
import com.app.babyroutine.ui.theme.BabyRoutineAppTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Rien ici pour l’instant.
        // La notification sera testée manuellement depuis l’écran Notifications.
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            BabyRoutineAppTheme(
                darkTheme = isDarkMode
            ) {
                AppRoot(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { enabled ->
                        isDarkMode = enabled
                    }
                )
            }
        }
    }
}