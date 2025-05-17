package com.example.gauchogymtrackerlite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast; // <-- IMPORT AÑADIDO

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


public class RoutinesActivity extends AppCompatActivity {

    private static final String TAG = "RoutinesActivity";

    private RecyclerView routinesRecyclerView;
    private RoutinesAdapter adapter;
    private RoutineDao routineDao;
    private AppDatabase db;
    private ExecutorService databaseExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        Log.d(TAG, "onCreate: Iniciando RoutinesActivity...");

        db = AppDatabase.getDatabase(getApplicationContext());
        routineDao = db.routineDao();
        databaseExecutor = AppDatabase.databaseWriteExecutor;
        Log.d(TAG, "onCreate: Instancia de BD, DAO y Executor obtenida.");

        Toolbar toolbar = findViewById(R.id.routinesToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Rutinas");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        routinesRecyclerView = findViewById(R.id.routinesRecyclerView);
        setupRecyclerView();

        FloatingActionButton addRoutineFab = findViewById(R.id.addRoutineFab);
        if (addRoutineFab != null) {
            addRoutineFab.setOnClickListener(v -> {
                Intent intent = new Intent(RoutinesActivity.this, EditRoutineActivity.class);
                intent.putExtra("IS_NEW_ROUTINE", true);
                startActivity(intent);
            });
        }

        Log.d(TAG, "onCreate: Fin del método.");
    }

    private void setupRecyclerView() {
         if (routinesRecyclerView == null) {
              Log.e(TAG, "setupRecyclerView: ERROR - RecyclerView es null.");
              return;
         }
         Log.d(TAG, "setupRecyclerView: Configurando...");
         adapter = new RoutinesAdapter(this, new ArrayList<>());
         routinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         routinesRecyclerView.setAdapter(adapter);
         Log.d(TAG, "setupRecyclerView: RecyclerView configurado inicialmente.");
    }

    @Override
    protected void onResume() {
        super.onResume();
         Log.d(TAG, "onResume: Iniciando recarga de datos...");
        loadRoutinesAndUpdateAdapter();
    }

    private void loadRoutinesAndUpdateAdapter() {
         if (adapter == null || routineDao == null || databaseExecutor == null) {
              Log.w(TAG, "loadRoutinesAndUpdateAdapter: Adapter, DAO o Executor no inicializado todavía.");
              return;
         }
         databaseExecutor.execute(() -> {
             Log.d(TAG, "loadRoutinesAndUpdateAdapter: Ejecutando consulta getAllRoutines en background...");
             final List<Routine> routinesFromDb;
             try {
                 routinesFromDb = routineDao.getAllRoutines();
                 if (routinesFromDb != null) {
                     Log.d(TAG, "loadRoutinesAndUpdateAdapter: Se cargaron " + routinesFromDb.size() + " rutinas desde BD.");
                 } else {
                     Log.w(TAG, "loadRoutinesAndUpdateAdapter: La consulta devolvió null.");
                 }
             } catch (Exception e) {
                  Log.e(TAG, "loadRoutinesAndUpdateAdapter: Error al cargar rutinas", e);
                  runOnUiThread(() -> {
                      if(adapter != null) adapter.setRoutines(new ArrayList<>());
                      Toast.makeText(this,"Error al cargar rutinas", Toast.LENGTH_SHORT).show(); // Ahora Toast es reconocido
                  });
                  return;
             }

             runOnUiThread(() -> {
                 if (adapter != null) {
                     Log.d(TAG, "loadRoutinesAndUpdateAdapter: Actualizando adapter en UI thread...");
                     adapter.setRoutines(routinesFromDb != null ? routinesFromDb : new ArrayList<>());
                 } else {
                      Log.e(TAG, "loadRoutinesAndUpdateAdapter: Adapter se volvió null antes de actualizar UI!");
                 }
             });
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