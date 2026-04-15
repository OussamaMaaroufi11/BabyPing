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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    notificationsEnabled: Boolean,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onSoundEnabledChange: (Boolean) -> Unit,
    onVibrationEnabledChange: (Boolean) -> Unit,
    onTestNotificationClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

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
                        text = "Notifications",
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

            NotificationOptionCard(
                title = "Activer les notifications",
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChange
            )

            NotificationOptionCard(
                title = "Sons",
                checked = soundEnabled,
                onCheckedChange = onSoundEnabledChange,
                enabled = notificationsEnabled
            )

            NotificationOptionCard(
                title = "Vibrations",
                checked = vibrationEnabled,
                onCheckedChange = onVibrationEnabledChange,
                enabled = notificationsEnabled
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onTestNotificationClick,
                enabled = notificationsEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = colors.outline.copy(alpha = 0.3f),
                    disabledContentColor = colors.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Tester la notification",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun NotificationOptionCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.16f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) colors.onSurface else colors.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = colors.primary.copy(alpha = 0.78f),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = colors.outline.copy(alpha = 0.45f),
                    disabledCheckedThumbColor = Color.White,
                    disabledUncheckedThumbColor = Color.White,
                    disabledCheckedTrackColor = colors.primary.copy(alpha = 0.30f),
                    disabledUncheckedTrackColor = colors.outline.copy(alpha = 0.20f)
                )
            )
        }
    }
}
