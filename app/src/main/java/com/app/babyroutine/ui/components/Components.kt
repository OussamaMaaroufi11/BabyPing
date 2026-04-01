package com.app.babyroutine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.babyroutine.model.Routine
import kotlin.math.absoluteValue

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
    val isDark = colors.background.luminance() < 0.5f

    val clickableModifier =
        if (onClick != null) Modifier.clickable { onClick() } else Modifier

    val cardColor =
        if (isDark) {
            Brush.linearGradient(
                colors = listOf(
                    colors.surface,
                    colors.surfaceVariant.copy(alpha = 0.95f)
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    bgColor.copy(alpha = 0.92f),
                    bgColor.copy(alpha = 0.78f)
                )
            )
        }

    Surface(
        shape = shape,
        color = Color.Transparent,
        modifier = Modifier
            .width(170.dp)
            .height(90.dp)
            .border(1.dp, colors.outline.copy(alpha = 0.28f), shape)
            .then(clickableModifier),
        shadowElevation = if (isDark) 10.dp else 6.dp
    ) {
        Box(
            modifier = Modifier
                .background(cardColor)
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDark) colors.background.copy(alpha = 0.9f)
                                else colors.surface.copy(alpha = 0.85f)
                            )
                            .border(
                                1.dp,
                                colors.outline.copy(alpha = 0.25f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isDark) colors.primary else colors.onSurface
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
                    color = if (isDark) colors.primary else colors.onSurface
                )
            }
        }
    }
}

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
            .border(
                1.dp,
                colors.outline.copy(alpha = 0.24f),
                RoundedCornerShape(22.dp)
            )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurface
        )
    }
}

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
                description = routine.description,
                time = routine.time,
                frequencyText = frequencyTextProvider(routine),
                onClick = { onRoutineClick(routine) }
            )
        }
    }
}

@Composable
fun ReminderCard(
    title: String,
    description: String,
    time: String,
    frequencyText: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)
    val isDark = colors.background.luminance() < 0.5f

    val lightColors = listOf(
        Color(0xFFDDEEFF),
        Color(0xFFFFF0C9),
        Color(0xFFFFE3E3),
        Color(0xFFE8F7EA)
    )

    val darkBrushes = listOf(
        Brush.linearGradient(
            listOf(colors.surface, colors.surfaceVariant.copy(alpha = 0.85f))
        ),
        Brush.linearGradient(
            listOf(colors.surfaceVariant.copy(alpha = 0.88f), colors.surface)
        ),
        Brush.linearGradient(
            listOf(colors.surface.copy(alpha = 0.98f), colors.surfaceVariant.copy(alpha = 0.82f))
        ),
        Brush.linearGradient(
            listOf(colors.surfaceVariant.copy(alpha = 0.82f), colors.surface.copy(alpha = 0.96f))
        )
    )

    val lightColor = lightColors[(title.hashCode().absoluteValue) % lightColors.size]
    val darkBrush = darkBrushes[(title.hashCode().absoluteValue) % darkBrushes.size]

    Surface(
        shape = shape,
        color = Color.Transparent,
        shadowElevation = if (isDark) 8.dp else 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.outline.copy(alpha = 0.30f), shape)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isDark) darkBrush else Brush.linearGradient(
                        listOf(lightColor, lightColor.copy(alpha = 0.92f))
                    )
                )
                .padding(14.dp)
        ) {
            Column {

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
                        color = if (isDark) colors.onSurface else colors.onSurface
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = null,
                        tint = if (isDark) colors.primary else colors.onSurface
                    )
                }

                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = description,
                        color = colors.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
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
                        tint = if (isDark) colors.primary else colors.onSurface
                    )
                }
            }
        }
    }
}