package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Athlete",
    val gender: String = "Male", // "Male" or "Female"
    val heightCm: Int = 175,
    val weightKg: Float = 75.0f,
    val targetWeightKg: Float = 75.0f,
    val experienceLevel: String = "Intermediate", // "Beginner", "Intermediate", "Advanced"
    val primaryGoal: String = "Strength" // "Strength", "Cardio", "Recovery"
)
