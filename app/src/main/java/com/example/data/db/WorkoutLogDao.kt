package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WorkoutLog)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)

    @Query("DELETE FROM workout_logs")
    suspend fun clearAllLogs()
}
