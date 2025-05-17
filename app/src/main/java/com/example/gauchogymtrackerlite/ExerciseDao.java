package com.example.gauchogymtrackerlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExercise(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllExercises(List<Exercise> exercises);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("DELETE FROM exercises WHERE exerciseId = :exerciseId")
    void deleteExerciseById(long exerciseId);

    @Query("DELETE FROM exercises WHERE routineOwnerId = :routineId")
    void deleteExercisesForRoutine(long routineId);

    @Query("SELECT * FROM exercises WHERE routineOwnerId = :routineId ORDER BY exerciseId ASC")
    List<Exercise> getExercisesForRoutine(long routineId); // Por ahora, síncrono

    @Query("DELETE FROM exercises")
    void deleteAllExercises(); // Opcional
}