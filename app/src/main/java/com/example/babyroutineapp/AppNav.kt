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

// -------------------- ROUTES --------------------

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object AddRoutine : Screen("addRoutine/{category}") {
        fun route(category: String) = "addRoutine/$category"
    }

    object Suivi : Screen("suivi")

    object CategoryList : Screen("category/{category}") {
        fun route(category: String) = "category/$category"
    }
}

// -------------------- HELPERS --------------------

private fun todayKey(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date())

private fun frequencyToText(f: Frequency): String = when (f) {
    Frequency.DAILY -> "Tous les jours"
    Frequency.SOME_DAYS -> "Certains jours"
    Frequency.ONCE -> "Une seule fois"
}

// -------------------- APP ROOT --------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    // Routines existantes viennent du fichier Util.kt
    val routines = remember { mutableStateListOf<Routine>().apply { addAll(initialRoutines) } }

    // doneByDate["2026-02-09"] = liste d’IDs terminés ce jour-là
    val doneByDate = remember { mutableStateMapOf<String, SnapshotStateList<String>>() }

    val navController = rememberNavController()

    // state du jour
    val dateKey = todayKey()
    val doneTodayIds = doneByDate.getOrPut(dateKey) { mutableStateListOf() }

    // calcul automatique (02/04 + %)
    val totalToday = routines.size
    val doneToday = doneTodayIds.size.coerceAtMost(totalToday)
    val progress = if (totalToday == 0) 0f else doneToday.toFloat() / totalToday.toFloat()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // ---------------- HOME ----------------
        composable(Screen.Home.route) {
            BabyPingHomeScreen(
                routines = routines,
                selectedTab = HomeTab.Home,
                onTabSelected = { tab ->
                    if (tab == HomeTab.Suivi) navController.navigate(Screen.Suivi.route)
                },
                onNewReminderClick = {
                    navController.navigate(Screen.AddRoutine.route("Quotidiens"))
                },
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryList.route(category))
                }
            )
        }

        // ---------------- CATEGORY LIST ----------------
        composable(
            route = Screen.CategoryList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Quotidiens"

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
                frequencyTextProvider = { routine -> frequencyToText(routine.frequency) }
            )
        }

        // ---------------- SUIVI ----------------
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

        // ---------------- ADD ROUTINE ----------------
        composable(
            route = Screen.AddRoutine.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Quotidiens"

            AddRoutineScreen(
                category = category,
                onSave = { newRoutine ->
                    routines.add(newRoutine)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}