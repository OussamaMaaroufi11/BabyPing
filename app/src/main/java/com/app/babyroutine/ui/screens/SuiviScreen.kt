package com.app.babyroutine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.model.HomeTab
import com.app.babyroutine.ui.components.BabyPingBottomBar
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Suivi",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onSurface,
                    navigationIconContentColor = colors.onSurface
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BabyPingBottomBar(
                selected = selectedTab,
                onSelected = onTabSelected
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            ProgressCard(
                dateText = formatFrenchDate(today),
                routineText = "${done.toString().padStart(2, '0')}/${total.toString().padStart(2, '0')}",
                progress = progress
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "PARCOURS",
                    fontWeight = FontWeight.Black,
                    color = colors.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "TOUT VOIR",
                    color = colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            CalendarCard(
                thisWeek = currentWeek,
                nextWeek = nextWeek
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

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
            colors.surface,
            colors.primary.copy(alpha = 0.18f)
        )
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = dateText,
                    fontWeight = FontWeight.Black,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            text = "ROUTINE",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                        Text(
                            text = routineText,
                            fontWeight = FontWeight.Black,
                            color = colors.primary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            progress = { safeProgress },
                            modifier = Modifier.size(34.dp),
                            strokeWidth = 4.dp,
                            color = colors.primary
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "${(safeProgress * 100).toInt()} %",
                            color = colors.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarCard(
    thisWeek: List<DayBubble>,
    nextWeek: List<DayBubble>
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.25f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "CETTE SEMAINE",
                fontWeight = FontWeight.Black,
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(12.dp))
            WeekRow(thisWeek)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "SEMAINE PROCHAINE",
                fontWeight = FontWeight.Black,
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(12.dp))
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
        days.forEach { day ->
            DayItem(
                day = day.day,
                letter = day.letter,
                selected = day.selected
            )
        }
    }
}

@Composable
private fun DayItem(
    day: String,
    letter: String,
    selected: Boolean
) {
    val colors = MaterialTheme.colorScheme

    val bg =
        if (selected) colors.primary.copy(alpha = 0.25f)
        else colors.surfaceVariant.copy(alpha = 0.4f)

    val border =
        if (selected) colors.primary
        else colors.outline.copy(alpha = 0.25f)

    val textColor =
        if (selected) colors.primary
        else colors.onSurface

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(bg)
                .border(
                    width = 1.dp,
                    color = border,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = letter,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant
        )
    }
}

data class DayBubble(
    val day: String,
    val letter: String,
    val selected: Boolean
)

@RequiresApi(Build.VERSION_CODES.O)
private fun buildWeek(
    startMonday: LocalDate,
    selectedDate: LocalDate?
): List<DayBubble> {
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
    return date.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)
    )
}