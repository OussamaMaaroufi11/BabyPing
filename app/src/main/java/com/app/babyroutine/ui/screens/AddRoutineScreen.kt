package com.app.babyroutine.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.app.babyroutine.domain.RoutineValidator
import com.app.babyroutine.model.Frequency
import com.app.babyroutine.model.Priority
import com.app.babyroutine.model.Routine
import com.app.babyroutine.model.RoutineLocation
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineScreen(
    category: String,
    initialRoutine: Routine?,
    selectedLocation: RoutineLocation?,
    onPickLocation: () -> Unit,
    onSave: (Routine) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val categories = listOf("Quotidiens", "Programmes", "Santé", "Activités", "Courses")

    var title by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf(initialRoutine?.title ?: "")
    }
    var description by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf(initialRoutine?.description ?: "")
    }
    var selectedCategory by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf(initialRoutine?.category ?: category)
    }
    var time by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf(initialRoutine?.time ?: "")
    }
    var frequencyName by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf((initialRoutine?.frequency ?: Frequency.DAILY).name)
    }
    var priorityName by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf((initialRoutine?.priority ?: Priority.HIGH).name)
    }
    var errorMessage by rememberSaveable(initialRoutine?.id, category) {
        mutableStateOf<String?>(null)
    }
    var categoryExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val frequency = Frequency.valueOf(frequencyName)
    val priority = Priority.valueOf(priorityName)

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
            colors.surfaceVariant.copy(alpha = 0.35f),
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
                        text = if (initialRoutine == null) "Créer une routine" else "Modifier la routine",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = colors.onSurface
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
                .imePadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.routines_banner),
                    contentDescription = "Bannière routines",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(155.dp)
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Entrez les informations",
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            style = MaterialTheme.typography.titleLarge
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                errorMessage = null
                            },
                            label = { Text("Nom de la routine") },
                            placeholder = { Text("Ex. : Donner le biberon") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = errorMessage != null && title.isBlank(),
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            placeholder = { Text("Ajoutez quelques détails...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(18.dp)
                        )

                        Column {
                            Text(
                                text = "Catégorie",
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onSurface,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Box {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Choisir une catégorie") },
                                    trailingIcon = {
                                        IconButton(onClick = { categoryExpanded = true }) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Choisir une catégorie",
                                                tint = colors.primary
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { categoryExpanded = true },
                                    shape = RoundedCornerShape(18.dp)
                                )

                                DropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false }
                                ) {
                                    categories.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                selectedCategory = item
                                                categoryExpanded = false
                                                errorMessage = null
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column {
                            Text(
                                text = "Horaire",
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onSurface,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = time,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("--:--") },
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
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Périodicité",
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onSurface
                            )

                            FrequencyRadio(
                                label = "Tous les jours",
                                selected = frequency == Frequency.DAILY,
                                onSelect = {
                                    frequencyName = Frequency.DAILY.name
                                    errorMessage = null
                                }
                            )

                            FrequencyRadio(
                                label = "Certains jours",
                                selected = frequency == Frequency.SOME_DAYS,
                                onSelect = {
                                    frequencyName = Frequency.SOME_DAYS.name
                                    errorMessage = null
                                }
                            )

                            FrequencyRadio(
                                label = "Une seule fois",
                                selected = frequency == Frequency.ONCE,
                                onSelect = {
                                    frequencyName = Frequency.ONCE.name
                                    errorMessage = null
                                }
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Priorité",
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onSurface
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PriorityRadio(
                                    label = "Faible",
                                    selected = priority == Priority.LOW,
                                    onSelect = { priorityName = Priority.LOW.name }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                PriorityRadio(
                                    label = "Moyenne",
                                    selected = priority == Priority.MEDIUM,
                                    onSelect = { priorityName = Priority.MEDIUM.name }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                PriorityRadio(
                                    label = "Élevée",
                                    selected = priority == Priority.HIGH,
                                    onSelect = { priorityName = Priority.HIGH.name }
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Lieu de déclenchement",
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Surface(
                                color = if (initialRoutine != null || selectedLocation != null) {
                                    colors.secondaryContainer.copy(alpha = 0.55f)
                                } else {
                                    colors.surfaceVariant.copy(alpha = 0.45f)
                                },
                                shape = RoundedCornerShape(22.dp),
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = colors.outline.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(22.dp)
                                    )
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = colors.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = selectedLocation?.displayName ?: "Aucun lieu sélectionné",
                                            fontWeight = FontWeight.SemiBold,
                                            color = colors.onSurface
                                        )
                                    }

                                    if (selectedLocation != null) {
                                        Text(
                                            text = "Latitude : %.5f".format(selectedLocation.latitude),
                                            color = colors.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Longitude : %.5f".format(selectedLocation.longitude),
                                            color = colors.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Rayon : ${selectedLocation.radius.toInt()} m",
                                            color = colors.onSurfaceVariant
                                        )
                                    } else {
                                        Text(
                                            text = "Choisissez un lieu sur la carte pour activer le déclenchement contextuel.",
                                            color = colors.onSurfaceVariant
                                        )
                                    }

                                    Button(
                                        onClick = onPickLocation,
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (selectedLocation == null) {
                                                "Choisir un lieu"
                                            } else {
                                                "Modifier le lieu"
                                            }
                                        )
                                    }
                                }
                            }
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
                                val routineId = initialRoutine?.id ?: UUID.randomUUID().toString()

                                val routineToValidate = Routine(
                                    id = routineId,
                                    title = title.trim(),
                                    description = description.trim(),
                                    time = time,
                                    category = selectedCategory,
                                    frequency = frequency,
                                    priority = priority,
                                    latitude = selectedLocation?.latitude ?: initialRoutine?.latitude,
                                    longitude = selectedLocation?.longitude ?: initialRoutine?.longitude,
                                    radius = selectedLocation?.radius ?: initialRoutine?.radius ?: 100f,
                                    locationName = selectedLocation?.locationName ?: initialRoutine?.locationName,
                                    notificationsEnabled = initialRoutine?.notificationsEnabled ?: true
                                )

                                val validation = RoutineValidator.validateForSave(routineToValidate)

                                if (!validation.isValid) {
                                    errorMessage = validation.message
                                    return@Button
                                }

                                errorMessage = null
                                onSave(routineToValidate)
                            },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9EE6B1),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(
                                text = "Sauvegarder",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (initialRoutine != null) {
                            Button(
                                onClick = onBack,
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE98297),
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text(
                                    text = "Abandonner",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
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