package com.example.gauchogymtrackerlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class EditRoutineActivity extends AppCompatActivity
        implements AddExerciseDialogFragment.AddExerciseListener, ExerciseAdapter.ExerciseInteractionListener {

    private static final String TAG = "EditRoutineActivity";
    private EditText routineNameEditText;
    private Button saveButton;
    private Button cancelButton;
    private Button addExerciseButton;
    private RecyclerView exercisesRecyclerView;
    private ExerciseAdapter exerciseAdapter;

    private List<Exercise> currentExercises;
    private RoutineDao routineDao;
    private ExerciseDao exerciseDao;
    private long currentRoutineId = -1L;
    private ExecutorService databaseExecutor;
    private boolean isNewRoutine = true; // Para saber el modo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        routineDao = db.routineDao();
        exerciseDao = db.exerciseDao();
        databaseExecutor = AppDatabase.databaseWriteExecutor;

        currentExercises = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.editRoutineToolbar);
        setSupportActionBar(toolbar);

        routineNameEditText = findViewById(R.id.routineNameEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        addExerciseButton = findViewById(R.id.addExerciseButton);
        exercisesRecyclerView = findViewById(R.id.exercisesRecyclerView);

        setupExercisesRecyclerView();
        handleIntent();

        if (cancelButton != null) cancelButton.setOnClickListener(v -> finish());
        if (saveButton != null) saveButton.setOnClickListener(v -> saveRoutineAndExercises());
        if (addExerciseButton != null) addExerciseButton.setOnClickListener(v -> {
             DialogFragment dialog = new AddExerciseDialogFragment();
             dialog.show(getSupportFragmentManager(), "AddExerciseDialog");
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        currentRoutineId = intent.getLongExtra("ROUTINE_ID", -1L);

        if (currentRoutineId != -1L) {
            isNewRoutine = false;
            String routineNameFromIntent = intent.getStringExtra("ROUTINE_NAME");
            if (routineNameEditText != null && routineNameFromIntent != null) {
                 routineNameEditText.setText(routineNameFromIntent);
            }
            // Cargar ejercicios en background
            loadExercisesForRoutine(currentRoutineId);
        } else {
            isNewRoutine = true;
            // Es nueva rutina, configurar UI inicial si es necesario
            if (exerciseAdapter != null) exerciseAdapter.notifyDataSetChanged(); // Asegurar que muestra lista vacía
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isNewRoutine ? "Nueva Rutina" : "Editar Rutina");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    private void setupExercisesRecyclerView() {
         if (exercisesRecyclerView == null) return;
         exerciseAdapter = new ExerciseAdapter(currentExercises, this);
         exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         exercisesRecyclerView.setAdapter(exerciseAdapter);
    }

     private void loadExercisesForRoutine(long routineId) {
         databaseExecutor.execute(() -> {
             try {
                 final List<Exercise> loadedExercises = exerciseDao.getExercisesForRoutine(routineId);
                 runOnUiThread(() -> {
                     if (loadedExercises != null) {
                         currentExercises.clear();
                         currentExercises.addAll(loadedExercises);
                         if (exerciseAdapter != null) {
                             exerciseAdapter.notifyDataSetChanged();
                         }
                         Log.d(TAG, "Ejercicios cargados para rutina ID: " + routineId + ", Cantidad: " + loadedExercises.size());
                     } else {
                          Log.w(TAG, "No se encontraron ejercicios para rutina ID: " + routineId);
                     }
                 });
             } catch (Exception e) {
                  Log.e(TAG, "Error cargando ejercicios para rutina ID: " + routineId, e);
                 runOnUiThread(() -> Toast.makeText(this, "Error al cargar ejercicios", Toast.LENGTH_SHORT).show());
             }
         });
     }


    @Override
    public void onExerciseAdded(String name, String setsStr, String reps, String weightStr) {
        int sets = 0; try { sets = Integer.parseInt(setsStr); } catch (NumberFormatException e) {}
        double weight = 0.0; if (weightStr != null && !weightStr.isEmpty()) { try { weight = Double.parseDouble(weightStr); } catch (NumberFormatException e) {} }

        // Asignar ID 0 temporalmente si es nueva rutina, o el ID real si estamos editando
        long routineIdForExercise = (currentRoutineId != -1L) ? currentRoutineId : 0;
        Exercise newExercise = new Exercise(routineIdForExercise, name, sets, reps, weight, "");
        currentExercises.add(newExercise);

        if (exerciseAdapter != null) {
             int insertedPosition = currentExercises.size() - 1;
             exerciseAdapter.notifyItemInserted(insertedPosition);
             if (exercisesRecyclerView != null) {
                 exercisesRecyclerView.scrollToPosition(insertedPosition);
             }
        }
        Toast.makeText(this, "Ejercicio añadido (en memoria)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteExercise(int position) {
         if (currentExercises != null && position >= 0 && position < currentExercises.size()) {
             Exercise removedExercise = currentExercises.get(position); // Guardar referencia antes de borrar
             currentExercises.remove(position);
             if (exerciseAdapter != null) {
                 exerciseAdapter.notifyItemRemoved(position);
                 exerciseAdapter.notifyItemRangeChanged(position, currentExercises.size());
                 Toast.makeText(this, "Ejercicio eliminado (en memoria)", Toast.LENGTH_SHORT).show();

                 // Si la rutina ya existía en la BD Y el ejercicio tenía ID de BD, borrarlo también de la BD
                 if (currentRoutineId != -1L && removedExercise.getExerciseId() > 0) {
                     deleteExerciseFromDb(removedExercise);
                 }
             }
         }
    }

    private void deleteExerciseFromDb(Exercise exerciseToDelete) {
         databaseExecutor.execute(() -> {
             try {
                  exerciseDao.deleteExercise(exerciseToDelete);
                  Log.d(TAG,"Ejercicio borrado de BD: ID " + exerciseToDelete.getExerciseId());
                  // No es necesario actualizar UI aquí, ya se hizo en onDeleteExercise
             } catch (Exception e) {
                  Log.e(TAG, "Error borrando ejercicio de BD", e);
                  runOnUiThread(() -> Toast.makeText(this, "Error al borrar ejercicio de BD", Toast.LENGTH_SHORT).show());
             }
         });
    }


    private void saveRoutineAndExercises() {
         String name = routineNameEditText.getText().toString().trim();
         if (name.isEmpty()) {
             Toast.makeText(this, "El nombre de la rutina no puede estar vacío.", Toast.LENGTH_SHORT).show();
             return;
         }

         final Routine routineToSave = new Routine();
         routineToSave.name = name;
         routineToSave.description = "Descripción pendiente..."; // TODO: Añadir campo descripción

         ExecutorService executor = AppDatabase.databaseWriteExecutor;

         executor.execute(() -> {
             long savedRoutineId = currentRoutineId;
             boolean success = false;

             try {
                 if (isNewRoutine) {
                     savedRoutineId = routineDao.insertRoutine(routineToSave);
                     if (savedRoutineId > 0) { // Check si la inserción fue exitosa (Room devuelve ID > 0)
                         currentRoutineId = savedRoutineId; // Actualizar ID local
                         isNewRoutine = false; // Ya no es nueva
                         success = true;
                         Log.d(TAG, "Nueva rutina insertada con ID: " + savedRoutineId);
                     } else {
                          Log.e(TAG, "Error insertando nueva rutina, ID devuelto: " + savedRoutineId);
                     }
                 } else {
                     routineToSave.routineId = currentRoutineId;
                     routineDao.updateRoutine(routineToSave);
                     exerciseDao.deleteExercisesForRoutine(currentRoutineId);
                     success = true; // Asumir éxito si no hay excepción
                     Log.d(TAG, "Rutina existente actualizada ID: " + currentRoutineId + ", ejercicios antiguos borrados.");
                 }

                 if (success && currentRoutineId != -1L && currentExercises != null && !currentExercises.isEmpty()) {
                     Log.d(TAG, "Guardando " + currentExercises.size() + " ejercicios para rutina ID: " + currentRoutineId);
                     for (Exercise ex : currentExercises) {
                         ex.setRoutineOwnerId(currentRoutineId);
                     }
                     exerciseDao.insertAllExercises(currentExercises);
                     Log.d(TAG, "Ejercicios guardados/actualizados en BD.");
                 } else if (!success) {
                      Log.e(TAG,"No se guardaron ejercicios porque la rutina no se pudo guardar/actualizar.");
                 }

                 // Volver al hilo principal SOLO si todo fue bien (o manejar errores)
                 if (success) {
                     runOnUiThread(() -> {
                         Toast.makeText(EditRoutineActivity.this, "Rutina guardada", Toast.LENGTH_SHORT).show();
                         finish();
                     });
                 } else {
                      runOnUiThread(() -> Toast.makeText(EditRoutineActivity.this, "Error al guardar rutina", Toast.LENGTH_SHORT).show());
                 }

             } catch (Exception e) {
                  Log.e(TAG, "Error al guardar rutina/ejercicios en BD", e);
                  runOnUiThread(() -> Toast.makeText(EditRoutineActivity.this, "Error al guardar en BD", Toast.LENGTH_SHORT).show());
             }
         });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}