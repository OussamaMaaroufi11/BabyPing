package com.example.babyroutineapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// -------------------- ENUMS --------------------

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

// -------------------- DATA --------------------

data class Routine(
    val id: String,
    val title: String,
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