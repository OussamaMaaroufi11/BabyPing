package com.example.babyroutineapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

// ---------------- CATEGORY MINI CARD ----------------

@Composable
fun CategoryMiniCard(
    title: String,
    icon: ImageVector,
    count: Int,
    bgColor: Color,
    onClick: (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)

    val clickableModifier =
        if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Surface(
        shape = shape,
        color = bgColor.copy(alpha = 0.85f),
        modifier = Modifier
            .width(170.dp)
            .height(90.dp)
            .border(1.dp, colors.outline.copy(alpha = 0.3f), shape)
            .then(clickableModifier),
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colors.surface.copy(alpha = 0.75f))
                        .border(
                            1.dp,
                            colors.outline.copy(alpha = 0.3f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = colors.onSurface
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = colors.onSurface
            )
        }
    }
}

// ---------------- EMPTY ----------------

@Composable
fun EmptyRoutineCard(text: String) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurface
        )
    }
}

// ---------------- LIST ----------------

@Composable
fun ReminderList(
    routines: List<Routine>,
    frequencyTextProvider: (Routine) -> String,
    onRoutineClick: (Routine) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        items(routines, key = { it.id }) { routine ->
            ReminderCard(
                title = routine.title,
                time = routine.time,
                frequencyText = frequencyTextProvider(routine),
                onClick = { onRoutineClick(routine) }
            )
        }
    }
}

// ---------------- CARD ----------------

@Composable
fun ReminderCard(
    title: String,
    time: String,
    frequencyText: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)

    val lightColors = listOf(
        Color(0xFFDDEEFF),
        Color(0xFFFFF0C9),
        Color(0xFFFFE3E3),
        Color(0xFFE8F7EA)
    )

    val darkColors = listOf(
        colors.surfaceVariant,
        colors.surface,
        colors.surfaceVariant.copy(alpha = 0.9f),
        colors.surface.copy(alpha = 0.92f)
    )

    val isDark = colors.background.luminance() < 0.5f
    val bgList = if (isDark) darkColors else lightColors
    val backgroundColor = bgList[(title.hashCode().absoluteValue) % bgList.size]

    Surface(
        shape = shape,
        color = backgroundColor,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.outline.copy(alpha = 0.35f), shape)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface
                )

                Text(
                    text = time,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(Icons.Default.Alarm, null, tint = colors.onSurface)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = frequencyText,
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = colors.onSurface
                )
            }
        }
    }
}