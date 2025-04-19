package com.example.gauchoGymTrackerLite.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gauchoGymTrackerLite.data.entity.WorkoutEntity

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>>

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)
}