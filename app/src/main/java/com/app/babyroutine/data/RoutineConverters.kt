package com.app.babyroutine.data

import androidx.room.TypeConverter
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Priority

class RoutineConverters {

    @TypeConverter
    fun fromFrequency(value: Frequency): String = value.name

    @TypeConverter
    fun toFrequency(value: String): Frequency = Frequency.valueOf(value)

    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}