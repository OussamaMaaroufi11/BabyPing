package com.app.babyroutine.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routines ORDER BY time ASC")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routines WHERE id = :routineId LIMIT 1")
    suspend fun getRoutineById(routineId: String): Routine?

    @Query("SELECT * FROM routines WHERE category = :category ORDER BY time ASC")
    fun getRoutinesByCategory(category: String): Flow<List<Routine>>

    @Query("SELECT COUNT(*) FROM routines")
    suspend fun getRoutineCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routines: List<Routine>)

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutineById(routineId: String)

    @Query("DELETE FROM routines")
    suspend fun deleteAllRoutines()
}