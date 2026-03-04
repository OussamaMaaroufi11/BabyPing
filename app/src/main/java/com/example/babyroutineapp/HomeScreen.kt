package com.example.babyroutineapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BabyPingHomeScreen(
    routines: List<Routine>,
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    onNewReminderClick: (String) -> Unit,   // ✅ maintenant on envoie la catégorie choisie
    onCategoryClick: (String) -> Unit
) {
    val categories = remember { homeCategories }

    var showCategoryPicker by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BabyPingBottomBar(selected = selectedTab, onSelected = onTabSelected) },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(babyPingBackgroundBrush())
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {

                // -------- LOGO HEADER --------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.babyping),
                        contentDescription = "Logo BabyPing",
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(14.dp))

                // -------- CATEGORIES --------
                Text(
                    text = "Catégories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(categories) { cat ->
                        CategoryMiniCard(
                            title = cat.title,
                            icon = cat.icon,
                            count = routines.count { it.category == cat.title },
                            bgColor = cat.bgColor,
                            onClick = { onCategoryClick(cat.title) }
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                // -------- RAPPELS HEADER --------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rappels",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Text("Nouveau", style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.width(10.dp))

                    IconButton(
                        onClick = { showCategoryPicker = true },  // ✅ ouvre la liste
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0x55000000), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = Color.Black)
                    }
                }

                Spacer(Modifier.height(18.dp))

                // -------- LISTE RAPPELS --------
                if (routines.isEmpty()) {
                    EmptyRoutineCard(
                        text = "Aucune routine n’est enregistrée\npour le moment. Veuillez créer une\nnouvelle routine."
                    )
                } else {
                    ReminderList(
                        routines = routines,
                        frequencyTextProvider = { routine ->
                            when (routine.frequency) {
                                Frequency.DAILY -> "Tous les jours"
                                Frequency.SOME_DAYS -> "Certains jours"
                                Frequency.ONCE -> "Une seule fois"
                            }
                        }
                    )
                }

                Spacer(Modifier.weight(1f))
            }

            // ✅ Dialog choix catégorie
            if (showCategoryPicker) {
                AlertDialog(
                    onDismissRequest = { showCategoryPicker = false },
                    title = { Text("Choisir une catégorie") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            categories.forEach { cat ->
                                OutlinedButton(
                                    onClick = {
                                        showCategoryPicker = false
                                        onNewReminderClick(cat.title) // ✅ envoie la catégorie choisie
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(cat.title)
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

@Composable
private fun babyPingBackgroundBrush(): Brush =
    Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E9FF),
            Color(0xFFE8FBFF),
            Color(0xFFF6E9FF),
            Color(0xFFE8FBFF)
        )
    )