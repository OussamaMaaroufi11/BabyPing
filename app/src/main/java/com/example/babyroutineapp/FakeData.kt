package com.example.babyroutineapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stroller
import androidx.compose.ui.graphics.Color

val homeCategories = listOf(
    HomeCategory("Quotidiens", Icons.Filled.CalendarToday, Color(0xFFDDEEFF)),
    HomeCategory("Programmes", Icons.Filled.DateRange, Color(0xFFFFF0C9)),
    HomeCategory("Santé", Icons.Filled.MonitorHeart, Color(0xFFFFE3E3)),
    HomeCategory("Activités", Icons.Filled.Stroller, Color(0xFFE8F7EA)),
    HomeCategory("Courses", Icons.Filled.ShoppingCart, Color(0xFFE89FFF))
)
