package com.example.gauchogymtrackerlite; // <-- ¡¡ASEGÚRATE DE QUE ESTE ES TU PAQUETE!!

// --- IMPORTS NECESARIOS ---
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // ¡¡IMPORTANTE!! Para cambiar el tema
import androidx.appcompat.widget.Toolbar;

import android.content.Context; // Para SharedPreferences
import android.content.SharedPreferences; // Para guardar/leer preferencias
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup; // Para el grupo de botones de tema
// import android.widget.SeekBar; // Si usas el SeekBar
// import android.widget.TextView;
// --- FIN IMPORTS ---

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    // Constantes para SharedPreferences
    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String PREF_NIGHT_MODE = "NightMode";

    // Vistas de la UI
    private RadioGroup themeRadioGroup;
    // private SeekBar sensitivitySeekBar;
    // private TextView aboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(TAG, "onCreate: Iniciando...");

        // Configuración de la Toolbar
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ajustes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Log.d(TAG, "Toolbar configurada.");
        } else {
             Log.w(TAG, "Toolbar no encontrada.");
        }

        // Encontrar el RadioGroup
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        if (themeRadioGroup == null) {
            Log.e(TAG, "ERROR: themeRadioGroup no encontrado! Verifica ID en activity_settings.xml.");
            // No podemos continuar sin el RadioGroup
        } else {
             Log.d(TAG, "themeRadioGroup encontrado.");
             // Cargar la preferencia guardada y marcar el botón correcto
             loadAndSetCurrentTheme();

            // Configurar el listener para guardar cuando el usuario cambie la selección
            themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int selectedMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM; // Valor por defecto

                     // Determinar el modo basado en el ID del RadioButton seleccionado
                     // Necesitas asegurarte de que los IDs en R.id.* coincidan con tu XML
                     if (checkedId == R.id.radioThemeLight) {
                         selectedMode = AppCompatDelegate.MODE_NIGHT_NO;
                         Log.d(TAG, "Tema Claro seleccionado.");
                     } else if (checkedId == R.id.radioThemeDark) {
                         selectedMode = AppCompatDelegate.MODE_NIGHT_YES;
                          Log.d(TAG, "Tema Oscuro seleccionado.");
                     } else if (checkedId == R.id.radioThemeSystem) {
                         selectedMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                          Log.d(TAG, "Tema Sistema seleccionado.");
                     } else {
                         Log.w(TAG, "ID de RadioButton desconocido: " + checkedId);
                         // Mantener el valor por defecto (Sistema) si hay un error
                     }

                    // Guardar y aplicar el modo seleccionado
                    saveAndApplyTheme(selectedMode);
                }
            });
            Log.d(TAG,"Listener asignado a themeRadioGroup.");
        }

        // Aquí encontrarías y configurarías otros elementos como el SeekBar...
        // sensitivitySeekBar = findViewById(R.id.sensitivitySeekBar);


        Log.d(TAG, "onCreate: Fin del método.");
    } // Fin onCreate

    // Método para cargar la preferencia y actualizar la UI
    private void loadAndSetCurrentTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Obtener el modo guardado, si no hay ninguno, usar el del sistema como defecto
        int currentNightMode = prefs.getInt(PREF_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Log.d(TAG, "Modo de tema cargado: " + currentNightMode);

        // Marcar el RadioButton correcto basado en el modo cargado
        // Es importante hacer esto *antes* de asignar el listener para evitar que se dispare al inicio
        // o *dentro* del listener controlar que no se aplique si el modo no ha cambiado.
        // Por simplicidad, lo hacemos antes. Asegúrate que el listener no cause bucles.
        switch (currentNightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                themeRadioGroup.check(R.id.radioThemeLight);
                 Log.d(TAG, "Marcando RadioButton: Claro");
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                themeRadioGroup.check(R.id.radioThemeDark);
                 Log.d(TAG, "Marcando RadioButton: Oscuro");
                break;
            default: // Incluye MODE_NIGHT_FOLLOW_SYSTEM y cualquier otro caso no manejado
                themeRadioGroup.check(R.id.radioThemeSystem);
                 Log.d(TAG, "Marcando RadioButton: Sistema (por defecto)");
                break;
        }
        // Nota: No llamamos a AppCompatDelegate.setDefaultNightMode() aquí necesariamente,
        // porque el tema ya debería haberse aplicado correctamente al iniciar la app
        // si se guardó la preferencia en un cierre anterior. Si quisieras forzarlo
        // al entrar a Ajustes, podrías añadir la llamada aquí.
    }

    // Método para guardar la preferencia y aplicar el tema
    private void saveAndApplyTheme(int mode) {
        // Guardar en SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_NIGHT_MODE, mode);
        editor.apply(); // apply() es asíncrono, commit() es síncrono
         Log.d(TAG, "Preferencia de tema guardada: " + mode);

        // Aplicar el modo inmediatamente
        // Esto hará que el sistema cambie el tema y, a menudo, recreará la Activity
        AppCompatDelegate.setDefaultNightMode(mode);
         Log.d(TAG, "setDefaultNightMode llamado con: " + mode);
    }


    // Manejo del botón de regreso de la Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: Item presionado ID: " + item.getItemId());
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: Botón Home presionado, finalizando Activity.");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} // Fin de la clase SettingsActivity