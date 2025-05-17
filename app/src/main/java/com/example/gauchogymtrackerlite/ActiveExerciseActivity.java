package com.example.gauchogymtrackerlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Si decides añadir una Toolbar

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Para OnClickListener si lo necesitas
import android.widget.Button; // Para botones
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList; // Para inicializar lista si es necesario
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class ActiveExerciseActivity extends AppCompatActivity {

    private static final String TAG = "ActiveExerciseActivity";

    // Vistas de UI
    private TextView exerciseNameTextView;
    private TextView setIndicatorTextView;
    private TextView repTargetTextView;
    private TextView weightTextView;
    private TextView repCountTextView;
    private TextView restTimerTextView;
    private Button nextSetButton;
    // private Button finishExerciseButton; // Si tienes este botón

    // Datos y BD
    private long currentRoutineId = -1L;
    private Routine currentRoutine; // La rutina completa que se está ejecutando
    private List<Exercise> exercisesInRoutine; // Lista de ejercicios de la rutina actual
    private int currentExerciseIndex = -1; // Índice del ejercicio actual en la lista
    private int currentSetNumber = 1; // Número de la serie actual para el ejercicio en curso

    private RoutineDao routineDao;
    private ExerciseDao exerciseDao;
    private ExecutorService databaseExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_exercise);
        Log.d(TAG, "onCreate: Iniciando...");

        // Inicializar DAOs y Executor
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        routineDao = db.routineDao();
        exerciseDao = db.exerciseDao();
        databaseExecutor = AppDatabase.databaseWriteExecutor;

        // Encontrar las vistas
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        setIndicatorTextView = findViewById(R.id.setIndicatorTextView);
        repTargetTextView = findViewById(R.id.repTargetTextView);
        weightTextView = findViewById(R.id.weightTextView);
        repCountTextView = findViewById(R.id.repCountTextView);
        restTimerTextView = findViewById(R.id.restTimerTextView);
        nextSetButton = findViewById(R.id.nextSetButton);
        // finishExerciseButton = findViewById(R.id.finishExerciseButton);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ROUTINE_ID")) {
            currentRoutineId = intent.getLongExtra("ROUTINE_ID", -1L);
            Log.d(TAG, "Recibido ROUTINE_ID: " + currentRoutineId);

            if (currentRoutineId == -1L) {
                handleInvalidRoutineId();
                return;
            }
            // Cargar la rutina y sus ejercicios
            loadRoutineData(currentRoutineId);

        } else {
            Log.e(TAG, "No se recibió ROUTINE_ID. Finalizando Activity.");
            Toast.makeText(this, "Error: No se especificó rutina para iniciar.", Toast.LENGTH_LONG).show();
            finish();
        }

        // Configurar listeners para botones (ejemplo básico)
        if (nextSetButton != null) {
            nextSetButton.setOnClickListener(v -> {
                // TODO: Implementar lógica para avanzar serie/ejercicio
                Toast.makeText(ActiveExerciseActivity.this, "Siguiente Serie (TODO)", Toast.LENGTH_SHORT).show();
                // advanceToNextSetOrExercise();
            });
        }
    }

    private void handleInvalidRoutineId() {
        Log.e(TAG, "ROUTINE_ID es inválido. Finalizando Activity.");
        Toast.makeText(this, "Error: ID de rutina inválido.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void loadRoutineData(long routineId) {
        Log.d(TAG, "loadRoutineData: Cargando datos para rutina ID: " + routineId);
        if (databaseExecutor == null || routineDao == null || exerciseDao == null) {
            Log.e(TAG, "loadRoutineData: DAOs o Executor no inicializados.");
            Toast.makeText(this, "Error interno al cargar datos.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toast.makeText(this, "Cargando rutina...", Toast.LENGTH_SHORT).show();
        databaseExecutor.execute(() -> {
            try {
                currentRoutine = routineDao.getRoutineById(routineId);
                if (currentRoutine != null) {
                    exercisesInRoutine = exerciseDao.getExercisesForRoutine(routineId);
                    Log.d(TAG, "Rutina '" + currentRoutine.getName() + "' cargada con " + (exercisesInRoutine != null ? exercisesInRoutine.size() : 0) + " ejercicios.");

                    runOnUiThread(() -> {
                        if (exercisesInRoutine != null && !exercisesInRoutine.isEmpty()) {
                            currentExerciseIndex = 0; // Empezar con el primer ejercicio
                            currentSetNumber = 1;    // Empezar con la primera serie
                            updateUiForCurrentExercise();
                        } else {
                            Log.w(TAG, "La rutina no tiene ejercicios o falló la carga de ejercicios.");
                            Toast.makeText(ActiveExerciseActivity.this, "La rutina no tiene ejercicios.", Toast.LENGTH_LONG).show();
                            // Podrías cerrar la activity o manejarlo de otra forma
                            // finish();
                            if (exerciseNameTextView!=null) exerciseNameTextView.setText("Rutina sin ejercicios");
                        }
                    });
                } else {
                    Log.e(TAG, "No se encontró la rutina con ID: " + routineId);
                    runOnUiThread(() -> {
                        Toast.makeText(ActiveExerciseActivity.this, "Rutina no encontrada.", Toast.LENGTH_LONG).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Error cargando datos de rutina desde BD", e);
                runOnUiThread(() -> {
                    Toast.makeText(ActiveExerciseActivity.this, "Error al cargar datos de rutina.", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void updateUiForCurrentExercise() {
        if (exercisesInRoutine == null || currentExerciseIndex < 0 || currentExerciseIndex >= exercisesInRoutine.size()) {
            Log.w(TAG, "updateUiForCurrentExercise: Índice de ejercicio inválido o lista vacía.");
            // TODO: Manejar fin de rutina
            if (exerciseNameTextView!=null) exerciseNameTextView.setText("Fin de Rutina");
            setIndicatorTextView.setText("");
            repTargetTextView.setText("");
            weightTextView.setText("");
            repCountTextView.setText("");
            restTimerTextView.setText("");
            if(nextSetButton!=null) nextSetButton.setText("Finalizar Rutina");
            return;
        }

        Exercise exercise = exercisesInRoutine.get(currentExerciseIndex);
        Log.d(TAG, "Mostrando ejercicio: " + exercise.getName() + ", Serie: " + currentSetNumber);

        if (exerciseNameTextView != null) exerciseNameTextView.setText(exercise.getName());
        if (setIndicatorTextView != null) setIndicatorTextView.setText(String.format(Locale.getDefault(), "Serie: %d / %d", currentSetNumber, exercise.getSets()));
        if (repTargetTextView != null) repTargetTextView.setText(String.format(Locale.getDefault(), "Objetivo: %s reps", exercise.getReps()));
        if (weightTextView != null) {
            if (exercise.getWeight() > 0) {
                if (exercise.getWeight() == (long) exercise.getWeight()) {
                    weightTextView.setText(String.format(Locale.getDefault(), "Peso: %d kg", (long)exercise.getWeight()));
                } else {
                    weightTextView.setText(String.format(Locale.getDefault(), "Peso: %.1f kg", exercise.getWeight()));
                }
            } else {
                weightTextView.setText("Peso: Corporal");
            }
        }
        if (repCountTextView != null) repCountTextView.setText("0"); // Resetear contador de reps
        if (restTimerTextView != null) restTimerTextView.setText("Descanso: --:--"); // Placeholder

        if(nextSetButton!=null) nextSetButton.setText("Siguiente Serie");
    }

    // TODO: Implementar advanceToNextSetOrExercise()
    // private void advanceToNextSetOrExercise() { ... }
}