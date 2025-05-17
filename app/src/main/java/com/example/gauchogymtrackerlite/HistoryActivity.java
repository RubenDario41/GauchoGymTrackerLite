package com.example.gauchogymtrackerlite; // <-- ¡¡ASEGÚRATE DE QUE ESTE ES TU PAQUETE!!

// --- IMPORTS NECESARIOS ---
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; // Para que sea una Activity
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager; // Para el LayoutManager
import androidx.recyclerview.widget.RecyclerView; // Para el RecyclerView

import android.os.Bundle; // Para onCreate
import android.util.Log;
import android.view.MenuItem;

// Imports para datos y Adapter
import java.util.ArrayList;
import java.util.List;
// --- FIN IMPORTS ---

// Asegúrate que extiende AppCompatActivity
public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    // Variables para RecyclerView
    private RecyclerView historyRecyclerView;
    private HistoryAdapter adapter; // Usaremos el HistoryAdapter que creamos
    private List<HistoryEntry> historyData; // Usaremos la clase HistoryEntry

    @Override // Anotación correcta
    protected void onCreate(Bundle savedInstanceState) { // Bundle reconocido
        super.onCreate(savedInstanceState); // super reconocido
        setContentView(R.layout.activity_history); // setContentView reconocido
        Log.d(TAG, "onCreate: Iniciando...");

        // --- Configuración de la Toolbar ---
        Toolbar toolbar = findViewById(R.id.historyToolbar); // Toolbar y findViewById reconocidos
        setSupportActionBar(toolbar); // setSupportActionBar reconocido
        if (getSupportActionBar() != null) { // getSupportActionBar reconocido
            getSupportActionBar().setTitle("Historial de Entrenamientos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Log.d(TAG, "Toolbar configurada.");
        } else {
             Log.w(TAG, "Toolbar no encontrada.");
        }
        // --- Fin Configuración Toolbar ---


        // --- CONFIGURACIÓN RECYCLERVIEW ---
        Log.d(TAG, "Configurando RecyclerView...");
        historyRecyclerView = findViewById(R.id.historyRecyclerView); // RecyclerView reconocido

        if (historyRecyclerView != null) {
            Log.d(TAG, "RecyclerView encontrado.");

            // Crear datos de ejemplo (Mover a fuente de datos real más adelante)
            historyData = createSampleHistoryData();
            Log.d(TAG, "Datos de ejemplo creados (" + historyData.size() + " items).");


            // Crear LayoutManager (Define cómo se muestran los ítems)
            LinearLayoutManager layoutManager = new LinearLayoutManager(this); // LinearLayoutManager reconocido
            Log.d(TAG, "LayoutManager creado.");

            // Crear Adapter (Puente entre datos y RecyclerView)
            adapter = new HistoryAdapter(this, historyData); // Pasamos contexto y datos
             Log.d(TAG, "Adapter creado.");

            // Asignar LayoutManager y Adapter al RecyclerView
            historyRecyclerView.setLayoutManager(layoutManager);
            historyRecyclerView.setAdapter(adapter);
            Log.d(TAG, "LayoutManager y Adapter asignados.");

        } else {
            Log.e(TAG, "ERROR: historyRecyclerView no encontrado! Verifica ID en activity_history.xml.");
        }
        // --- FIN CONFIGURACIÓN RECYCLERVIEW ---


        Log.d(TAG, "onCreate: Fin del método.");
    } // Fin onCreate


    // Método auxiliar para crear datos de ejemplo
    private List<HistoryEntry> createSampleHistoryData() { // List y ArrayList reconocidos
        List<HistoryEntry> list = new ArrayList<>();
        // Añadimos algunas entradas de ejemplo
        list.add(new HistoryEntry("28 Abr 2025", "Rutina Pecho/Tríceps - 6 ejercicios - 55 min"));
        list.add(new HistoryEntry("26 Abr 2025", "Rutina Pierna Completa - 7 ejercicios - 65 min"));
        list.add(new HistoryEntry("24 Abr 2025", "Rutina Espalda/Bíceps - 6 ejercicios - 50 min"));
        list.add(new HistoryEntry("22 Abr 2025", "Cardio Intenso - 30 min"));
        list.add(new HistoryEntry("21 Abr 2025", "Rutina Hombro/Trapecio - 5 ejercicios - 45 min"));
        list.add(new HistoryEntry("19 Abr 2025", "Full Body Workout - 8 ejercicios - 60 min"));
        list.add(new HistoryEntry("17 Abr 2025", "Rutina Pecho/Tríceps - 6 ejercicios - 58 min"));
        return list;
    }


    // Manejo del botón de regreso de la Toolbar
    @Override // Anotación correcta
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // NonNull y MenuItem reconocidos
         Log.d(TAG, "onOptionsItemSelected: Item presionado ID: " + item.getItemId());
        if (item.getItemId() == android.R.id.home) {
             Log.d(TAG, "onOptionsItemSelected: Botón Home presionado, finalizando Activity.");
            finish(); // finish() reconocido
            return true;
        }
        return super.onOptionsItemSelected(item); // super reconocido
    }
} // Fin de la clase HistoryActivity