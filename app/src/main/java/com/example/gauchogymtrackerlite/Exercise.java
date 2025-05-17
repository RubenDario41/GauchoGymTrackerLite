package com.example.gauchogymtrackerlite;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.Locale;

@Entity(tableName = "exercises",
        foreignKeys = @ForeignKey(entity = Routine.class,
                                  parentColumns = "routineId",
                                  childColumns = "routineOwnerId",
                                  onDelete = ForeignKey.CASCADE), // Si borras rutina, borra ejercicios
        indices = {@Index(value = {"routineOwnerId"})}) // Index para búsquedas por rutina
public class Exercise implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long exerciseId;

    @ColumnInfo(name = "routineOwnerId") // Nombre de columna para la clave foránea
    public long routineOwnerId;

    @ColumnInfo(name = "exercise_name")
    public String name;

    @ColumnInfo(name = "exercise_sets")
    public int sets;

    @ColumnInfo(name = "exercise_reps")
    public String reps;

    @ColumnInfo(name = "exercise_weight")
    public double weight;

    @ColumnInfo(name = "exercise_notes")
    public String notes;

    // Constructor vacío para Room
    public Exercise() {}

    // Constructor principal (ahora incluye routineOwnerId)
    public Exercise(long routineOwnerId, String name, int sets, String reps, double weight, String notes) {
        this.routineOwnerId = routineOwnerId;
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.notes = (notes != null) ? notes : "";
    }

    // Getters y Setters (simplificados - genera los que necesites)
    public long getExerciseId() { return exerciseId; }
    public long getRoutineOwnerId() { return routineOwnerId; }
    public String getName() { return name; }
    public int getSets() { return sets; }
    public String getReps() { return reps; }
    public double getWeight() { return weight; }
    public String getNotes() { return notes; }

    public void setRoutineOwnerId(long routineOwnerId) { this.routineOwnerId = routineOwnerId; }
    public void setName(String name) { this.name = name; }
    public void setSets(int sets) { this.sets = sets; }
    public void setReps(String reps) { this.reps = reps; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setNotes(String notes) { this.notes = notes != null ? notes : ""; }


    @Override
    public String toString() {
        String weightStr = (weight > 0) ? " - " + String.format(Locale.getDefault(), (weight == (long) weight) ? "%dkg" : "%.1fkg", weight) : "";
        String notesStr = (notes != null && !notes.isEmpty()) ? " (" + notes + ")" : "";
        return name + " (" + sets + "x" + reps + weightStr + ")" + notesStr;
    }
}