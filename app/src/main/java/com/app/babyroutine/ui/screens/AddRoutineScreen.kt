package com.app.babyroutine.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.R
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Priority
import com.app.babyroutine.model.Routine
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineScreen(
    category: String,
    initialRoutine: Routine?,
    onSave: (Routine) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var title by remember { mutableStateOf(initialRoutine?.title ?: "") }
    var description by remember { mutableStateOf(initialRoutine?.description ?: "") }
    var time by remember { mutableStateOf(initialRoutine?.time ?: "") }
    var frequency by remember { mutableStateOf(initialRoutine?.frequency ?: Frequency.DAILY) }
    var priority by remember { mutableStateOf(initialRoutine?.priority ?: Priority.HIGH) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun openTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                time = String.format("%02d:%02d", hour, minute)
                errorMessage = null
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.45f),
            colors.background
        )
    )

    Scaffold(
        containerColor = colors.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialRoutine == null) {
                            "Création d'une nouvelle routine"
                        } else {
                            "Modifier la routine"
                        },
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
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onSurface,
                    navigationIconContentColor = colors.onSurface
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.routines_banner),
                    contentDescription = "Bannière routines",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            item {
                Surface(
                    color = colors.surface.copy(alpha = 0.96f),
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 2.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Entrez les informations",
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Catégorie : $category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                errorMessage = null
                            },
                            label = { Text("Nom de la routine") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = errorMessage != null && title.isBlank(),
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                description = it
                            },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = time,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Heure") },
                            trailingIcon = {
                                IconButton(onClick = { openTimePicker() }) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "Choisir l'heure",
                                        tint = colors.primary
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openTimePicker() },
                            isError = errorMessage != null && time.isBlank(),
                            shape = RoundedCornerShape(18.dp)
                        )

                        Text(
                            text = "Fréquence",
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )

                        FrequencyRadio(
                            label = "Tous les jours",
                            selected = frequency == Frequency.DAILY,
                            onSelect = {
                                frequency = Frequency.DAILY
                                errorMessage = null
                            }
                        )

                        FrequencyRadio(
                            label = "Certains jours",
                            selected = frequency == Frequency.SOME_DAYS,
                            onSelect = {
                                frequency = Frequency.SOME_DAYS
                                errorMessage = null
                            }
                        )

                        FrequencyRadio(
                            label = "Une seule fois",
                            selected = frequency == Frequency.ONCE,
                            onSelect = {
                                frequency = Frequency.ONCE
                                errorMessage = null
                            }
                        )

                        Text(
                            text = "Priorité",
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onSurface
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PriorityRadio(
                                label = "Faible",
                                selected = priority == Priority.LOW,
                                onSelect = { priority = Priority.LOW }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            PriorityRadio(
                                label = "Moyenne",
                                selected = priority == Priority.MEDIUM,
                                onSelect = { priority = Priority.MEDIUM }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            PriorityRadio(
                                label = "Élevée",
                                selected = priority == Priority.HIGH,
                                onSelect = { priority = Priority.HIGH }
                            )
                        }

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = colors.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            onClick = {
                                if (title.isBlank() || time.isBlank()) {
                                    errorMessage = "Veuillez remplir le nom et l'heure."
                                    return@Button
                                }

                                val routineId = initialRoutine?.id ?: UUID.randomUUID().toString()

                                onSave(
                                    Routine(
                                        id = routineId,
                                        title = title.trim(),
                                        description = description.trim(),
                                        time = time,
                                        category = category,
                                        frequency = frequency,
                                        priority = priority
                                    )
                                )
                            },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Text(
                                text = if (initialRoutine == null) {
                                    "Sauvegarder"
                                } else {
                                    "Mettre à jour"
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FrequencyRadio(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PriorityRadio(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}