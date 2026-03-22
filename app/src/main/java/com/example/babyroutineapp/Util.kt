package com.example.babyroutineapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stroller
import androidx.compose.ui.graphics.Color

// -------------------- CATÉGORIES --------------------

val homeCategories = listOf(
    HomeCategory(
        title = "Quotidiens",
        icon = Icons.Filled.CalendarToday,
        bgColor = Color(0xFFBDE3FF) // bleu doux
    ),
    HomeCategory(
        title = "Programmes",
        icon = Icons.Filled.DateRange,
        bgColor = Color(0xFFFFE7A8) // jaune pastel
    ),
    HomeCategory(
        title = "Santé",
        icon = Icons.Filled.MonitorHeart,
        bgColor = Color(0xFFFFC9C9) // rouge doux
    ),
    HomeCategory(
        title = "Activités",
        icon = Icons.Filled.Stroller,
        bgColor = Color(0xFFCFF5D6) // vert pastel
    ),
    HomeCategory(
        title = "Courses",
        icon = Icons.Filled.ShoppingCart,
        bgColor = Color(0xFFE9C8FF) // violet doux
    )
)

// -------------------- ROUTINES INITIALES --------------------

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