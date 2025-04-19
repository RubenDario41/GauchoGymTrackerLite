package com.example.gauchoGymTrackerLite

import android.app.Application
import androidx.room.Room
import com.example.gauchoGymTrackerLite.data.AppDatabase
import com.example.gauchoGymTrackerLite.repository.WorkoutRepository

class MyApplication : Application() {

    // Base de datos Room
    lateinit var database: AppDatabase
        private set

    // Repositorio para gestionar datos
    lateinit var workoutRepository: WorkoutRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos Room
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "gaucho_gym_tracker_db"
        ).build()

        // Inicializar el repositorio
        workoutRepository = WorkoutRepository(database.workoutDao())
    }
}