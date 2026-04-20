package com.app.babyroutine.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.babyroutine.domain.RoutineValidator
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.HomeTab
import com.app.babyroutine.model.Routine
import com.app.babyroutine.model.RoutineLocation
import com.app.babyroutine.notifications.NotificationHelper
import com.app.babyroutine.ui.screens.AddRoutineScreen
import com.app.babyroutine.ui.screens.BabyPingHomeScreen
import com.app.babyroutine.ui.screens.CategoryListScreen
import com.app.babyroutine.ui.screens.IgnoredRemindersScreen
import com.app.babyroutine.ui.screens.MapPickerScreen
import com.app.babyroutine.ui.screens.NotificationSettingsScreen
import com.app.babyroutine.ui.screens.ProfileScreen
import com.app.babyroutine.ui.screens.RoutineOptionsScreen
import com.app.babyroutine.ui.screens.SettingsScreen
import com.app.babyroutine.ui.screens.StatisticsScreen
import com.app.babyroutine.ui.screens.SuiviScreen
import com.app.babyroutine.ui.viewmodel.RoutineViewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
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

    data object RoutineOptions : Screen("routineOptions/{id}") {
        fun route(id: String): String = "routineOptions/$id"
    }

    data object MapPicker : Screen("mapPicker")
    data object Suivi : Screen("suivi")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
    data object Statistics : Screen("statistics")
    data object IgnoredReminders : Screen("ignoredReminders")
    data object NotificationSettings : Screen("notificationSettings")

    data object CategoryList : Screen("category/{category}") {
        fun route(category: String): String = "category/$category"
    }
}

private data class DemoConfig(
    val enabled: Boolean = true
)

private data class StatsUiState(
    val totalToday: Int,
    val doneToday: Int,
    val progress: Float,
    val completedPercent: Int,
    val totalRoutinesForStats: Int,
    val weekProgress: List<Int>,
    val ignoredRemindersToday: List<String>,
    val ignoredYesterdayCount: Int,
    val totalReceivedCount: Int,
    val ignoredPerDay: List<Int>
)

private object DemoStatsProvider {
    val ignoredReminders = listOf(
        "Donner le biberon au bébé",
        "Faire une petite balade",
        "Rendez-vous chez le pédiatre"
    )

    val weekProgress = listOf(100, 80, 80, 80, 80, 50, 70)
    val ignoredPerDay = listOf(1, 3, 0, 2, 1, 4, 2)

    fun build(totalToday: Int, doneToday: Int, progress: Float): StatsUiState {
        return StatsUiState(
            totalToday = totalToday,
            doneToday = doneToday,
            progress = progress,
            completedPercent = 80,
            totalRoutinesForStats = 7,
            weekProgress = weekProgress,
            ignoredRemindersToday = ignoredReminders,
            ignoredYesterdayCount = 3,
            totalReceivedCount = 7,
            ignoredPerDay = ignoredPerDay
        )
    }
}

private object StatsCalculator {

    fun calculateProgress(totalToday: Int, doneToday: Int): Float {
        return if (totalToday == 0) 0f else doneToday.toFloat() / totalToday.toFloat()
    }

    fun calculateCompletedPercent(totalToday: Int, doneToday: Int): Int {
        return if (totalToday == 0) 0 else (doneToday * 100) / totalToday
    }

    fun calculateWeekProgress(
        weekKeys: List<String>,
        doneByDate: Map<String, List<String>>,
        routinesCount: Int
    ): List<Int> {
        return weekKeys.map { key ->
            val doneCount = doneByDate[key]?.size ?: 0
            if (routinesCount == 0) 0
            else (doneCount.coerceAtMost(routinesCount) * 100) / routinesCount
        }
    }

    fun calculateIgnoredPerDay(
        weekKeys: List<String>,
        ignoredByDate: Map<String, List<String>>
    ): List<Int> {
        return weekKeys.map { key ->
            ignoredByDate[key]?.size ?: 0
        }
    }

    fun buildRealState(
        routinesCount: Int,
        doneTodayIds: List<String>,
        ignoredToday: List<String>,
        ignoredYesterdayCount: Int,
        weekProgress: List<Int>,
        ignoredPerDay: List<Int>
    ): StatsUiState {
        val totalToday = routinesCount
        val doneToday = doneTodayIds.size.coerceAtMost(totalToday)
        val progress = calculateProgress(totalToday, doneToday)
        val completedPercent = calculateCompletedPercent(totalToday, doneToday)

        return StatsUiState(
            totalToday = totalToday,
            doneToday = doneToday,
            progress = progress,
            completedPercent = completedPercent,
            totalRoutinesForStats = routinesCount,
            weekProgress = weekProgress,
            ignoredRemindersToday = ignoredToday,
            ignoredYesterdayCount = ignoredYesterdayCount,
            totalReceivedCount = routinesCount,
            ignoredPerDay = ignoredPerDay
        )
    }
}

private fun todayKey(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(Date())
}

@RequiresApi(Build.VERSION_CODES.O)
private fun dateKey(date: LocalDate): String {
    return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun currentWeekKeys(): List<String> {
    val today = LocalDate.now()
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return (0..6).map { offset ->
        dateKey(monday.plusDays(offset.toLong()))
    }
}

private fun frequencyToText(frequency: Frequency): String {
    return when (frequency) {
        Frequency.DAILY -> "Tous les jours"
        Frequency.SOME_DAYS -> "Certains jours"
        Frequency.ONCE -> "Une seule fois"
    }
}

private fun Routine.toRoutineLocationOrNull(): RoutineLocation? {
    return if (latitude != null && longitude != null) {
        RoutineLocation(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            locationName = locationName
        )
    } else {
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    openRoutineId: String? = null,
    onRoutineIntentConsumed: () -> Unit = {},
    routineViewModel: RoutineViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dependencies = remember(context) { createAppDependencies(context) }
    val notificationCoordinator = dependencies.notificationCoordinator

    val demoConfig = remember { DemoConfig(enabled = true) }

    val notificationsEnabled = remember { mutableStateOf(true) }
    val soundEnabled = remember { mutableStateOf(true) }
    val vibrationEnabled = remember { mutableStateOf(true) }

    val profileName = remember { mutableStateOf("Oussama Maaroufi") }
    val profileEmail = remember { mutableStateOf("oussama@email.com") }
    val profilePhone = remember { mutableStateOf("+1 000 000 0000") }

    val routines by routineViewModel.allRoutines.collectAsState(initial = emptyList())

    val doneByDate = remember {
        mutableStateMapOf<String, SnapshotStateList<String>>()
    }

    val ignoredByDate = remember {
        mutableStateMapOf<String, SnapshotStateList<String>>()
    }

    val currentDateKey = todayKey()
    val weekKeys = currentWeekKeys()

    val doneTodayIds = doneByDate.getOrPut(currentDateKey) {
        mutableStateListOf()
    }

    val realIgnoredRemindersToday = ignoredByDate.getOrPut(currentDateKey) {
        mutableStateListOf()
    }

    val pendingPickedLocation = remember { mutableStateOf<RoutineLocation?>(null) }

    val yesterdayKey = if (weekKeys.size >= 2) {
        weekKeys[weekKeys.lastIndex - 1]
    } else {
        currentDateKey
    }

    val realWeekProgress = StatsCalculator.calculateWeekProgress(
        weekKeys = weekKeys,
        doneByDate = doneByDate,
        routinesCount = routines.size
    )

    val realIgnoredPerDay = StatsCalculator.calculateIgnoredPerDay(
        weekKeys = weekKeys,
        ignoredByDate = ignoredByDate
    )

    val realStats = StatsCalculator.buildRealState(
        routinesCount = routines.size,
        doneTodayIds = doneTodayIds,
        ignoredToday = realIgnoredRemindersToday,
        ignoredYesterdayCount = ignoredByDate[yesterdayKey]?.size ?: 0,
        weekProgress = realWeekProgress,
        ignoredPerDay = realIgnoredPerDay
    )

    val statsState = if (demoConfig.enabled) {
        DemoStatsProvider.build(
            totalToday = realStats.totalToday,
            doneToday = realStats.doneToday,
            progress = realStats.progress
        )
    } else {
        realStats
    }

    LaunchedEffect(routines) {
        notificationCoordinator.syncAll(routines)
    }

    LaunchedEffect(openRoutineId, routines) {
        val targetId = openRoutineId ?: return@LaunchedEffect
        val routineExists = routines.any { it.id == targetId }

        if (routineExists) {
            navController.navigate(Screen.RoutineOptions.route(targetId))
            onRoutineIntentConsumed()
        }
    }

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
                    pendingPickedLocation.value = null
                    navController.navigate(Screen.AddRoutine.route(category))
                },
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryList.route(category))
                },
                onRoutineClick = { routine ->
                    navController.navigate(Screen.RoutineOptions.route(routine.id))
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
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onAdd = {
                    pendingPickedLocation.value = null
                    navController.navigate(Screen.AddRoutine.route(category))
                },
                onEdit = { routineId ->
                    navController.navigate(Screen.EditRoutine.route(routineId))
                },
                onDelete = { routineId ->
                    val routineToDelete = routines.firstOrNull { it.id == routineId }
                    if (routineToDelete != null) {
                        notificationCoordinator.onRoutineDeleted(routineToDelete)
                    }
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
                onSeeAllClick = {
                    navController.navigate(Screen.Statistics.route)
                },
                selectedTab = HomeTab.Suivi,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeTab.Home -> {
                            navController.popBackStack(Screen.Home.route, inclusive = false)
                        }
                        HomeTab.Suivi -> Unit
                    }
                },
                total = statsState.totalToday,
                done = statsState.doneToday,
                progress = statsState.progress
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onBack = { navController.popBackStack() },
                completedPercent = statsState.completedPercent,
                ignoredRemindersCount = statsState.ignoredRemindersToday.size,
                totalRoutines = statsState.totalRoutinesForStats,
                weekProgress = statsState.weekProgress,
                onIgnoredRemindersClick = {
                    navController.navigate(Screen.IgnoredReminders.route)
                }
            )
        }

        composable(Screen.IgnoredReminders.route) {
            IgnoredRemindersScreen(
                onBack = { navController.popBackStack() },
                ignoredReminders = statsState.ignoredRemindersToday,
                ignoredYesterdayCount = statsState.ignoredYesterdayCount,
                totalReceivedCount = statsState.totalReceivedCount,
                ignoredPerDay = statsState.ignoredPerDay
            )
        }

        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                onBack = { navController.popBackStack() },
                notificationsEnabled = notificationsEnabled.value,
                soundEnabled = soundEnabled.value,
                vibrationEnabled = vibrationEnabled.value,
                onNotificationsEnabledChange = { enabled ->
                    notificationsEnabled.value = enabled
                    NotificationHelper.createNotificationChannel(
                        context = context,
                        soundEnabled = soundEnabled.value,
                        vibrationEnabled = vibrationEnabled.value
                    )
                },
                onSoundEnabledChange = { enabled ->
                    soundEnabled.value = enabled
                    NotificationHelper.createNotificationChannel(
                        context = context,
                        soundEnabled = soundEnabled.value,
                        vibrationEnabled = vibrationEnabled.value
                    )
                },
                onVibrationEnabledChange = { enabled ->
                    vibrationEnabled.value = enabled
                    NotificationHelper.createNotificationChannel(
                        context = context,
                        soundEnabled = soundEnabled.value,
                        vibrationEnabled = vibrationEnabled.value
                    )
                },
                onTestNotificationClick = {
                    if (notificationsEnabled.value) {
                        NotificationHelper.showRoutineNotification(
                            context = context,
                            title = "Routine BabyPing",
                            message = "Ceci est une notification de test pour vérifier le système."
                        )
                    }
                }
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
                onDarkModeChange = onDarkModeChange,
                onNotificationsChange = { enabled ->
                    notificationsEnabled.value = enabled
                },
                onBack = { navController.popBackStack() },
                onNotificationsClick = {
                    navController.navigate(Screen.NotificationSettings.route)
                }
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
                selectedLocation = pendingPickedLocation.value,
                onPickLocation = {
                    navController.navigate(Screen.MapPicker.route)
                },
                onSave = { newRoutine ->
                    val finalRoutine = pendingPickedLocation.value?.let { pickedLocation ->
                        newRoutine.copy(
                            latitude = pickedLocation.latitude,
                            longitude = pickedLocation.longitude,
                            radius = pickedLocation.radius,
                            locationName = pickedLocation.locationName
                        )
                    } ?: newRoutine

                    val validation = RoutineValidator.validateForSave(finalRoutine)
                    if (!validation.isValid) {
                        return@AddRoutineScreen
                    }

                    routineViewModel.upsertRoutine(finalRoutine)
                    notificationCoordinator.onRoutineCreated(finalRoutine)
                    pendingPickedLocation.value = null
                    navController.popBackStack()
                },
                onBack = {
                    pendingPickedLocation.value = null
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditRoutine.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("id") ?: ""
            val routineToEdit = routines.firstOrNull { it.id == routineId }

            if (routineToEdit != null) {
                val selectedLocationForEdit =
                    pendingPickedLocation.value ?: routineToEdit.toRoutineLocationOrNull()

                AddRoutineScreen(
                    category = routineToEdit.category,
                    initialRoutine = routineToEdit,
                    selectedLocation = selectedLocationForEdit,
                    onPickLocation = {
                        pendingPickedLocation.value = selectedLocationForEdit
                        navController.navigate(Screen.MapPicker.route)
                    },
                    onSave = { updatedRoutine ->
                        val finalRoutine = pendingPickedLocation.value?.let { pickedLocation ->
                            updatedRoutine.copy(
                                latitude = pickedLocation.latitude,
                                longitude = pickedLocation.longitude,
                                radius = pickedLocation.radius,
                                locationName = pickedLocation.locationName
                            )
                        } ?: updatedRoutine

                        val validation = RoutineValidator.validateForSave(finalRoutine)
                        if (!validation.isValid) {
                            return@AddRoutineScreen
                        }

                        routineViewModel.upsertRoutine(finalRoutine)
                        notificationCoordinator.onRoutineUpdated(
                            oldRoutine = routineToEdit,
                            newRoutine = finalRoutine
                        )
                        pendingPickedLocation.value = null
                        navController.popBackStack()
                    },
                    onBack = {
                        pendingPickedLocation.value = null
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(
            route = Screen.RoutineOptions.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("id") ?: ""
            val routine = routines.firstOrNull { it.id == routineId }

            if (routine != null) {
                RoutineOptionsScreen(
                    routine = routine,
                    onBack = { navController.popBackStack() },
                    onEdit = {
                        navController.navigate(Screen.EditRoutine.route(routine.id))
                    },
                    onTrigger = {
                        pendingPickedLocation.value = routine.toRoutineLocationOrNull()
                        navController.navigate(Screen.MapPicker.route)
                    },
                    onDelete = {
                        notificationCoordinator.onRoutineDeleted(routine)
                        routineViewModel.deleteRoutineById(routine.id)
                        navController.popBackStack()
                    },
                    onToggleNotifications = { enabled ->
                        val updatedRoutine = routine.copy(notificationsEnabled = enabled)
                        routineViewModel.upsertRoutine(updatedRoutine)
                        notificationCoordinator.onNotificationsToggled(updatedRoutine)
                    }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(Screen.MapPicker.route) {
            MapPickerScreen(
                initialLocation = pendingPickedLocation.value,
                onBack = { navController.popBackStack() },
                onConfirmLocation = { pickedLocation ->
                    pendingPickedLocation.value = pickedLocation
                    navController.popBackStack()
                }
            )
        }
    }
}