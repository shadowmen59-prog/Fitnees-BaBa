package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.WorkoutPlan
import com.example.ui.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutActiveScreen(
    viewModel: FitnessViewModel,
    onBack: () -> Unit
) {
    val activePlan = viewModel.activePlan ?: return
    val currentExerciseIndex = viewModel.currentExerciseIndex
    val timerSeconds = viewModel.timerSeconds
    val isTimerRunning = viewModel.isTimerRunning
    val completedSetsMap = viewModel.completedSetsMap

    // Format timer
    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60
    val timerDisplay = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = activePlan.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "${activePlan.exercises.size} Exercises • ${activePlan.type}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.cancelWorkout(); onBack() },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.finishWorkout()
                    onBack()
                },
                modifier = Modifier.testTag("submit_workout_button"),
                icon = { Icon(Icons.Default.Check, contentDescription = "Check") },
                text = { Text("Complete Session", fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                        )
                    )
                )
        ) {
            // Timer Card with beautiful visual focus
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DURATION ELAPSED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = timerDisplay,
                            fontSize = 36.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleTimer() },
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isTimerRunning) "Pause Timer" else "Resume Timer",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Horizontal step indicators of exercises
            ScrollableTabRow(
                selectedTabIndex = currentExerciseIndex,
                edgePadding = 16.dp,
                divider = {},
                containerColor = Color.Transparent,
                indicator = {}
            ) {
                activePlan.exercises.forEachIndexed { index, exercise ->
                    val isCurrent = index == currentExerciseIndex
                    val setsList = completedSetsMap[exercise.name] ?: emptyList()
                    val setsDone = setsList.count { it }
                    val isAllDone = setsList.isNotEmpty() && setsDone == setsList.size

                    Tab(
                        selected = isCurrent,
                        onClick = { viewModel.setExerciseIndex(index) },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${index + 1}. ${exercise.name}",
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (isAllDone) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else if (setsDone > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ) {
                                        Text("$setsDone/${setsList.size}")
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .background(
                                color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 4.dp)
                    )
                }
            }

            // Exercise Detail card and Sets checklist
            val currentExercise = activePlan.exercises[currentExerciseIndex]
            val setsList = completedSetsMap[currentExercise.name] ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(currentExercise.targetMuscle.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Text(
                                    text = currentExercise.repsDescription,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentExercise.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentExercise.description,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "SET-BY-SET TRACKER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                itemsIndexed(setsList) { setIndex, isComplete ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleSetComplete(currentExercise.name, setIndex) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isComplete) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                                            else MaterialTheme.colorScheme.surface
                        ),
                        border = RowDefaults.borderStroke(isComplete)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "SET ${setIndex + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Target Reps Range",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Checkbox(
                                checked = isComplete,
                                onCheckedChange = { viewModel.toggleSetComplete(currentExercise.name, setIndex) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "SESSION NOTES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = viewModel.workoutNotes,
                        onValueChange = { viewModel.workoutNotes = it },
                        placeholder = { Text("How did this weight feel? (e.g. Inc weight next time!)", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("workout_notes_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

object RowDefaults {
    @Composable
    fun borderStroke(isComplete: Boolean): androidx.compose.foundation.BorderStroke? {
        return if (isComplete) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        }
    }
}
