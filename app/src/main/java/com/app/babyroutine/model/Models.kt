package com.app.babyroutine.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Frequency {
    DAILY,
    SOME_DAYS,
    ONCE
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

enum class HomeTab {
    Home,
    Suivi
}

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String = "",
    val time: String,
    val category: String,
    val frequency: Frequency = Frequency.DAILY,
    val priority: Priority = Priority.MEDIUM,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radius: Float = 100f,
    val locationName: String? = null,

    val notificationsEnabled: Boolean = true
)

data class HomeCategory(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color
)

data class RoutineLocation(
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val locationName: String? = null
)