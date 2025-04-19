package com.example.gauchoGymTrackerLite.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gauchoGymTrackerLite.data.dao.WorkoutDao
import com.example.gauchoGymTrackerLite.data.entity.WorkoutEntity

@Database(entities = [WorkoutEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}