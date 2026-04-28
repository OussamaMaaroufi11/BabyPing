package com.app.babyroutine.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.babyroutine.data.AppDatabase
import com.app.babyroutine.data.RoutineDailyState
import com.app.babyroutine.data.RoutineRepository
import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application.applicationContext)

    private val repository = RoutineRepository(
        routineDao = database.routineDao(),
        routineDailyStateDao = database.routineDailyStateDao()
    )

    val allRoutines: Flow<List<Routine>> = repository.getAllRoutines()
    val allDailyStates: Flow<List<RoutineDailyState>> = repository.getAllDailyStates()

    init {
        seedInitialDataIfNeeded()
    }

    private fun seedInitialDataIfNeeded() {
        viewModelScope.launch {
            repository.seedDemoDataIfEmpty()
        }
    }

    fun getRoutinesByCategory(category: String): Flow<List<Routine>> {
        return repository.getRoutinesByCategory(category)
    }

    fun upsertRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.upsertRoutine(routine)
        }
    }

    fun deleteRoutineById(routineId: String) {
        viewModelScope.launch {
            repository.deleteRoutineById(routineId)
        }
    }

    fun deleteAllRoutines() {
        viewModelScope.launch {
            repository.deleteAllRoutines()
        }
    }

    fun setRoutineCompleted(
        routine: Routine,
        dateKey: String,
        completed: Boolean
    ) {
        viewModelScope.launch {
            repository.setRoutineCompleted(
                routine = routine,
                dateKey = dateKey,
                completed = completed
            )
        }
    }

    fun markRoutineIgnored(
        routine: Routine,
        dateKey: String
    ) {
        viewModelScope.launch {
            repository.markRoutineIgnored(
                routine = routine,
                dateKey = dateKey
            )
        }
    }
}