package com.example.babyroutineapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Frequency { DAILY, SOME_DAYS, ONCE }
enum class Priority { LOW, MEDIUM, HIGH }
enum class HomeTab { Home, Suivi }

data class Routine(
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