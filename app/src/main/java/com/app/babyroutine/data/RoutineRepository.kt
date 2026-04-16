package com.app.babyroutine.data

import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow

class RoutineRepository(
    private val routineDao: RoutineDao
) {

    fun getAllRoutines(): Flow<List<Routine>> =
        routineDao.getAllRoutines()

    fun getRoutinesByCategory(category: String): Flow<List<Routine>> =
        routineDao.getRoutinesByCategory(category)

    suspend fun getRoutineById(routineId: String): Routine? =
        routineDao.getRoutineById(routineId)

    suspend fun upsertRoutine(routine: Routine) {
        routineDao.upsertRoutine(routine)
    }

    suspend fun insertAll(routines: List<Routine>) {
        routineDao.insertAll(routines)
    }

    suspend fun deleteRoutineById(routineId: String) {
        routineDao.deleteRoutineById(routineId)
    }

    suspend fun deleteAllRoutines() {
        routineDao.deleteAllRoutines()
    }

    suspend fun getRoutineCount(): Int =
        routineDao.getRoutineCount()

    suspend fun seedDemoDataIfEmpty() {
        if (routineDao.getRoutineCount() == 0) {
            routineDao.insertAll(demoRoutines)
        }
    }
}