package com.example.gauchogymtrackerlite;
import com.example.gauchogymtrackerlite.AppDatabase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.gauchogymtrackerlite.RoutinesActivity;
import com.example.gauchogymtrackerlite.HistoryActivity;
import com.example.gauchogymtrackerlite.SettingsActivity;
import com.example.gauchogymtrackerlite.ActiveExerciseActivity;
import com.example.gauchogymtrackerlite.SelectRoutineDialogFragment;
import com.example.gauchogymtrackerlite.Routine;
import com.example.gauchogymtrackerlite.AppDatabase; // Importar AppDatabase
import com.example.gauchogymtrackerlite.RoutineDao;  // Importar RoutineDao

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList; // Importar ArrayList
import java.util.List;      // Importar List
import java.util.concurrent.ExecutorService; // Importar ExecutorService


public class MainActivity extends AppCompatActivity implements SelectRoutineDialogFragment.SelectRoutineListener {

    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String PREF_NIGHT_MODE = "NightMode";
    private static final String TAG = "MainActivity";

    private MaterialButton startButton;
    private MaterialButton routinesButton;
    private MaterialButton historyButton;
    private View settingsButton;

    private RoutineDao routineDao; // Para acceder a la BD
    private ExecutorService databaseExecutor; // Para hilos de BD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener instancia de la BD y DAO
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        routineDao = db.routineDao();
        databaseExecutor = AppDatabase.databaseWriteExecutor;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startButton = findViewById(R.id.startButton);
        routinesButton = findViewById(R.id.routinesButton);
        historyButton = findViewById(R.id.historyButton);
        settingsButton = findViewById(R.id.settingsButton);

        if (routinesButton != null) {
            routinesButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RoutinesActivity.class);
                startActivity(intent);
            });
        }

        if (historyButton != null) {
            historyButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }

        if (startButton != null) {
            startButton.setOnClickListener(v -> {
                Log.d(TAG, "Click en startButton, iniciando carga de rutinas para diálogo...");
                loadRoutinesAndShowDialog();
            });
        }
    }

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int nightMode = prefs.getInt(PREF_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode);
        }
    }

    private void loadRoutinesAndShowDialog() {
        Toast.makeText(this, "Cargando rutinas...", Toast.LENGTH_SHORT).show(); // Feedback al usuario
        databaseExecutor.execute(() -> {
            Log.d(TAG, "loadRoutinesAndShowDialog: Cargando rutinas en background...");
            final List<Routine> loadedRoutinesList;
            try {
                // Si aún usas allowMainThreadQueries, esto es seguro. Si no, también es seguro porque está en executor.
                loadedRoutinesList = routineDao.getAllRoutines();
            } catch (Exception e) {
                Log.e(TAG, "loadRoutinesAndShowDialog: Error cargando rutinas", e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al cargar rutinas", Toast.LENGTH_SHORT).show());
                return;
            }

            runOnUiThread(() -> {
                if (loadedRoutinesList != null && !loadedRoutinesList.isEmpty()) {
                    Log.d(TAG, "loadRoutinesAndShowDialog: Rutinas cargadas (" + loadedRoutinesList.size() + "), mostrando diálogo.");
                    
                    // Convertir List<Routine> a ArrayList<Routine> para el Bundle
                    ArrayList<Routine> routinesArrayList = new ArrayList<>(loadedRoutinesList);

                    FragmentManager fm = getSupportFragmentManager();
                    SelectRoutineDialogFragment selectDialog = SelectRoutineDialogFragment.newInstance(routinesArrayList);
                    selectDialog.show(fm, "SelectRoutineDialogTag"); // Usar un tag diferente
                } else {
                    Log.w(TAG, "loadRoutinesAndShowDialog: No hay rutinas para mostrar en el diálogo.");
                    Toast.makeText(MainActivity.this, "No hay rutinas disponibles para iniciar.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onRoutineSelected(Routine routine) {
        if (routine == null) {
             Log.e(TAG, "onRoutineSelected: La rutina recibida es null.");
             Toast.makeText(this, "Error al seleccionar rutina.", Toast.LENGTH_SHORT).show();
             return;
        }
        Log.d(TAG, "Rutina seleccionada: ID=" + routine.getRoutineId() + ", Nombre=" + routine.getName());
        Toast.makeText(this, "Iniciando rutina: " + routine.getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, ActiveExerciseActivity.class);
        intent.putExtra("ROUTINE_ID", routine.getRoutineId());
        startActivity(intent);
    }
}