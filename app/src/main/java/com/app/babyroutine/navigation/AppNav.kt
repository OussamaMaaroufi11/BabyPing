package com.app.babyroutine.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.babyroutine.ui.viewmodel.RoutineViewModel
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.HomeTab
import com.app.babyroutine.ui.screens.AddRoutineScreen
import com.app.babyroutine.ui.screens.BabyPingHomeScreen
import com.app.babyroutine.ui.screens.CategoryListScreen
import com.app.babyroutine.ui.screens.ProfileScreen
import com.app.babyroutine.ui.screens.SettingsScreen
import com.app.babyroutine.ui.screens.SuiviScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object AddRoutine : Screen("addRoutine/{category}") {
        fun route(category: String): String = "addRoutine/$category"
    }

    data object EditRoutine : Screen("editRoutine/{id}") {
        fun route(id: String): String = "editRoutine/$id"
    }

    data object Suivi : Screen("suivi")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")

    data object CategoryList : Screen("category/{category}") {
        fun route(category: String): String = "category/$category"
    }
}

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    routineViewModel: RoutineViewModel = viewModel()
) {
    val navController = rememberNavController()

    val notificationsEnabled = remember { mutableStateOf(true) }

    val profileName = remember { mutableStateOf("Oussama Maaroufi") }
    val profileEmail = remember { mutableStateOf("oussama@email.com") }
    val profilePhone = remember { mutableStateOf("+1 000 000 0000") }

    val routines by routineViewModel.allRoutines.collectAsState(initial = emptyList())

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

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

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
                    routineViewModel.deleteRoutineById(routineId)
                },
                frequencyTextProvider = { routine ->
                    frequencyToText(routine.frequency)
                }
            )
        }

        composable(Screen.Suivi.route) {
            SuiviScreen(
                onBack = { navController.popBackStack() },
                selectedTab = HomeTab.Suivi,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.Home -> {
                            navController.popBackStack(
                                Screen.Home.route,
                                inclusive = false
                            )
                        }
                        HomeTab.Suivi -> Unit
                    }
                },
                total = totalToday,
                done = doneToday,
                progress = progress
            )
        }

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

        composable(
            route = Screen.AddRoutine.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->

            val category = backStackEntry.arguments?.getString("category") ?: "Quotidiens"

            AddRoutineScreen(
                category = category,
                initialRoutine = null,
                onSave = { newRoutine ->
                    routineViewModel.upsertRoutine(newRoutine)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditRoutine.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->

            val routineId = backStackEntry.arguments?.getString("id") ?: ""
            val routineToEdit = routines.firstOrNull { it.id == routineId }

            if (routineToEdit != null) {
                AddRoutineScreen(
                    category = routineToEdit.category,
                    initialRoutine = routineToEdit,
                    onSave = { updatedRoutine ->
                        routineViewModel.upsertRoutine(updatedRoutine)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}