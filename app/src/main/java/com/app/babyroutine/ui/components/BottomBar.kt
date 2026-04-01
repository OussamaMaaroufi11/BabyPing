package com.app.babyroutine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.model.HomeTab

@Composable
fun BabyPingBottomBar(
    selected: HomeTab,
    onSelected: (HomeTab) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        color = colors.background,
        tonalElevation = 3.dp,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BigBottomItem(
                label = "Accueil",
                icon = Icons.Default.Home,
                selected = selected == HomeTab.Home,
                onClick = { onSelected(HomeTab.Home) }
            )

            BigBottomItem(
                label = "Suivi",
                icon = Icons.Default.CalendarToday,
                selected = selected == HomeTab.Suivi,
                onClick = { onSelected(HomeTab.Suivi) }
            )
        }
    }
}

@Composable
private fun BigBottomItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val itemShape = RoundedCornerShape(18.dp)

    val backgroundColor =
        if (selected) colors.secondary.copy(alpha = 0.9f)
        else colors.surfaceVariant.copy(alpha = 0.45f)

    val contentColor =
        if (selected) Color.Black
        else colors.onSurfaceVariant

    val borderColor =
        if (selected) colors.secondary.copy(alpha = 0.55f)
        else colors.outline.copy(alpha = 0.22f)

    val textColor =
        if (selected) colors.onBackground
        else colors.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(124.dp)
            .semantics { this.selected = selected }
            .clickable(
                role = Role.Tab,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(66.dp)
                .clip(itemShape)
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = itemShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            color = textColor
        )
    }
}