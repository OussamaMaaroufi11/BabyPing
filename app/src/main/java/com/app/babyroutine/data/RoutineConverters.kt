package com.app.babyroutine.data

import androidx.room.TypeConverter
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Priority

class RoutineConverters {

    @TypeConverter
    fun fromFrequency(value: Frequency?): String {
        return value?.name ?: Frequency.DAILY.name
    }

    @TypeConverter
    fun toFrequency(value: String?): Frequency {
        return try {
            Frequency.valueOf(value ?: Frequency.DAILY.name)
        } catch (_: IllegalArgumentException) {
            Frequency.DAILY
        }
    }

    @TypeConverter
    fun fromPriority(value: Priority?): String {
        return value?.name ?: Priority.MEDIUM.name
    }

    @TypeConverter
    fun toPriority(value: String?): Priority {
        return try {
            Priority.valueOf(value ?: Priority.MEDIUM.name)
        } catch (_: IllegalArgumentException) {
            Priority.MEDIUM
        }
    }
}