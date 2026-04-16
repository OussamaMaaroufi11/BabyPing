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
import java.util.UUID

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

val demoRoutines = listOf(
    Routine(
        id = UUID.randomUUID().toString(),
        title = "Donner le biberon",
        description = "Routine du matin pour bébé",
        time = "08:00",
        category = "Quotidiens",
        frequency = Frequency.DAILY,
        priority = Priority.HIGH
    ),
    Routine(
        id = UUID.randomUUID().toString(),
        title = "Petite promenade",
        description = "Sortie légère au parc",
        time = "10:00",
        category = "Activités",
        frequency = Frequency.SOME_DAYS,
        priority = Priority.MEDIUM
    ),
    Routine(
        id = UUID.randomUUID().toString(),
        title = "Rendez-vous pédiatre",
        description = "Contrôle de santé",
        time = "17:45",
        category = "Santé",
        frequency = Frequency.ONCE,
        priority = Priority.HIGH
    )
)