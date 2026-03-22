package com.example.babyroutineapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    val suggestion: SmartSuggestion? = remember(routines) { buildSmartSuggestion(routines) }

    Scaffold(
        bottomBar = {
            BabyPingBottomBar(selected = selectedTab, onSelected = onTabSelected)
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

                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.babyping),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, null, tint = colors.primary)
                    }
                }

                Spacer(Modifier.height(14.dp))

                // CATEGORIES
                Text(
                    "Catégories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onBackground
                )

                Spacer(Modifier.height(10.dp))

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

                Spacer(Modifier.height(18.dp))

                // SMART CARD
                suggestion?.let {
                    SmartSuggestionCard(
                        message = it.message,
                        onClick = { onNewReminderClick(it.category) }
                    )
                    Spacer(Modifier.height(18.dp))
                }

                // HEADER LIST
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

                    Spacer(Modifier.weight(1f))

                    Text("Nouveau", color = colors.onSurfaceVariant)

                    Spacer(Modifier.width(10.dp))

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

                Spacer(Modifier.height(18.dp))

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

            // -------- DIALOG FINAL --------
            if (showCategoryPicker) {

                val isDark = colors.background.luminance() < 0.5f

                AlertDialog(
                    onDismissRequest = { showCategoryPicker = false },
                    containerColor = colors.surface,
                    shape = RoundedCornerShape(30.dp),

                    // ✅ SANS LE "+"
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
                                    if (isDark) colors.surfaceVariant
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
                                                .background(colors.surface),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                category.icon,
                                                null,
                                                tint = colors.primary
                                            )
                                        }

                                        Spacer(Modifier.width(12.dp))

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

// -------- SMART --------

private data class SmartSuggestion(
    val category: String,
    val message: String
)

private fun buildSmartSuggestion(routines: List<Routine>): SmartSuggestion? {
    if (routines.isEmpty()) {
        return SmartSuggestion(
            "Quotidiens",
            "Commence avec une routine Quotidienne 🌱"
        )
    }

    return null
}

@Composable
private fun SmartSuggestionCard(
    message: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lightbulb, null, tint = colors.primary)
            Spacer(Modifier.width(12.dp))
            Text(message)
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