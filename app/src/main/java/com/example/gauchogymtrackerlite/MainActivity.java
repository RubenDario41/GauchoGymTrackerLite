package com.example.gauchogymtrackerlite; // <-- ¡¡ASEGÚRATE DE QUE ESTE ES TU PAQUETE!!

// Imports necesarios
import android.content.Intent;
import android.content.SharedPreferences; // Para leer preferencias
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // Para aplicar el tema
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// import androidx.activity.EdgeToEdge;

// Imports de Activities
import com.example.gauchogymtrackerlite.RoutinesActivity;
import com.example.gauchogymtrackerlite.HistoryActivity;
import com.example.gauchogymtrackerlite.SettingsActivity;
import com.example.gauchogymtrackerlite.ActiveExerciseActivity;

// Import específico para MaterialButton si lo usas directamente
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {

    // Constantes para SharedPreferences (pueden ser las mismas que en SettingsActivity)
    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String PREF_NIGHT_MODE = "NightMode";

    private static final String TAG = "MainActivity";

    // Variables de UI (si las necesitas como miembro)
    private MaterialButton startButton;
    private MaterialButton routinesButton;
    private MaterialButton historyButton;
    private View settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- APLICAR TEMA GUARDADO ANTES DE TODO ---
        applySavedTheme(); // Llamamos a nuestro nuevo método
        // --- FIN APLICAR TEMA ---

        super.onCreate(savedInstanceState); // Llamada a super después de aplicar tema

        // EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Iniciando y layout inflado.");

        // Código opcional Insets...
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // --- Inicialización y DIAGNÓSTICO con findViewById ---
         Log.d(TAG, "Buscando vistas...");
        startButton = findViewById(R.id.startButton);
        routinesButton = findViewById(R.id.routinesButton);
        historyButton = findViewById(R.id.historyButton);
        settingsButton = findViewById(R.id.settingsButton);

        // Comprobaciones null...
        if (startButton == null) Log.e(TAG, "ERROR: startButton no encontrado!"); else Log.d(TAG, "startButton encontrado.");
        if (routinesButton == null) Log.e(TAG, "ERROR: routinesButton no encontrado!"); else Log.d(TAG, "routinesButton encontrado.");
        if (historyButton == null) Log.e(TAG, "ERROR: historyButton no encontrado!"); else Log.d(TAG, "historyButton encontrado.");
        if (settingsButton == null) Log.e(TAG, "ERROR: settingsButton no encontrado!"); else Log.d(TAG, "settingsButton encontrado.");


        // --- Asignación de OnClickListeners ---
         Log.d(TAG, "Asignando Listeners...");
        // ... (Código de los listeners que ya tenías y funcionaba) ...
        if (routinesButton != null) {
            routinesButton.setOnClickListener(v -> {
                Log.d(TAG, "Click en routinesButton");
                Intent intent = new Intent(MainActivity.this, RoutinesActivity.class);
                startActivity(intent);
            });
        } else { Log.w(TAG, "Listener no asignado a routinesButton (null)."); }

        if (historyButton != null) {
            historyButton.setOnClickListener(v -> {
                Log.d(TAG, "Click en historyButton");
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        } else { Log.w(TAG, "Listener no asignado a historyButton (null)."); }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                Log.d(TAG, "Click en settingsButton");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        } else { Log.w(TAG, "Listener no asignado a settingsButton (null)."); }

        if (startButton != null) {
            startButton.setOnClickListener(v -> {
                Log.d(TAG, "Click en startButton");
                Toast.makeText(MainActivity.this, "Iniciar Entrenamiento Próximamente", Toast.LENGTH_SHORT).show();
            });
        } else { Log.w(TAG, "Listener no asignado a startButton (null)."); }

        Log.d(TAG, "onCreate: Fin del método.");
    } // Fin onCreate


    // --- MÉTODO NUEVO PARA APLICAR TEMA ---
    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int nightMode = prefs.getInt(PREF_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
             Log.d(TAG, "Aplicando modo noche guardado: " + nightMode);
            AppCompatDelegate.setDefaultNightMode(nightMode);
            // No es necesario recrear aquí, super.onCreate lo manejará
        } else {
             Log.d(TAG, "Modo noche actual (" + AppCompatDelegate.getDefaultNightMode() + ") ya coincide con el guardado (" + nightMode + "). No se aplica cambio.");
        }
    }
    // --- FIN MÉTODO NUEVO ---

} // Fin clase MainActivity