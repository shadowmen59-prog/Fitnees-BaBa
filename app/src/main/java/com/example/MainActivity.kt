package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FitnessViewModel
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WorkoutActiveScreen
import com.example.ui.theme.MyApplicationTheme

enum class FitnessTab {
    HOME, HISTORY, PROFILE
}

class MainActivity : ComponentActivity() {
    private val viewModel: FitnessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val activeWorkout = viewModel.activePlan

                if (activeWorkout != null) {
                    WorkoutActiveScreen(
                        viewModel = viewModel,
                        onBack = { /* ViewModel state changes to null to close */ }
                    )
                } else {
                    AppMainLayout(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppMainLayout(viewModel: FitnessViewModel) {
    var activeTab by remember { mutableStateOf(FitnessTab.HOME) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideScreen = maxWidth > 600.dp

        if (isWideScreen) {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    
                    NavigationRailItem(
                        selected = activeTab == FitnessTab.HOME,
                        onClick = { activeTab = FitnessTab.HOME },
                        icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
                        label = { Text("Workouts", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_rail_workouts")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NavigationRailItem(
                        selected = activeTab == FitnessTab.HISTORY,
                        onClick = { activeTab = FitnessTab.HISTORY },
                        icon = { Icon(Icons.Default.History, contentDescription = "History") },
                        label = { Text("History", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_rail_history")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NavigationRailItem(
                        selected = activeTab == FitnessTab.PROFILE,
                        onClick = { activeTab = FitnessTab.PROFILE },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_rail_profile")
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    ScreenContent(activeTab, viewModel)
                }
            }
        } else {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    ) {
                        NavigationBarItem(
                            selected = activeTab == FitnessTab.HOME,
                            onClick = { activeTab = FitnessTab.HOME },
                            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
                            label = { Text("Workouts", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_bar_workouts")
                        )

                        NavigationBarItem(
                            selected = activeTab == FitnessTab.HISTORY,
                            onClick = { activeTab = FitnessTab.HISTORY },
                            icon = { Icon(Icons.Default.History, contentDescription = "History") },
                            label = { Text("History", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_bar_history")
                        )

                        NavigationBarItem(
                            selected = activeTab == FitnessTab.PROFILE,
                            onClick = { activeTab = FitnessTab.PROFILE },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile", fontSize = 11.sp) },
                            modifier = Modifier.testTag("nav_bar_profile")
                        )
                    }
                },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent(activeTab, viewModel)
                }
            }
        }
    }
}

@Composable
fun ScreenContent(tab: FitnessTab, viewModel: FitnessViewModel) {
    Crossfade(targetState = tab, label = "ScreenTransition") { currentTab ->
        when (currentTab) {
            FitnessTab.HOME -> HomeScreen(
                viewModel = viewModel,
                onNavigateToActiveWorkout = { /* Navigation state-driven, handled in AppMainLayout */ }
            )
            FitnessTab.HISTORY -> HistoryScreen(viewModel = viewModel)
            FitnessTab.PROFILE -> ProfileScreen(viewModel = viewModel)
        }
    }
}
