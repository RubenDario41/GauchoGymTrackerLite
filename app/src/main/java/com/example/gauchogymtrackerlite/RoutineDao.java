package com.example.gauchogymtrackerlite;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoutineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoutine(Routine routine); // Devuelve el nuevo ID

    @Update
    void updateRoutine(Routine routine);

    @Delete
    void deleteRoutine(Routine routine);

    @Query("DELETE FROM routines WHERE routineId = :routineId")
    void deleteRoutineById(long routineId);

    @Query("SELECT * FROM routines ORDER BY routine_name ASC")
    List<Routine> getAllRoutines(); // Por ahora, síncrono

    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    Routine getRoutineById(long routineId); // Por ahora, síncrono

    @Query("DELETE FROM routines")
    void deleteAllRoutines(); // Opcional
}