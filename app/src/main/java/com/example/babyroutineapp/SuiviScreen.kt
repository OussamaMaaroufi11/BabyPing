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
import androidx.compose.ui.graphics.Color
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
    val bg = Brush.verticalGradient(
        listOf(Color(0xFFF3E9FF), Color(0xFFE8FBFF), Color(0xFFF6E9FF))
    )

    val today = LocalDate.now()

    val mondayThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val mondayNextWeek = mondayThisWeek.plusWeeks(1)

    val thisWeek = buildWeek(startMonday = mondayThisWeek, selected = today)
    val nextWeek = buildWeek(startMonday = mondayNextWeek, selected = null)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Suivi", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            BabyPingBottomBar(
                selected = selectedTab,
                onSelected = onTabSelected
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Carte verte
            ProgressCard(
                dateText = formatFrenchDate(today),
                routineText = "${done.toString().padStart(2, '0')}/${total.toString().padStart(2, '0')}",
                progress = progress
            )

            Spacer(Modifier.height(18.dp))

            // Parcours header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PARCOURS", fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                Text("TOUT VOIR", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(10.dp))

            // Bloc calendrier AUTOMATIQUE
            CalendarCard(
                thisWeek = thisWeek,
                nextWeek = nextWeek
            )

            Spacer(Modifier.weight(1f))
        }
    }
}

// ------------------ UI COMPONENTS ------------------

@Composable
private fun ProgressCard(
    dateText: String,
    routineText: String,
    progress: Float
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        shape = shape,
        color = Color(0xFFB9FFB2),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(dateText, fontWeight = FontWeight.Black)

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ROUTINE", color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodySmall)
                    Text(routineText, fontWeight = FontWeight.Black)
                }

                Spacer(Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Text("PROGRESSION", color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodySmall)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            progress = { progress.coerceIn(0f, 1f) },
                            modifier = Modifier.size(26.dp),
                            strokeWidth = 4.dp,
                            color = Color(0xFF2E7D32),
                            trackColor = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("${(progress.coerceIn(0f, 1f) * 100).toInt()} %", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

data class DayBubble(val day: String, val letter: String, val selected: Boolean)

@Composable
private fun CalendarCard(
    thisWeek: List<DayBubble>,
    nextWeek: List<DayBubble>
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        shape = shape,
        color = Color(0xFFF2D7C6),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text("CETTE SEMAINE", fontWeight = FontWeight.Black)

            Spacer(Modifier.height(10.dp))
            WeekRow(thisWeek)

            Spacer(Modifier.height(14.dp))
            Text("SEMAINE PROCHAINE", fontWeight = FontWeight.Black)

            Spacer(Modifier.height(10.dp))
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
        days.forEach { d ->
            DayItem(day = d.day, letter = d.letter, selected = d.selected)
        }
    }
}

@Composable
private fun DayItem(day: String, letter: String, selected: Boolean) {
    val bg = if (selected) Color(0xFFB9FFB2) else Color(0xFFEEDFD5)
    val border = if (selected) Color(0xFF2E7D32) else Color(0x33000000)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(bg)
                .border(1.dp, border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(day, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Text(letter, style = MaterialTheme.typography.bodySmall, color = Color.Black)
    }
}

// ------------------ HELPERS (AUTO DATE + WEEK) ------------------

@RequiresApi(Build.VERSION_CODES.O)
private fun formatFrenchDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)
    return date.format(formatter).uppercase(Locale.FRENCH)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun frenchLetter(d: DayOfWeek): String = when (d) {
    DayOfWeek.MONDAY -> "L"
    DayOfWeek.TUESDAY -> "M"
    DayOfWeek.WEDNESDAY -> "M"
    DayOfWeek.THURSDAY -> "J"
    DayOfWeek.FRIDAY -> "V"
    DayOfWeek.SATURDAY -> "S"
    DayOfWeek.SUNDAY -> "D"
}

@RequiresApi(Build.VERSION_CODES.O)
private fun buildWeek(startMonday: LocalDate, selected: LocalDate?): List<DayBubble> {
    return (0..6).map { i ->
        val d = startMonday.plusDays(i.toLong())
        DayBubble(
            day = "%02d".format(d.dayOfMonth),
            letter = frenchLetter(d.dayOfWeek),
            selected = (selected != null && d == selected)
        )
    }
}
