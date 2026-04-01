package com.app.babyroutine.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stroller
import androidx.compose.ui.graphics.Color
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.HomeCategory
import com.app.babyroutine.model.Priority
import com.app.babyroutine.model.Routine

val homeCategories = listOf(
    HomeCategory(
        title = "Quotidiens",
        icon = Icons.Filled.CalendarToday,
        bgColor = Color(0xFFBDE3FF)
    ),
    HomeCategory(
        title = "Programmes",
        icon = Icons.Filled.DateRange,
        bgColor = Color(0xFFFFE7A8)
    ),
    HomeCategory(
        title = "Santé",
        icon = Icons.Filled.MonitorHeart,
        bgColor = Color(0xFFFFC9C9)
    ),
    HomeCategory(
        title = "Activités",
        icon = Icons.Filled.Stroller,
        bgColor = Color(0xFFCFF5D6)
    ),
    HomeCategory(
        title = "Courses",
        icon = Icons.Filled.ShoppingCart,
        bgColor = Color(0xFFE9C8FF)
    )
)

val initialRoutines = listOf(
    Routine(
        id = "r1",
        title = "Changer la couche",
        description = "Préparer une couche propre et des lingettes.",
        time = "07:00",
        category = "Quotidiens",
        frequency = Frequency.DAILY,
        priority = Priority.HIGH
    ),
    Routine(
        id = "r2",
        title = "Petit déjeuner",
        description = "Donner le repas du matin au bébé.",
        time = "08:00",
        category = "Quotidiens",
        frequency = Frequency.DAILY,
        priority = Priority.MEDIUM
    ),
    Routine(
        id = "r3",
        title = "Sieste",
        description = "Prévoir une période calme pour la sieste.",
        time = "14:00",
        category = "Programmes",
        frequency = Frequency.SOME_DAYS,
        priority = Priority.MEDIUM
    ),
    Routine(
        id = "r4",
        title = "Jeu",
        description = "Temps de jeu et de stimulation.",
        time = "16:00",
        category = "Activités",
        frequency = Frequency.DAILY,
        priority = Priority.LOW
    )
)