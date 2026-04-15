package com.app.babyroutine.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.app.babyroutine.R
import kotlin.math.abs

object NotificationHelper {

    const val CHANNEL_ID = "babyping_routine_channel"
    private const val CHANNEL_NAME = "Rappels BabyPing"
    private const val CHANNEL_DESCRIPTION = "Notifications pour les routines BabyPing"

    fun createNotificationChannel(
        context: Context,
        soundEnabled: Boolean = true,
        vibrationEnabled: Boolean = true
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existingChannel = manager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel != null) {
                manager.deleteNotificationChannel(CHANNEL_ID)
            }

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(vibrationEnabled)

                if (vibrationEnabled) {
                    vibrationPattern = longArrayOf(0, 300, 200, 300)
                }

                if (soundEnabled) {
                    val soundUri: Uri =
                        android.provider.Settings.System.DEFAULT_NOTIFICATION_URI

                    val audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()

                    setSound(soundUri, audioAttributes)
                } else {
                    setSound(null, null)
                }
            }

            manager.createNotificationChannel(channel)
        }
    }

    fun showRoutineNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = abs(System.currentTimeMillis().toInt()),
        vibrationEnabled: Boolean = true
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.babyping)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (vibrationEnabled) {
            builder.setVibrate(longArrayOf(0, 300, 200, 300))
        } else {
            builder.setVibrate(longArrayOf(0L))
        }

        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}