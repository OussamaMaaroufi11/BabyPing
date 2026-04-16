package com.app.babyroutine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IgnoredRemindersScreen(
    onBack: () -> Unit,
    ignoredReminders: List<String>,
    ignoredYesterdayCount: Int,
    totalReceivedCount: Int,
    ignoredPerDay: List<Int>
) {
    val colors = MaterialTheme.colorScheme
    val safeIgnoredPerDay = ignoredPerDay.take(7).let {
        if (it.size == 7) it else List(7) { index -> it.getOrElse(index) { 0 } }
    }

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
                        text = "Rappels ignorés",
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Détails et analyse de cette semaine",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFF2C9F2)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallSummaryCard(
                    modifier = Modifier.weight(1f),
                    value = ignoredReminders.size.toString(),
                    title = "Total ignorés",
                    subtitle = "Aujourd’hui",
                    accent = Color(0xFFF4B400),
                    bg = Color(0xFFF8F0DE),
                    icon = Icons.Default.Notifications
                )

                SmallSummaryCard(
                    modifier = Modifier.weight(1f),
                    value = ignoredYesterdayCount.toString(),
                    title = "Hier",
                    subtitle = "Jour précédent",
                    accent = Color(0xFFE06464),
                    bg = Color(0xFFFBEDED),
                    icon = Icons.Default.NotificationsNone
                )

                SmallSummaryCard(
                    modifier = Modifier.weight(1f),
                    value = totalReceivedCount.toString(),
                    title = "Total reçus",
                    subtitle = "Aujourd’hui",
                    accent = Color(0xFF59D2CC),
                    bg = Color(0xFFE6F8F7),
                    icon = Icons.Default.Notifications
                )
            }

            DailyEvolutionCard(
                values = safeIgnoredPerDay,
                labels = listOf("Lun.", "Mar.", "Mer.", "Jeu.", "Ven.", "Sam.", "Dim.")
            )

            LatestIgnoredCard(
                reminders = ignoredReminders
            )
        }
    }
}

@Composable
private fun SmallSummaryCard(
    modifier: Modifier = Modifier,
    value: String,
    title: String,
    subtitle: String,
    accent: Color,
    bg: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = bg,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun DailyEvolutionCard(
    values: List<Int>,
    labels: List<String>
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Évolution quotidienne",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEachIndexed { index, value ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .height((value.coerceAtLeast(1)) * 14.dp)
                                .size(width = 20.dp, height = (value.coerceAtLeast(1)) * 14.dp)
                                .background(
                                    color = Color(0xFFF49898),
                                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = labels[index],
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LatestIgnoredCard(
    reminders: List<String>
) {
    val colors = MaterialTheme.colorScheme
    val backgrounds = listOf(
        Color(0xFFE5EEF8),
        Color(0xFFF4EBCF),
        Color(0xFFF8E3E6)
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Derniers rappels ignorés",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFF2C9F2)
            )

            if (reminders.isEmpty()) {
                Text(
                    text = "Aucun rappel ignoré pour le moment.",
                    modifier = Modifier.padding(16.dp),
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    reminders.take(3).forEachIndexed { index, reminder ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = backgrounds[index % backgrounds.size],
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reminder,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2A2A2A)
                                    )
                                    Text(
                                        text = "Aujourd’hui",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.DarkGray
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color(0xFF2A2A2A),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}