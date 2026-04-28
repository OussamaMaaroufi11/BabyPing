package com.app.babyroutine.data

import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow

class RoutineRepository(
    private val routineDao: RoutineDao,
    private val routineDailyStateDao: RoutineDailyStateDao
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

    fun getAllDailyStates(): Flow<List<RoutineDailyState>> =
        routineDailyStateDao.getAllDailyStates()

    suspend fun setRoutineCompleted(
        routine: Routine,
        dateKey: String,
        completed: Boolean
    ) {
        val existing = routineDailyStateDao.getStateForRoutineAndDate(
            routineId = routine.id,
            dateKey = dateKey
        )

        val ignored = if (completed) {
            false
        } else {
            existing?.wasIgnored ?: false
        }

        if (!completed && !ignored) {
            routineDailyStateDao.deleteStateForRoutineAndDate(
                routineId = routine.id,
                dateKey = dateKey
            )
            return
        }

        routineDailyStateDao.upsertDailyState(
            RoutineDailyState(
                routineId = routine.id,
                dateKey = dateKey,
                routineTitle = routine.title,
                wasCompleted = completed,
                wasIgnored = ignored
            )
        )
    }

    suspend fun markRoutineIgnored(
        routine: Routine,
        dateKey: String
    ) {

        routineDailyStateDao.upsertDailyState(
            RoutineDailyState(
                routineId = routine.id,
                dateKey = dateKey,
                routineTitle = routine.title,
                wasCompleted = false,
                wasIgnored = true
            )
        )
    }

    suspend fun markRoutineIgnoredByInfo(
        routineId: String,
        routineTitle: String,
        dateKey: String
    ) {

        routineDailyStateDao.upsertDailyState(
            RoutineDailyState(
                routineId = routineId,
                dateKey = dateKey,
                routineTitle = routineTitle,
                wasCompleted = false,
                wasIgnored = true
            )
        )
    }
}