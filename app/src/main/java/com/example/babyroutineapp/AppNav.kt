package com.example.babyroutineapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object AddRoutine : Screen("addRoutine/{category}") {
        fun route(category: String) = "addRoutine/$category"
    }

    object EditRoutine : Screen("editRoutine/{id}") {
        fun route(id: String) = "editRoutine/$id"
    }

    object Suivi : Screen("suivi")

    object CategoryList : Screen("category/{category}") {
        fun route(category: String) = "category/$category"
    }
}

private fun todayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date())

private fun frequencyToText(f: Frequency): String = when (f) {
    Frequency.DAILY -> "Tous les jours"
    Frequency.SOME_DAYS -> "Certains jours"
    Frequency.ONCE -> "Une seule fois"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    val routines = remember { mutableStateListOf<Routine>().apply { addAll(initialRoutines) } }

    val doneByDate = remember { mutableStateMapOf<String, SnapshotStateList<String>>() }
    val navController = rememberNavController()

    val dateKey = todayKey()
    val doneTodayIds = doneByDate.getOrPut(dateKey) { mutableStateListOf() }

    val totalToday = routines.size
    val doneToday = doneTodayIds.size.coerceAtMost(totalToday)
    val progress = if (totalToday == 0) 0f else doneToday.toFloat() / totalToday.toFloat()

    fun upsertRoutine(r: Routine) {
        val idx = routines.indexOfFirst { it.id == r.id }
        if (idx == -1) routines.add(r) else routines[idx] = r
    }

    fun deleteRoutine(id: String) {
        routines.removeAll { it.id == id }
        doneByDate.values.forEach { list -> list.remove(id) }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            BabyPingHomeScreen(
                routines = routines,
                selectedTab = HomeTab.Home,
                onTabSelected = { tab ->
                    if (tab == HomeTab.Suivi) navController.navigate(Screen.Suivi.route)
                },
                onNewReminderClick = { category ->
                    navController.navigate(Screen.AddRoutine.route(category))
                },
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryList.route(category))
                }
            )
        }

        composable(
            route = Screen.CategoryList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { entry ->
            val category = entry.arguments?.getString("category") ?: "Quotidiens"

            CategoryListScreen(
                categoryTitle = category,
                routines = routines.filter { it.category == category },
                doneIdsToday = doneTodayIds,
                onToggleDone = { routineId ->
                    if (doneTodayIds.contains(routineId)) doneTodayIds.remove(routineId)
                    else doneTodayIds.add(routineId)
                },
                onBack = { navController.popBackStack() },
                onQuit = { navController.popBackStack(Screen.Home.route, inclusive = false) },
                onAdd = { navController.navigate(Screen.AddRoutine.route(category)) },
                onEdit = { id -> navController.navigate(Screen.EditRoutine.route(id)) },
                onDelete = { id -> deleteRoutine(id) },
                frequencyTextProvider = { routine -> frequencyToText(routine.frequency) }
            )
        }

        composable(Screen.Suivi.route) {
            SuiviScreen(
                onBack = { navController.popBackStack() },
                selectedTab = HomeTab.Suivi,
                onTabSelected = { tab ->
                    if (tab == HomeTab.Home) {
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }
                },
                total = totalToday,
                done = doneToday,
                progress = progress
            )
        }

        composable(
            route = Screen.AddRoutine.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { entry ->
            val category = entry.arguments?.getString("category") ?: "Quotidiens"

            AddRoutineScreen(
                category = category,
                initialRoutine = null, // mode ADD
                onSave = { newRoutine ->
                    upsertRoutine(newRoutine)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditRoutine.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: ""
            val routine = routines.firstOrNull { it.id == id }

            if (routine == null) {
                navController.popBackStack()
            } else {
                AddRoutineScreen(
                    category = routine.category,
                    initialRoutine = routine, // mode EDIT
                    onSave = { editedRoutine ->
                        upsertRoutine(editedRoutine)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}