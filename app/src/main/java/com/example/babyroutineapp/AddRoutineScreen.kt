package com.example.babyroutineapp

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineScreen(
    category: String,
    onSave: (Routine) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var frequency by remember { mutableStateOf(Frequency.DAILY) }
    var priority by remember { mutableStateOf(Priority.HIGH) }

    var error by remember { mutableStateOf<String?>(null) }

    val bg = Brush.verticalGradient(
        listOf(Color(0xFFF3E9FF), Color(0xFFE8FBFF), Color(0xFFF6E9FF))
    )

    fun openTimePicker() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, h, m ->
                time = String.format("%02d:%02d", h, m)
                error = null
            },
            hour,
            minute,
            true
        ).show()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Création d'une nouvelle routine", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.routines_banner),
                contentDescription = "Bannière routines",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(Modifier.height(14.dp))

            Surface(
                color = Color.White.copy(alpha = 0.88f),
                shape = RoundedCornerShape(26.dp),
                tonalElevation = 0.dp,
                shadowElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("Entrez les informations", fontWeight = FontWeight.Bold)

                    Text(
                        text = "Catégorie : $category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            error = null
                        },
                        label = { Text("Nom de la routine") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = error != null && title.isBlank()
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { /* readOnly */ },
                        readOnly = true,
                        label = { Text("Heure") },
                        trailingIcon = {
                            IconButton(onClick = { openTimePicker() }) {
                                Icon(Icons.Default.AccessTime, contentDescription = "Choisir l'heure")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openTimePicker() },
                        isError = error != null && time.isBlank()
                    )


                    Text("Fréquence", fontWeight = FontWeight.SemiBold)
                    FrequencyRadio("Tous les jours", frequency == Frequency.DAILY) { frequency = Frequency.DAILY }
                    FrequencyRadio("Certains jours", frequency == Frequency.SOME_DAYS) { frequency = Frequency.SOME_DAYS }
                    FrequencyRadio("Une seule fois", frequency == Frequency.ONCE) { frequency = Frequency.ONCE }

                    Text("Priorité", fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PriorityRadio("Faible", priority == Priority.LOW) { priority = Priority.LOW }
                        Spacer(Modifier.width(10.dp))
                        PriorityRadio("Moyenne", priority == Priority.MEDIUM) { priority = Priority.MEDIUM }
                        Spacer(Modifier.width(10.dp))
                        PriorityRadio("Élevée", priority == Priority.HIGH) { priority = Priority.HIGH }
                    }

                    if (error != null) {
                        Text(
                            text = error!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            if (title.isBlank() || time.isBlank()) {
                                error = "Veuillez remplir le nom et l'heure."
                                return@Button
                            }

                            onSave(
                                Routine(
                                    id = UUID.randomUUID().toString(),
                                    title = title.trim(),
                                    time = time,
                                    category = category,
                                    frequency = frequency,
                                    priority = priority
                                )
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB9F3C8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Sauvegarder", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun FrequencyRadio(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}

@Composable
private fun PriorityRadio(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}
