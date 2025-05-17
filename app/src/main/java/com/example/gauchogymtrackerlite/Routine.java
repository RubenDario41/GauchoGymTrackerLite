package com.example.gauchogymtrackerlite;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "routines")
public class Routine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long routineId;

    @ColumnInfo(name = "routine_name")
    public String name;

    @ColumnInfo(name = "routine_description")
    public String description;

    @Ignore
    public List<Exercise> exercises;

    public Routine() {
        this.exercises = new ArrayList<>();
    }

    @Ignore
    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<>();
    }

    @Ignore
    public Routine(String name, String description, List<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.exercises = (exercises != null) ? new ArrayList<>(exercises) : new ArrayList<>();
    }

    // --- GETTER AÑADIDO ---
    public long getRoutineId() {
        return routineId;
    }
    // --- FIN GETTER ---

    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<Exercise> getExercises() { return exercises; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
    public void addExercise(Exercise exercise) {
        if (this.exercises == null) {
            this.exercises = new ArrayList<>();
        }
        this.exercises.add(exercise);
    }

    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", exercisesCount=" + (exercises != null ? exercises.size() : 0) +
                ", routineId=" + routineId + // Añadido ID al toString
                '}';
    }
}