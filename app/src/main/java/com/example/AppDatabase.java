package com.example.gauchogymtrackerlite;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Imports para ExecutorService
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Routine.class, Exercise.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RoutineDao routineDao();
    public abstract ExerciseDao exerciseDao();

    private static volatile AppDatabase INSTANCE;

    // Número de hilos para operaciones de BD
    private static final int NUMBER_OF_THREADS = 4;
    // Executor para ejecutar operaciones de BD en segundo plano
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "gaucho_gym_database")
                            .fallbackToDestructiveMigration()
                            // Quitar allowMainThreadQueries si ya no es necesario
                            // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}