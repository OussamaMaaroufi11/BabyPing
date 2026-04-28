package com.app.babyroutine.data

import androidx.room.Entity

@Entity(
    tableName = "routine_daily_state",
    primaryKeys = ["routineId", "dateKey"]
)
data class RoutineDailyState(
    val routineId: String,
    val dateKey: String,
    val routineTitle: String,
    val wasCompleted: Boolean = false,
    val wasIgnored: Boolean = false
)