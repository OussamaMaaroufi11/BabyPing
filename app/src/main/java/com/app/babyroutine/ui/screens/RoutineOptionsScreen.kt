package com.app.babyroutine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Priority
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
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                title = {
                    Text(
                        text = "Détails de la routine",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = colors.surface,
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colors.outline.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = routine.title,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = routine.time,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (routine.description.isNotBlank()) {
                        Text(
                            text = routine.description,
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    InfoRow(
                        icon = Icons.Default.Schedule,
                        label = "Périodicité",
                        value = frequencyLabel(routine.frequency)
                    )

                    InfoRow(
                        icon = Icons.Default.Edit,
                        label = "Priorité",
                        value = priorityLabel(routine.priority)
                    )

                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Déclenchement",
                        value = if (routine.hasLocationTrigger) {
                            routine.locationName ?: "Zone personnalisée"
                        } else {
                            "Aucun lieu défini"
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            Text(
                                text = "Notifications",
                                color = colors.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Switch(
                            checked = routine.notificationsEnabled,
                            onCheckedChange = onToggleNotifications,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = colors.primary.copy(alpha = 0.78f),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = colors.outline.copy(alpha = 0.45f)
                            )
                        )
                    }
                }
            }

            ActionCard(
                text = "Modifier la routine",
                subtitle = "Mettre à jour le nom, l’horaire ou la priorité",
                icon = Icons.Default.Edit,
                iconTint = Color(0xFF4A7DFF),
                containerColor = Color(0xFFEAF0FF),
                onClick = onEdit
            )

            ActionCard(
                text = "Gérer le lieu de déclenchement",
                subtitle = "Choisir ou modifier la zone contextuelle",
                icon = Icons.Default.LocationOn,
                iconTint = Color(0xFF8A5CF6),
                containerColor = Color(0xFFEEE5FF),
                onClick = onTrigger
            )

            ActionCard(
                text = "Supprimer la routine",
                subtitle = "Retirer définitivement cette routine",
                icon = Icons.Default.Delete,
                iconTint = Color(0xFFFF5B5B),
                containerColor = Color(0xFFFFE8E8),
                onClick = {
                    showDeleteDialog = true
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = colors.surface,
            shape = RoundedCornerShape(28.dp),
            title = {
                Text(
                    text = "Confirmation",
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Voulez-vous vraiment supprimer cette routine ?",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        text = "Annuler",
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(
                        text = "Supprimer",
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    val colors = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = colors.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier.size(34.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = colors.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colors.onSurface
            )
        }
    }
}

@Composable
private fun ActionCard(
    text: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            Text(
                text = ">",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
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

private fun priorityLabel(priority: Priority): String {
    return when (priority) {
        Priority.LOW -> "Faible"
        Priority.MEDIUM -> "Moyenne"
        Priority.HIGH -> "Élevée"
    }
}