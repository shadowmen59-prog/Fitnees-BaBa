package com.example.data.repository

import com.example.data.db.UserDao
import com.example.data.db.WorkoutLogDao
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

class FitnessRepository(
    private val userDao: UserDao,
    private val workoutLogDao: WorkoutLogDao
) {
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
    val allLogs: Flow<List<WorkoutLog>> = workoutLogDao.getAllLogs()

    suspend fun saveUserProfile(profile: UserProfile) {
        userDao.insertUserProfile(profile)
    }

    suspend fun insertWorkoutLog(log: WorkoutLog) {
        workoutLogDao.insertLog(log)
    }

    suspend fun deleteWorkoutLog(id: Long) {
        workoutLogDao.deleteLogById(id)
    }

    suspend fun clearHistory() {
        workoutLogDao.clearAllLogs()
    }
}
