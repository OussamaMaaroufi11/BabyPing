package com.app.babyroutine.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.babyroutine.R
import com.app.babyroutine.model.Routine

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
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    frequencyTextProvider: (Routine) -> String
) {
    val colors = MaterialTheme.colorScheme

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surface,
            colors.surfaceVariant.copy(alpha = 0.4f),
            colors.background
        )
    )

    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        containerColor = colors.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = colors.secondary,
                contentColor = Color.Black,
                modifier = Modifier
                    .padding(end = 10.dp, bottom = 60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(18.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kids_banner),
                            contentDescription = "Bannière catégorie",
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            colors.scrim.copy(alpha = 0.28f),
                                            colors.scrim.copy(alpha = 0.60f)
                                        )
                                    )
                                )
                        )

                        Text(
                            text = categoryTitle.uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                item {
                    Text(
                        text = "Routines enregistrées",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                }

                if (routines.isEmpty()) {
                    item {
                        Text(
                            text = "Aucune routine pour cette catégorie.",
                            color = colors.onSurfaceVariant
                        )
                    }
                } else {
                    items(routines, key = { it.id }) { routine ->
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

            OutlinedButton(
                onClick = onQuit,
                shape = RoundedCornerShape(size = 28.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = "QUITTER",
                    fontWeight = FontWeight.SemiBold
                )
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
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(18.dp)

    val doneBackground = colors.secondary.copy(alpha = 0.20f)
    val doneBorder = colors.secondary.copy(alpha = 0.65f)

    val cardColor = animateColorAsState(
        targetValue = if (done) doneBackground else colors.surface,
        label = "cardColor"
    )

    val borderColor = animateColorAsState(
        targetValue = if (done) doneBorder else colors.outline.copy(alpha = 0.4f),
        label = "borderColor"
    )

    val mainTextColor = animateColorAsState(
        targetValue = if (done) colors.secondary else colors.onSurface,
        label = "mainTextColor"
    )

    val secondaryTextColor = animateColorAsState(
        targetValue = if (done) colors.onSurface else colors.onSurfaceVariant,
        label = "secondaryTextColor"
    )

    val scale = animateFloatAsState(
        targetValue = if (done) 1.15f else 1f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "checkScale"
    )

    Surface(
        shape = shape,
        color = cardColor.value,
        shadowElevation = if (done) 12.dp else 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor.value, shape)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    color = mainTextColor.value
                )

                Text(
                    text = time,
                    fontWeight = FontWeight.Bold,
                    color = mainTextColor.value
                )

                Spacer(modifier = Modifier.size(8.dp))

                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null,
                    tint = mainTextColor.value
                )

                Spacer(modifier = Modifier.size(6.dp))

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = mainTextColor.value
                )

                IconButton(onClick = onToggleDone) {
                    Icon(
                        imageVector = if (done) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.RadioButtonUnchecked
                        },
                        contentDescription = null,
                        tint = if (done) colors.primary else colors.onSurfaceVariant,
                        modifier = Modifier.scale(scale.value)
                    )
                }
            }

            if (description.isNotBlank()) {
                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = description,
                    color = secondaryTextColor.value,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.size(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (done) "Terminée • $frequencyText" else frequencyText,
                    modifier = Modifier.weight(1f),
                    color = secondaryTextColor.value,
                    style = MaterialTheme.typography.bodySmall
                )

                IconButton(
                    onClick = onEdit,
                    enabled = !done
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = if (done) colors.secondary else colors.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDelete,
                    enabled = !done
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = if (done) colors.secondary else colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}