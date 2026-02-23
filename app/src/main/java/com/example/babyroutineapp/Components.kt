package com.example.babyroutineapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun CategoryMiniCard(
    title: String,
    icon: ImageVector,
    count: Int,
    bgColor: Color,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(16.dp)

    val clickableModifier =
        if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Surface(
        shape = shape,
        color = bgColor,
        modifier = Modifier
            .width(170.dp)
            .height(86.dp)
            .border(1.dp, Color(0x55000000), shape)
            .then(clickableModifier),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.75f))
                        .border(1.dp, Color(0x22000000), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = Color.Black)
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun EmptyRoutineCard(text: String) {
    val shape = RoundedCornerShape(22.dp)
    Surface(
        shape = shape,
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

@Composable
fun ReminderList(
    routines: List<Routine>,
    frequencyTextProvider: (Routine) -> String
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(routines) { routine ->
            ReminderCard(
                title = routine.title,
                time = routine.time,
                frequencyText = frequencyTextProvider(routine)
            )
        }

        item { Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
fun ReminderCard(
    title: String,
    time: String,
    frequencyText: String
) {
    val shape = RoundedCornerShape(16.dp)

    // Couleurs alternées comme la maquette
    val bgColors = listOf(
        Color(0xFFDDEEFF),
        Color(0xFFFFF0C9),
        Color(0xFFFFE3E3),
        Color(0xFFE8F7EA)
    )
    val bg = bgColors[(title.hashCode().absoluteValue) % bgColors.size]

    Surface(
        shape = shape,
        color = bg,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x33000000), shape)
    ) {
        Column(Modifier.padding(14.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium
                )

                Text(text = time, fontWeight = FontWeight.Bold)

                Spacer(Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = frequencyText,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
