package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutPlan
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FitnessViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FitnessRepository

    val userProfile: StateFlow<UserProfile>
    val workoutLogs: StateFlow<List<WorkoutLog>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FitnessRepository(database.userDao(), database.workoutLogDao())

        userProfile = repository.userProfile
            .map { it ?: UserProfile() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserProfile()
            )

        workoutLogs = repository.allLogs
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // Active Workout State
    var activePlan by mutableStateOf<WorkoutPlan?>(null)
        private set

    var currentExerciseIndex by mutableStateOf(0)
        private set

    var timerSeconds by mutableStateOf(0)
        private set

    var isTimerRunning by mutableStateOf(false)
        private set

    // Tracks set checkpoints as Map of "ExerciseName" -> List of Booleans
    var completedSetsMap by mutableStateOf<Map<String, List<Boolean>>>(emptyMap())
        private set

    var workoutNotes by mutableStateOf("")

    private var timerJob: Job? = null

    fun selectGender(gender: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(gender = gender))
        }
    }

    fun updateUserProfile(name: String, weight: Float, targetWeight: Float, level: String, goal: String) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(
                current.copy(
                    name = name,
                    weightKg = weight,
                    targetWeightKg = targetWeight,
                    experienceLevel = level,
                    primaryGoal = goal
                )
            )
        }
    }

    fun startWorkout(plan: WorkoutPlan) {
        activePlan = plan
        currentExerciseIndex = 0
        timerSeconds = 0
        workoutNotes = ""
        isTimerRunning = true
        
        val tempMap = mutableMapOf<String, List<Boolean>>()
        plan.exercises.forEach { exercise ->
            val setsCount = extractSetCount(exercise.repsDescription)
            tempMap[exercise.name] = List(setsCount) { false }
        }
        completedSetsMap = tempMap

        startTimer()
    }

    private fun extractSetCount(repsDescription: String): Int {
        // Look for sets count, default to 3 if not found
        val match = Regex("""(\d+)\s*set""").find(repsDescription.lowercase())
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 3
    }

    fun toggleSetComplete(exerciseName: String, setIndex: Int) {
        val currentSets = completedSetsMap[exerciseName] ?: return
        if (setIndex in currentSets.indices) {
            val updated = currentSets.toMutableList()
            updated[setIndex] = !updated[setIndex]
            completedSetsMap = completedSetsMap.toMutableMap().apply {
                put(exerciseName, updated)
            }
        }
    }

    fun setExerciseIndex(index: Int) {
        if (activePlan != null && index in 0 until (activePlan?.exercises?.size ?: 0)) {
            currentExerciseIndex = index
        }
    }

    fun toggleTimer() {
        if (isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        isTimerRunning = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                timerSeconds++
            }
        }
    }

    private fun pauseTimer() {
        isTimerRunning = false
        timerJob?.cancel()
    }

    fun finishWorkout() {
        val plan = activePlan ?: return
        pauseTimer()
        viewModelScope.launch {
            val durationMin = if (timerSeconds > 0) (timerSeconds / 60) + 1 else 1
            
            val summaryList = mutableListOf<String>()
            plan.exercises.forEach { exercise ->
                val sets = completedSetsMap[exercise.name] ?: emptyList()
                val completedCount = sets.count { it }
                if (completedCount > 0) {
                    summaryList.add("${exercise.name} ($completedCount/${sets.size} sets)")
                }
            }
            val completedSummary = if (summaryList.isNotEmpty()) {
                summaryList.joinToString(", ")
            } else {
                "Fully completed session"
            }

            val calorieFactor = when (plan.type) {
                "Strength" -> 7
                "Cardio" -> 10
                "Recovery" -> 3
                else -> 5
            }
            val calories = durationMin * calorieFactor

            repository.insertWorkoutLog(
                WorkoutLog(
                    routineName = plan.name,
                    genderCategory = plan.gender,
                    routineType = plan.type,
                    durationMinutes = durationMin,
                    caloriesBurned = calories,
                    completedSummary = completedSummary,
                    notes = workoutNotes
                )
            )

            activePlan = null
            timerSeconds = 0
            completedSetsMap = emptyMap()
            workoutNotes = ""
        }
    }

    fun cancelWorkout() {
        pauseTimer()
        activePlan = null
        timerSeconds = 0
        completedSetsMap = emptyMap()
        workoutNotes = ""
    }

    fun deleteLog(id: Long) {
        viewModelScope.launch {
            repository.deleteWorkoutLog(id)
        }
    }

    fun clearLogHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
