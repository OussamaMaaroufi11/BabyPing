package com.app.babyroutine.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.babyroutine.data.AppDatabase
import com.app.babyroutine.data.RoutineRepository
import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = RoutineRepository(database.routineDao())

    val allRoutines: Flow<List<Routine>> = repository.getAllRoutines()

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
}