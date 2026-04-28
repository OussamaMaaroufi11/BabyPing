package com.app.babyroutine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    completedPercent: Int,
    ignoredRemindersCount: Int,
    totalRoutines: Int,
    weekProgress: List<Int>,
    onIgnoredRemindersClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val safeWeekProgress = weekProgress.take(7).let { values ->
        if (values.size == 7) {
            values
        } else {
            List(7) { index -> values.getOrElse(index) { 0 } }
        }
    }

    val weekLabels = listOf("L", "M", "M", "J", "V", "S", "D")

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.22f),
            colors.background
        )
    )

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistiques",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onSurface,
                    navigationIconContentColor = colors.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Résumé détaillé des routines",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFF2C9F2)
            )

            CompletedCard(
                percent = completedPercent
            )

            IgnoredCard(
                ignoredRemindersCount = ignoredRemindersCount,
                onClick = onIgnoredRemindersClick
            )

            TotalRoutinesCard(
                totalRoutines = totalRoutines,
                weekProgress = safeWeekProgress,
                weekLabels = weekLabels
            )
        }
    }
}

@Composable
private fun CompletedCard(percent: Int) {
    val colors = MaterialTheme.colorScheme
    val accent = Color(0xFF76C26B)
    val safePercent = percent.coerceIn(0, 100)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.7f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${safePercent.toString().padStart(2, '0')}%",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = accent
            )

            Spacer(modifier = Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Routines complétées",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Text(
                    text = "Aujourd’hui",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant.copy(alpha = 0.18f))
                    .border(
                        width = 2.dp,
                        color = accent.copy(alpha = 0.75f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$safePercent%",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
            }
        }
    }
}

@Composable
private fun IgnoredCard(
    ignoredRemindersCount: Int,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val accent = Color(0xFFF4B400)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.85f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Rappels ignorés",
                    tint = accent,
                    modifier = Modifier.size(28.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6842))
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = ignoredRemindersCount.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = accent
            )

            Spacer(modifier = Modifier.size(14.dp))

            Column {
                Text(
                    text = "Rappels ignorés",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Text(
                    text = "Aujourd’hui",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TotalRoutinesCard(
    totalRoutines: Int,
    weekProgress: List<Int>,
    weekLabels: List<String>
) {
    val colors = MaterialTheme.colorScheme
    val accent = Color(0xFFB16486)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.55f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = totalRoutines.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = accent
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Column {
                        Text(
                            text = "Routines totales",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )

                        Text(
                            text = "Cette semaine",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = accent.copy(alpha = 0.28f)
            )

            WeeklyBarChart(
                values = weekProgress,
                labels = weekLabels,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun WeeklyBarChart(
    values: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val barColors = listOf(
        Color(0xFF9FD7D3),
        Color(0xFFB9D9B4),
        Color(0xFFC9DEBE),
        Color(0xFFD0E4CA),
        Color(0xFFC3DEB8),
        Color(0xFFF3B3B3),
        Color(0xFFF0A9B0)
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            val safeValue = value.coerceIn(0, 100)
            val barHeight = safeValue.coerceIn(10, 100) * 0.55.dp

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "$safeValue%",
                    color = if (safeValue <= 50) {
                        Color(0xFFE45B5B)
                    } else {
                        Color(0xFF5A9A73)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .height(barHeight)
                        .width(24.dp)
                        .background(
                            color = barColors[index % barColors.size],
                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                        )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = labels[index],
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}