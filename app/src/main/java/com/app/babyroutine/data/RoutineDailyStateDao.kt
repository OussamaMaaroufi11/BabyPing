package com.app.babyroutine.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDailyStateDao {

    @Query("SELECT * FROM routine_daily_state")
    fun getAllDailyStates(): Flow<List<RoutineDailyState>>

    @Query("SELECT * FROM routine_daily_state WHERE routineId = :routineId AND dateKey = :dateKey LIMIT 1")
    suspend fun getStateForRoutineAndDate(
        routineId: String,
        dateKey: String
    ): RoutineDailyState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyState(state: RoutineDailyState)

    @Query("DELETE FROM routine_daily_state WHERE routineId = :routineId AND dateKey = :dateKey")
    suspend fun deleteStateForRoutineAndDate(
        routineId: String,
        dateKey: String
    )
}