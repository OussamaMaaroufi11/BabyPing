package com.example.babyroutineapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stroller
import androidx.compose.ui.graphics.Color

// Catégories existantes
val homeCategories = listOf(
    HomeCategory("Quotidiens", Icons.Filled.CalendarToday, Color(0xFFDDEEFF)),
    HomeCategory("Programmes", Icons.Filled.DateRange, Color(0xFFFFF0C9)),
    HomeCategory("Santé", Icons.Filled.MonitorHeart, Color(0xFFFFE3E3)),
    HomeCategory("Activités", Icons.Filled.Stroller, Color(0xFFE8F7EA)),
    HomeCategory("Courses", Icons.Filled.ShoppingCart, Color(0xFFE89FFF))
)

// Routines existantes
val initialRoutines = listOf(
    Routine(
        id = "r1",
        title = "Changer la couche",
        time = "07:00",
        category = "Quotidiens",
        frequency = Frequency.DAILY,
        priority = Priority.HIGH
    ),
    Routine(
        id = "r2",
        title = "Petit déjeuner",
        time = "08:00",
        category = "Quotidiens",
        frequency = Frequency.DAILY,
        priority = Priority.MEDIUM
    ),
    Routine(
        id = "r3",
        title = "Sieste",
        time = "14:00",
        category = "Programmes",
        frequency = Frequency.SOME_DAYS,
        priority = Priority.MEDIUM
    ),
    Routine(
        id = "r4",
        title = "Jeu",
        time = "16:00",
        category = "Activités",
        frequency = Frequency.DAILY,
        priority = Priority.LOW
    )
)