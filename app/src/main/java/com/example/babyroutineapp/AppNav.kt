package com.example.babyroutineapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -------------------- NAVIGATION --------------------

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object AddRoutine : Screen("addRoutine/{category}") {
        fun route(category: String) = "addRoutine/$category"
    }

    object EditRoutine : Screen("editRoutine/{id}") {
        fun route(id: String) = "editRoutine/$id"
    }

    object Suivi : Screen("suivi")
    object Settings : Screen("settings")
    object Profile : Screen("profile")

    object CategoryList : Screen("category/{category}") {
        fun route(category: String) = "category/$category"
    }
}

// -------------------- UTILS --------------------

private fun todayKey(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date())
}

private fun frequencyToText(frequency: Frequency): String {
    return when (frequency) {
        Frequency.DAILY -> "Tous les jours"
        Frequency.SOME_DAYS -> "Certains jours"
        Frequency.ONCE -> "Une seule fois"
    }
}

// -------------------- ROOT --------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    // -------- SETTINGS --------
    val notificationsEnabled = remember { mutableStateOf(true) }

    // -------- PROFILE --------
    val profileName = remember { mutableStateOf("Oussama Maaroufi") }
    val profileEmail = remember { mutableStateOf("oussama@email.com") }
    val profilePhone = remember { mutableStateOf("+1 000 000 0000") }

    // -------- ROUTINES --------
    val routines = remember {
        mutableStateListOf<Routine>().apply {
            addAll(initialRoutines)
        }
    }

    // -------- DONE STATE --------
    val doneByDate = remember {
        mutableStateMapOf<String, SnapshotStateList<String>>()
    }

    val currentDateKey = todayKey()
    val doneTodayIds = doneByDate.getOrPut(currentDateKey) {
        mutableStateListOf()
    }

    val totalToday = routines.size
    val doneToday = doneTodayIds.size.coerceAtMost(totalToday)
    val progress =
        if (totalToday == 0) 0f else doneToday.toFloat() / totalToday.toFloat()

    // -------------------- ACTIONS --------------------

    fun upsertRoutine(routine: Routine) {
        val index = routines.indexOfFirst { it.id == routine.id }
        if (index == -1) {
            routines.add(routine)
        } else {
            routines[index] = routine
        }
    }

    fun deleteRoutine(routineId: String) {
        routines.removeAll { it.id == routineId }
        doneByDate.values.forEach { ids ->
            ids.remove(routineId)
        }
    }

    // -------------------- NAV HOST --------------------

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // -------- HOME --------
        composable(Screen.Home.route) {
            BabyPingHomeScreen(
                routines = routines,
                selectedTab = HomeTab.Home,

                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.Home -> Unit
                        HomeTab.Suivi -> navController.navigate(Screen.Suivi.route)
                    }
                },

                onNewReminderClick = { category ->
                    navController.navigate(Screen.AddRoutine.route(category))
                },

                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryList.route(category))
                },

                onRoutineClick = { routine ->
                    navController.navigate(Screen.EditRoutine.route(routine.id))
                },

                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // -------- CATEGORY LIST --------
        composable(
            route = Screen.CategoryList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->

            val category =
                backStackEntry.arguments?.getString("category") ?: "Quotidiens"

            CategoryListScreen(
                categoryTitle = category,
                routines = routines.filter { it.category == category },
                doneIdsToday = doneTodayIds,

                onToggleDone = { routineId ->
                    if (doneTodayIds.contains(routineId)) {
                        doneTodayIds.remove(routineId)
                    } else {
                        doneTodayIds.add(routineId)
                    }
                },

                onBack = { navController.popBackStack() },

                onQuit = {
                    navController.popBackStack(
                        Screen.Home.route,
                        inclusive = false
                    )
                },

                onAdd = {
                    navController.navigate(Screen.AddRoutine.route(category))
                },

                onEdit = { routineId ->
                    navController.navigate(Screen.EditRoutine.route(routineId))
                },

                onDelete = { routineId ->
                    deleteRoutine(routineId)
                },

                frequencyTextProvider = { routine ->
                    frequencyToText(routine.frequency)
                }
            )
        }

        // -------- SUIVI --------
        composable(Screen.Suivi.route) {
            SuiviScreen(
                onBack = { navController.popBackStack() },
                selectedTab = HomeTab.Suivi,

                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.Home -> navController.popBackStack(
                            Screen.Home.route,
                            inclusive = false
                        )

                        HomeTab.Suivi -> Unit
                    }
                },

                total = totalToday,
                done = doneToday,
                progress = progress
            )
        }

        // -------- SETTINGS --------
        composable(Screen.Settings.route) {
            SettingsScreen(
                isDarkMode = isDarkMode,
                notificationsEnabled = notificationsEnabled.value,
                profileName = profileName.value,

                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },

                onDarkModeChange = { enabled ->
                    onDarkModeChange(enabled)
                },

                onNotificationsChange = { enabled ->
                    notificationsEnabled.value = enabled
                },

                onBack = { navController.popBackStack() }
            )
        }

        // -------- PROFILE --------
        composable(Screen.Profile.route) {
            ProfileScreen(
                initialName = profileName.value,
                initialEmail = profileEmail.value,
                initialPhone = profilePhone.value,

                onSave = { newName, newEmail, newPhone ->
                    profileName.value = newName
                    profileEmail.value = newEmail
                    profilePhone.value = newPhone
                    navController.popBackStack()
                },

                onBack = { navController.popBackStack() }
            )
        }

        // -------- ADD ROUTINE --------
        composable(
            route = Screen.AddRoutine.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->

            val category =
                backStackEntry.arguments?.getString("category") ?: "Quotidiens"

            AddRoutineScreen(
                category = category,
                initialRoutine = null,

                onSave = { newRoutine ->
                    upsertRoutine(newRoutine)
                    navController.popBackStack()
                },

                onBack = { navController.popBackStack() }
            )
        }

        // -------- EDIT ROUTINE --------
        composable(
            route = Screen.EditRoutine.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->

            val routineId =
                backStackEntry.arguments?.getString("id") ?: ""

            val routineToEdit =
                routines.firstOrNull { it.id == routineId }

            if (routineToEdit == null) {
                navController.popBackStack()
            } else {
                AddRoutineScreen(
                    category = routineToEdit.category,
                    initialRoutine = routineToEdit,

                    onSave = { updatedRoutine ->
                        upsertRoutine(updatedRoutine)
                        navController.popBackStack()
                    },

                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}