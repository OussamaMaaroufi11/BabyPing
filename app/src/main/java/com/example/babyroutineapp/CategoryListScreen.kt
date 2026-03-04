package com.example.babyroutineapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.babyroutineapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    categoryTitle: String,
    routines: List<Routine>,

    doneIdsToday: List<String>,
    onToggleDone: (String) -> Unit,

    onBack: () -> Unit,
    onQuit: () -> Unit,
    onAdd: () -> Unit,

    onEdit: (String) -> Unit,     // PP2
    onDelete: (String) -> Unit,   // PP2

    frequencyTextProvider: (Routine) -> String
) {
    val bg = Brush.verticalGradient(
        listOf(Color(0xFFF3E9FF), Color(0xFFE8FBFF), Color(0xFFF6E9FF))
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = Color(0xFFB9F3C8)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kids_banner),
                    contentDescription = "Banner",
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                )

                Text(
                    text = categoryTitle.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Routines enregistrées",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            if (routines.isEmpty()) {
                Text("Aucune routine pour cette catégorie.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    routines.forEach { routine ->
                        val isDone = doneIdsToday.contains(routine.id)

                        RoutineItemCard(
                            title = routine.title,
                            description = routine.description,
                            time = routine.time,
                            frequencyText = frequencyTextProvider(routine),
                            done = isDone,
                            onToggleDone = { onToggleDone(routine.id) },
                            onEdit = { onEdit(routine.id) },
                            onDelete = { onDelete(routine.id) }
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                onClick = onQuit,
                shape = RoundedCornerShape(28.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text("QUITTER", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RoutineItemCard(
    title: String,
    description: String,
    time: String,
    frequencyText: String,
    done: Boolean,
    onToggleDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        shape = shape,
        color = Color.White.copy(alpha = 0.88f),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x55000000), shape)
    ) {
        Column(Modifier.padding(14.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Medium)
                    if (description.isNotBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }

                Text(text = time, fontWeight = FontWeight.Bold)

                Spacer(Modifier.width(10.dp))
                Icon(Icons.Default.Alarm, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))

                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onToggleDone) {
                    Icon(
                        imageVector = if (done) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Terminé",
                        tint = if (done) Color(0xFF2E7D32) else Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = frequencyText,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFD32F2F))
                }
            }
        }
    }
}