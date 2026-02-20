package com.example.babyroutineapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BabyPingBottomBar(
    selected: HomeTab,
    onSelected: (HomeTab) -> Unit
) {
    Surface(color = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp, top = 8.dp),
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
                icon = Icons.Default.FavoriteBorder,
                selected = selected == HomeTab.Suivi,
                onClick = { onSelected(HomeTab.Suivi) }
            )
        }
    }
}

@Composable
private fun BigBottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0xFF9BE7B4) else Color(0xFF6FD7D7)
    val shape = RoundedCornerShape(16.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(124.dp)
            .semantics {
                this.selected = selected
            }
            .clickable(
                role = Role.Tab,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(shape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color.Black,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}
