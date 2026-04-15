package com.app.babyroutine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Routine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineOptionsScreen(
    routine: Routine,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onTrigger: () -> Unit,
    onDelete: () -> Unit,
    onToggleNotifications: (Boolean) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.35f),
            colors.background
        )
    )

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = { Text("Modifier Routine", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = colors.surface,
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = routine.title,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )
                        Text(
                            text = routine.time,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = frequencyLabel(routine.frequency),
                        color = colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Désactiver l’alarme",
                            modifier = Modifier.weight(1f),
                            color = colors.onSurfaceVariant
                        )
                        Switch(
                            checked = routine.notificationsEnabled,
                            onCheckedChange = onToggleNotifications
                        )
                    }
                }
            }

            ActionCard(
                text = "Modifier la routine",
                icon = Icons.Default.Edit,
                iconTint = Color(0xFF4A7DFF),
                containerColor = Color(0xFFEAF0FF),
                onClick = onEdit
            )

            ActionCard(
                text = "Déclenchement",
                icon = Icons.Default.LocationOn,
                iconTint = Color(0xFF8A5CF6),
                containerColor = Color(0xFFEEE5FF),
                onClick = onTrigger
            )

            ActionCard(
                text = "Supprimer la routine",
                icon = Icons.Default.Delete,
                iconTint = Color(0xFFFF5B5B),
                containerColor = Color(0xFFFFE8E8),
                onClick = onDelete
            )
        }
    }
}

@Composable
private fun ActionCard(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = iconTint)
            Spacer(modifier = Modifier.height(0.dp).weight(0f))
            Text(
                text = text,
                modifier = Modifier.padding(start = 12.dp).weight(1f),
                fontWeight = FontWeight.Medium
            )
            Text(">")
        }
    }
}

private fun frequencyLabel(frequency: Frequency): String {
    return when (frequency) {
        Frequency.DAILY -> "Tous les jours"
        Frequency.SOME_DAYS -> "Certains jours"
        Frequency.ONCE -> "Une seule fois"
    }
}