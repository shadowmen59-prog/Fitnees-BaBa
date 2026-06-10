package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PresetPlans
import com.example.data.model.WorkoutPlan
import com.example.ui.FitnessViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: FitnessViewModel,
    onNavigateToActiveWorkout: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    var selectedGender by remember(profile.gender) { mutableStateOf(profile.gender) }
    var selectedCategoryTab by remember { mutableStateOf(0) } // 0: Strength, 1: Cardio, 2: Recovery
    var searchQueries by remember { mutableStateOf("") }

    val categories = listOf("Strength", "Cardio", "Recovery")
    val activeWorkout = viewModel.activePlan

    // Filter plans based on Gender / Tab / Search queries
    val filteredPlans = remember(selectedGender, selectedCategoryTab, searchQueries) {
        val basePlans = PresetPlans.getPlansFor(selectedGender)
        val selectedTypeName = categories[selectedCategoryTab]

        basePlans.filter { workoutPlan ->
            workoutPlan.type.equals(selectedTypeName, ignoreCase = true) &&
                    (searchQueries.isBlank() || 
                     workoutPlan.name.contains(searchQueries, ignoreCase = true) ||
                     workoutPlan.description.contains(searchQueries, ignoreCase = true) ||
                     workoutPlan.exercises.any { it.name.contains(searchQueries, ignoreCase = true) })
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                    )
                )
            )
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
    ) {
        // ATHLETE WELCOME SIGNATURE HEADER
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "WELCOME BACK,",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${profile.name.uppercase()} 👋",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Track your sets, weights, and daily physical milestones.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // PERSISTENT RESUME BAR (Only shows if there's an active in-progress workout!)
        if (activeWorkout != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToActiveWorkout() }
                        .testTag("resume_workout_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Active workout indicator",
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "WORKOUT IN PROGRESS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = activeWorkout.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigateToActiveWorkout() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Resume", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // GENDER & GATEGORY HORIZONTAL CONTROLS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Gender Selector
                Row(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    ).padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val genders = listOf("Male", "Female")
                    genders.forEach { gender ->
                        val isSelected = selectedGender.lowercase() == gender.lowercase()
                        Box(
                            modifier = Modifier
                                .clickable {
                                    selectedGender = gender
                                    viewModel.selectGender(gender)
                                }
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary 
                                            else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("gender_chip_${gender.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = gender,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary 
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Experience Level indicator badge
                SuggestionChip(
                    onClick = {},
                    label = { Text("Level: ${profile.experienceLevel}") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                )
            }
        }

        // TABS FOR TRAINING CATEGORY
        item {
            TabRow(
                selectedTabIndex = selectedCategoryTab,
                divider = {},
                containerColor = Color.Transparent
            ) {
                categories.forEachIndexed { index, label ->
                    val isSelected = selectedCategoryTab == index
                    val tabIcon = when (label) {
                        "Strength" -> Icons.Default.FitnessCenter
                        "Cardio" -> Icons.Default.DirectionsRun
                        else -> Icons.Default.SelfImprovement
                    }
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategoryTab = index },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = tabIcon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    )
                }
            }
        }

        // SEARCH BAR
        item {
            OutlinedTextField(
                value = searchQueries,
                onValueChange = { searchQueries = it },
                placeholder = { Text("Search routines (e.g. 'Push', 'Plank', 'Glutes') ...", fontSize = 13.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("workout_search_input"),
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQueries.isNotEmpty()) {
                        IconButton(onClick = { searchQueries = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
        }

        // PLANS GENERATOR FEED
        if (filteredPlans.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search empty state",
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No plans found",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Try refining your search text or switching the gender toggle.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredPlans, key = { it.id }) { plan ->
                WorkoutPlanCard(
                    plan = plan,
                    onStart = {
                        viewModel.startWorkout(plan)
                        onNavigateToActiveWorkout()
                    }
                )
            }
        }
    }
}

@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlan,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("workout_plan_card_${plan.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = plan.type.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "~${plan.estimatedMinutes} mins",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = plan.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plan.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sub exercises indicators
            Text(
                text = "INCLUDED EXERCISES (${plan.exercises.size}):",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
            
            // Render comma-separated exercises
            val exercisesJoined = remember(plan.exercises) {
                plan.exercises.joinToString(", ") { it.name }
            }
            Text(
                text = exercisesJoined,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("start_workout_button_${plan.id}"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Text("Start Workout Session", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
