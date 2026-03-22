package com.example.babyroutineapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuiviScreen(
    onBack: () -> Unit,
    selectedTab: HomeTab = HomeTab.Suivi,
    onTabSelected: (HomeTab) -> Unit = {},
    total: Int,
    done: Int,
    progress: Float
) {
    val colors = MaterialTheme.colorScheme

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.4f),
            colors.background
        )
    )

    val today = LocalDate.now()
    val mondayThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val mondayNextWeek = mondayThisWeek.plusWeeks(1)

    val currentWeek = buildWeek(mondayThisWeek, today)
    val nextWeek = buildWeek(mondayNextWeek, null)

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = { Text("Suivi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onSurface
                )
            )
        },
        bottomBar = {
            BabyPingBottomBar(selected = selectedTab, onSelected = onTabSelected)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(10.dp))

            ProgressCard(
                dateText = formatFrenchDate(today),
                routineText = "${done.toString().padStart(2, '0')}/${total.toString().padStart(2, '0')}",
                progress = progress
            )

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PARCOURS", fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                Text("TOUT VOIR", color = colors.onSurfaceVariant)
            }

            Spacer(Modifier.height(12.dp))

            CalendarCard(currentWeek, nextWeek)

            Spacer(Modifier.weight(1f))
        }
    }
}

// -------------------- PROGRESS --------------------

@Composable
private fun ProgressCard(
    dateText: String,
    routineText: String,
    progress: Float
) {
    val colors = MaterialTheme.colorScheme
    val safeProgress = progress.coerceIn(0f, 1f)

    val gradient = Brush.linearGradient(
        colors = listOf(
            colors.secondaryContainer,
            colors.primary.copy(alpha = 0.25f)
        )
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(18.dp)
        ) {
            Column {

                Text(dateText, fontWeight = FontWeight.Black)

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Column {
                        Text("ROUTINE", style = MaterialTheme.typography.bodySmall)
                        Text(routineText, fontWeight = FontWeight.Black)
                    }

                    Spacer(Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        CircularProgressIndicator(
                            progress = { safeProgress },
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 4.dp,
                            color = colors.primary
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("${(safeProgress * 100).toInt()} %")
                    }
                }
            }
        }
    }
}

// -------------------- CALENDAR --------------------

@Composable
private fun CalendarCard(
    thisWeek: List<DayBubble>,
    nextWeek: List<DayBubble>
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surfaceVariant,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp)) {

            Text("CETTE SEMAINE", fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            WeekRow(thisWeek)

            Spacer(Modifier.height(18.dp))

            Text("SEMAINE PROCHAINE", fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            WeekRow(nextWeek)
        }
    }
}

@Composable
private fun WeekRow(days: List<DayBubble>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach {
            DayItem(it.day, it.letter, it.selected)
        }
    }
}

@Composable
private fun DayItem(day: String, letter: String, selected: Boolean) {
    val colors = MaterialTheme.colorScheme

    val bg = if (selected) colors.primary.copy(alpha = 0.25f) else colors.surface
    val border = if (selected) colors.primary else colors.outline.copy(alpha = 0.4f)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(bg)
                .border(1.dp, border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(day, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(6.dp))
        Text(letter, style = MaterialTheme.typography.bodySmall)
    }
}

// -------------------- UTILS --------------------

data class DayBubble(
    val day: String,
    val letter: String,
    val selected: Boolean
)

@RequiresApi(Build.VERSION_CODES.O)
private fun buildWeek(startMonday: LocalDate, selectedDate: LocalDate?): List<DayBubble> {
    return (0..6).map {
        val date = startMonday.plusDays(it.toLong())
        DayBubble(
            day = "%02d".format(date.dayOfMonth),
            letter = frenchLetter(date.dayOfWeek),
            selected = selectedDate == date
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun frenchLetter(day: DayOfWeek) = when (day) {
    DayOfWeek.MONDAY -> "L"
    DayOfWeek.TUESDAY -> "M"
    DayOfWeek.WEDNESDAY -> "M"
    DayOfWeek.THURSDAY -> "J"
    DayOfWeek.FRIDAY -> "V"
    DayOfWeek.SATURDAY -> "S"
    DayOfWeek.SUNDAY -> "D"
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatFrenchDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH))
}