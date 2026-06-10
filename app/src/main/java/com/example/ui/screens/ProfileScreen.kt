package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FitnessViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: FitnessViewModel
) {
    val profile by viewModel.userProfile.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Form states
    var nameState by remember(profile.name) { mutableStateOf(profile.name) }
    var weightState by remember(profile.weightKg) { mutableStateOf(profile.weightKg.toString()) }
    var targetWeightState by remember(profile.targetWeightKg) { mutableStateOf(profile.targetWeightKg.toString()) }
    var levelState by remember(profile.experienceLevel) { mutableStateOf(profile.experienceLevel) }
    var goalState by remember(profile.primaryGoal) { mutableStateOf(profile.primaryGoal) }

    val currentWeight = weightState.toFloatOrNull() ?: 0.0f
    val targetWeight = targetWeightState.toFloatOrNull() ?: 0.0f
    val weightDiff = targetWeight - currentWeight

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = {
                            val finalW = weightState.toFloatOrNull() ?: profile.weightKg
                            val finalT = targetWeightState.toFloatOrNull() ?: profile.targetWeightKg
                            viewModel.updateUserProfile(
                                name = nameState,
                                weight = finalW,
                                targetWeight = finalT,
                                level = levelState,
                                goal = goalState
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar("Profile updated successfully!")
                            }
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("save_profile_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp)
        ) {
            // ATHLETE IDENTITY BANNER CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsRun,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = if (nameState.isNotBlank()) nameState else "Athlete",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Training Level: $levelState • Goal: $goalState",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // SECTION 1: PHYSICAL ATTRIBUTES
            item {
                Text(
                    text = "ATHLETE PROFILE INFORMATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = nameState,
                            onValueChange = { nameState = it },
                            label = { Text("Display Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = weightState,
                                onValueChange = { weightState = it },
                                label = { Text("Current Weight (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("profile_current_weight_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = targetWeightState,
                                onValueChange = { targetWeightState = it },
                                label = { Text("Target Weight (kg)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("profile_target_weight_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        // Weight target delta description
                        if (weightDiff != 0.0f && currentWeight > 0.1f && targetWeight > 0.1f) {
                            val actionText = if (weightDiff > 0) "gain" else "lose"
                            Text(
                                text = "Goal trajectory: You aim to $actionText ${String.format("%.1f", abs(weightDiff))} kg to reach your target.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // SECTION 2: EXPERIENCE METRIC LEVEL
            item {
                Text(
                    text = "TRAINING EXPERIENCE LEVEL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val levels = listOf("Beginner", "Intermediate", "Advanced")
                    levels.forEach { level ->
                        val isSelected = levelState == level
                        Card(
                            onClick = { levelState = level },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = RowDefaults.borderStroke(isSelected)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 3: KEY FITNESS FOCUS
            item {
                Text(
                    text = "PRIMARY FITNESS OBJECTIVE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val goals = listOf("Strength", "Cardio", "Recovery")
                    goals.forEach { goal ->
                        val isSelected = goalState == goal
                        Card(
                            onClick = { goalState = goal },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = RowDefaults.borderStroke(isSelected)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = goal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
