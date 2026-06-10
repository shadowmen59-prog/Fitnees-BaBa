package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val routineName: String,
    val genderCategory: String, // "Male" or "Female"
    val routineType: String, // "Strength", "Cardio", "Recovery"
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val completedSummary: String, // e.g., "Bench Press, Overhead Press, Incline Dumbbell Fly"
    val notes: String = ""
)
