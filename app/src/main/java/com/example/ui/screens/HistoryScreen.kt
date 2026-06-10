package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutLog
import com.example.ui.FitnessViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: FitnessViewModel
) {
    val logs by viewModel.workoutLogs.collectAsState()
    var showClearConfirm by remember { mutableStateOf(false) }

    // Aggregate statistics
    val totalWorkouts = logs.size
    val totalMinutes = logs.sumOf { it.durationMinutes }
    val totalCalories = logs.sumOf { it.caloriesBurned }
    val currentStreak = calculateStreak(logs)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress & History", fontWeight = FontWeight.Bold) },
                actions = {
                    if (logs.isNotEmpty()) {
                        IconButton(onClick = { showClearConfirm = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All Logs", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
            ) {
                // STATS GRID CARD
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "OVERALL METRICS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Column 1: Total
                                StatItem(
                                    label = "Workouts",
                                    value = totalWorkouts.toString(),
                                    icon = Icons.Default.FitnessCenter,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                // Column 2: Minutes
                                StatItem(
                                    label = "Total Mins",
                                    value = "${totalMinutes}m",
                                    icon = Icons.Default.Timer,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                // Column 3: Calories
                                StatItem(
                                    label = "Est. Kcal",
                                    value = totalCalories.toString(),
                                    icon = Icons.Default.LocalFireDepartment,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                // Column 4: Streak
                                StatItem(
                                    label = "Day Streak",
                                    value = "${currentStreak}d",
                                    icon = Icons.Default.Whatshot,
                                    color = Color(0xFFF97316) // Orange streak color
                                )
                            }
                        }
                    }
                }

                // HEADER FOR FEED
                item {
                    Text(
                        text = "WORKOUT LOG HISTORY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                    )
                }

                // FEED LIST
                if (logs.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "Your Gym Log is Empty",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Completed workout plans will automatically be saved here. Go back and tap 'Start Workout'!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    items(logs, key = { it.id }) { log ->
                        WorkoutLogItem(
                            log = log,
                            onDelete = { viewModel.deleteLog(log.id) }
                        )
                    }
                }
            }

            // Dialog for Clear History confirmation
            if (showClearConfirm) {
                AlertDialog(
                    onDismissRequest = { showClearConfirm = false },
                    title = { Text("Wipe Log History?") },
                    text = { Text("This will permanently delete all completed workouts from your device database. This action cannot be undone.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.clearLogHistory()
                                showClearConfirm = false
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Wipe All Logs", fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showClearConfirm = false }) {
                            Text("Keep History")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WorkoutLogItem(
    log: WorkoutLog,
    onDelete: () -> Unit
) {
    val dateText = remember(log.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault())
        sdf.format(Date(log.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("workout_log_item"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Badge(
                            containerColor = if (log.genderCategory.lowercase() == "female") 
                                            Color(0xFFE11D48) else Color(0xFF2563EB),
                            contentColor = Color.White
                        ) {
                            Text(log.genderCategory, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        Badge(
                            containerColor = MaterialTheme.colorScheme.outlineVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(log.routineType, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = log.routineName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dateText,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Log Record",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // Dynamic Metric row inside log card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, size = 14.dp, tint = MaterialTheme.colorScheme.primary)
                    Text("${log.durationMinutes} mins", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, size = 14.dp, tint = MaterialTheme.colorScheme.tertiary)
                    Text("${log.caloriesBurned} kcal", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Exercises completed:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = log.completedSummary,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Custom user notes if present
            if (log.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = "Notes icon",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = log.notes,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
    }
}

private fun calculateStreak(logs: List<WorkoutLog>): Int {
    if (logs.isEmpty()) return 0
    
    val dateIndices = logs.map { log ->
        val cal = Calendar.getInstance()
        cal.timeInMillis = log.timestamp
        // Get unique day index (yyyy * 365 + dayOfYear)
        cal.get(Calendar.YEAR) * 365 + cal.get(Calendar.DAY_OF_YEAR)
    }.distinct().sortedDescending() // newest days first

    if (dateIndices.isEmpty()) return 0

    val todayCal = Calendar.getInstance()
    val todayIndex = todayCal.get(Calendar.YEAR) * 365 + todayCal.get(Calendar.DAY_OF_YEAR)

    // Check if the latest logged day is today or yesterday
    val newestLoggedIndex = dateIndices.first()
    if (newestLoggedIndex != todayIndex && newestLoggedIndex != todayIndex - 1) {
        return 0 // Streak broken
    }

    var streak = 1
    for (i in 0 until dateIndices.lastIndex) {
        // If consecutive elements differ by exactly 1 day, increment streak
        if (dateIndices[i] - dateIndices[i + 1] == 1) {
            streak++
        } else {
            break // streak broken
        }
    }
    return streak
}

@Composable
fun Icon(imageVector: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.size(size),
        tint = tint
    )
}
