package com.example.gauchoGymTrackerLite.repository

import androidx.lifecycle.LiveData
import com.example.gauchoGymTrackerLite.data.dao.WorkoutDao
import com.example.gauchoGymTrackerLite.data.entity.WorkoutEntity

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    // Obtener todos los entrenamientos
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>> {
        return workoutDao.getAllWorkouts()
    }

    // Insertar un nuevo entrenamiento
    suspend fun insertWorkout(workout: WorkoutEntity) {
        workoutDao.insertWorkout(workout)
    }

    // Eliminar un entrenamiento
    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }
}