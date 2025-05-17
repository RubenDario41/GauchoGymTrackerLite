package com.example.gauchogymtrackerlite; // Reemplaza con tu paquete

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


import java.io.Serializable; // Mantener si Exercise es Serializable y quieres pasar Routine
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

    @Ignore // Room ignorará este campo directamente; se manejará por relación/consulta separada
    public List<Exercise> exercises;

    // Constructor vacío requerido por Room (puede ser protegido si prefieres)
    public Routine() {
        this.exercises = new ArrayList<>();
    }

    // Constructor para crear nuevas rutinas (sin ID)
    @Ignore // Room ignora este constructor
    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<>();
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Exercise> getExercises() { // <-- NUEVO GETTER
        return exercises;
    }

    // --- Setters (Opcionales) ---
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Setter para reemplazar toda la lista (usar con cuidado)
    public void setExercises(List<Exercise> exercises) { // <-- NUEVO SETTER (opcional)
         this.exercises = (exercises != null) ? new ArrayList<>(exercises) : new ArrayList<>();
    }

    // --- Métodos para manipular la lista (Más comunes que setExercises) ---
    public void addExercise(Exercise exercise) { // <-- NUEVO MÉTODO
        if (this.exercises != null && exercise != null) {
            this.exercises.add(exercise);
        }
    }

    public void removeExercise(int position) { // <-- NUEVO MÉTODO (opcional)
        if (this.exercises != null && position >= 0 && position < this.exercises.size()) {
            this.exercises.remove(position);
        }
    }

    public void clearExercises() { // <-- NUEVO MÉTODO (opcional)
        if (this.exercises != null) {
            this.exercises.clear();
        }
    }
    // --- Fin Métodos de Lista ---

    @Override
    public String toString() {
        return "Routine{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", exercisesCount=" + (exercises != null ? exercises.size() : 0) +
                '}';
    }
}