package com.app.babyroutine.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.R
import com.app.babyroutine.data.homeCategories
import com.app.babyroutine.model.HomeTab
import com.app.babyroutine.model.Routine
import com.app.babyroutine.ui.components.BabyPingBottomBar
import com.app.babyroutine.ui.components.CategoryMiniCard
import com.app.babyroutine.ui.components.EmptyRoutineCard
import com.app.babyroutine.ui.components.ReminderList

@Composable
fun BabyPingHomeScreen(
    routines: List<Routine>,
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    onNewReminderClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onRoutineClick: (Routine) -> Unit,
    onSettingsClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val categories = remember { homeCategories }
    var showCategoryPicker by remember { mutableStateOf(false) }


    Scaffold(
        bottomBar = {
            BabyPingBottomBar(
                selected = selectedTab,
                onSelected = onTabSelected
            )
        },
        containerColor = colors.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(babyPingBackgroundBrush())
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Paramètres",
                            tint = colors.primary,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.babyping),
                        contentDescription = "Logo BabyPing",
                        modifier = Modifier.size(90.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Catégories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories) { category ->
                        CategoryMiniCard(
                            title = category.title,
                            icon = category.icon,
                            count = routines.count { it.category == category.title },
                            bgColor = category.bgColor,
                            onClick = { onCategoryClick(category.title) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Rappels",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text("Nouveau", color = colors.onSurfaceVariant)

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = { showCategoryPicker = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, colors.outline, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, null)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (routines.isEmpty()) {
                    EmptyRoutineCard("Aucune routine enregistrée.")
                } else {
                    Box(modifier = Modifier.weight(1f)) {
                        ReminderList(
                            routines = routines,
                            frequencyTextProvider = { it.frequency.toDisplayText() },
                            onRoutineClick = onRoutineClick
                        )
                    }
                }
            }

            if (showCategoryPicker) {
                val isDark = colors.background.luminance() < 0.5f

                AlertDialog(
                    onDismissRequest = { showCategoryPicker = false },
                    modifier = Modifier.shadow(
                        20.dp,
                        shape = RoundedCornerShape(30.dp),
                        ambientColor = colors.primary.copy(alpha = 0.2f),
                        spotColor = colors.primary.copy(alpha = 0.3f)
                    ),
                    containerColor = colors.surface,
                    shape = RoundedCornerShape(30.dp),
                    title = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Choisir une catégorie",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )

                            Text(
                                text = "Sélectionne le type de routine à ajouter",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant
                            )
                        }
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            categories.forEach { category ->

                                val bgColor =
                                    if (isDark) colors.surfaceVariant.copy(alpha = 0.75f)
                                    else category.bgColor

                                OutlinedButton(
                                    onClick = {
                                        showCategoryPicker = false
                                        onNewReminderClick(category.title)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(58.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = bgColor,
                                        contentColor = colors.onSurface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(colors.background.copy(alpha = 0.9f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                category.icon,
                                                null,
                                                tint = colors.primary
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            category.title,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showCategoryPicker = false }) {
                            Text("Annuler")
                        }
                    }
                )
            }
        }
    }
}

private data class SmartSuggestion(
    val category: String,
    val message: String
)

@Composable
private fun SmartSuggestionCard(
    message: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.primary.copy(alpha = 0.15f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lightbulb, null, tint = colors.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = colors.onSurface
            )
        }
    }
}

@Composable
private fun babyPingBackgroundBrush(): Brush {
    val colors = MaterialTheme.colorScheme
    return Brush.verticalGradient(
        listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.4f),
            colors.background
        )
    )
}