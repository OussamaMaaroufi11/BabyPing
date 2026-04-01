package com.app.babyroutine.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.babyroutine.data.AppDatabase
import com.app.babyroutine.data.initialRoutines
import com.app.babyroutine.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.Companion.getDatabase(application)
    private val routineDao = database.routineDao()

    val allRoutines: Flow<List<Routine>> = routineDao.getAllRoutines()

    init {
        seedInitialDataIfNeeded()
    }

    private fun seedInitialDataIfNeeded() {
        viewModelScope.launch {
            val count = routineDao.getRoutineCount()
            if (count == 0) {
                routineDao.insertAll(initialRoutines)
            }
        }
    }

    fun deleteRoutineById(routineId: String) {
        viewModelScope.launch {
            routineDao.deleteRoutineById(routineId)
        }
    }

    fun upsertRoutine(routine: Routine) {
        viewModelScope.launch {
            routineDao.upsertRoutine(routine)
        }
    }
}