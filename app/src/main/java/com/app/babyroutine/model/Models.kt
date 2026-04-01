package com.app.babyroutine.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Frequency {
    DAILY,
    SOME_DAYS,
    ONCE;

    fun toDisplayText(): String {
        return when (this) {
            DAILY -> "Tous les jours"
            SOME_DAYS -> "Certains jours"
            ONCE -> "Une seule fois"
        }
    }
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH;

    fun toDisplayText(): String {
        return when (this) {
            LOW -> "Faible"
            MEDIUM -> "Moyenne"
            HIGH -> "Élevée"
        }
    }
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
    val priority: Priority = Priority.MEDIUM
)

data class HomeCategory(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color
)