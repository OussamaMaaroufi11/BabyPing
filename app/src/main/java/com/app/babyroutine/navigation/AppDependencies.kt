package com.app.babyroutine.navigation

import android.content.Context
import com.app.babyroutine.location.GeofenceManager
import com.app.babyroutine.notifications.NotificationCoordinator

data class AppDependencies(
    val geofenceManager: GeofenceManager,
    val notificationCoordinator: NotificationCoordinator
)

fun createAppDependencies(context: Context): AppDependencies {
    val appContext = context.applicationContext

    val geofenceManager = GeofenceManager(appContext)
    val notificationCoordinator = NotificationCoordinator(
        context = appContext,
        geofenceManager = geofenceManager
    )

    return AppDependencies(
        geofenceManager = geofenceManager,
        notificationCoordinator = notificationCoordinator
    )
}