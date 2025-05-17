package com.example.gauchogymtrackerlite; // <-- REEMPLAZA con tu paquete

// --- IMPORTS NECESARIOS ---
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // O TextInputEditText
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // Para crear el diálogo
import androidx.fragment.app.DialogFragment; // Extiende de DialogFragment

import com.google.android.material.textfield.TextInputEditText; // Si usas este tipo
// --- FIN IMPORTS ---

public class AddExerciseDialogFragment extends DialogFragment {

    private static final String TAG = "AddExerciseDialog";

    // Interfaz para comunicar el resultado a la Activity/Fragment que lo llamó
    public interface AddExerciseListener {
        void onExerciseAdded(String name, String sets, String reps, String weight);
    }

    private AddExerciseListener listener;

    // Vistas del diálogo
    private TextInputEditText nameEditText;
    private TextInputEditText setsEditText;
    private TextInputEditText repsEditText;
    private TextInputEditText weightEditText;
    private Button addButton;
    private Button cancelButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verifica que la Activity host implemente la interfaz de callback
        try {
            // Instancia el listener para poder enviar eventos a la Activity
            listener = (AddExerciseListener) context;
             Log.d(TAG, "Listener asignado correctamente.");
        } catch (ClassCastException e) {
            // La activity no implementa la interfaz, lanzar excepción
            throw new ClassCastException(context.toString()
                    + " debe implementar AddExerciseListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_exercise, null);
        Log.d(TAG, "Layout del diálogo inflado.");

        // Encontrar las vistas DENTRO del layout del diálogo (view)
        nameEditText = view.findViewById(R.id.exerciseNameEditTextDialog);
        setsEditText = view.findViewById(R.id.setsEditTextDialog);
        repsEditText = view.findViewById(R.id.repsEditTextDialog);
        weightEditText = view.findViewById(R.id.weightEditTextDialog);
        addButton = view.findViewById(R.id.addDialogButton);
        cancelButton = view.findViewById(R.id.cancelDialogButton);

        if (nameEditText == null || setsEditText == null || repsEditText == null || weightEditText == null || addButton == null || cancelButton == null) {
             Log.e(TAG, "ERROR: No se encontró alguna de las vistas en dialog_add_exercise.xml!");
             // Podrías mostrar un error o simplemente cerrar el diálogo
        } else {
            Log.d(TAG, "Vistas del diálogo encontradas.");
        }

        // Configurar listeners para los botones del diálogo
        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> {
                Log.d(TAG, "Botón Cancelar del diálogo presionado.");
                dismiss(); // Cierra el diálogo
            });
        }

        if (addButton != null) {
            addButton.setOnClickListener(v -> {
                Log.d(TAG, "Botón Añadir del diálogo presionado.");
                // Leer datos de los EditText
                String name = nameEditText.getText().toString().trim();
                String sets = setsEditText.getText().toString().trim();
                String reps = repsEditText.getText().toString().trim();
                String weight = weightEditText.getText().toString().trim(); // Puede estar vacío

                // Validación simple (puedes mejorarla)
                if (name.isEmpty() || sets.isEmpty() || reps.isEmpty()) {
                    Toast.makeText(getContext(), "Nombre, Series y Reps son requeridos", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Intento de añadir ejercicio con campos requeridos vacíos.");
                } else {
                    // Enviar datos de vuelta a la Activity usando la interfaz
                    listener.onExerciseAdded(name, sets, reps, weight);
                    Log.i(TAG, "Ejercicio añadido: " + name + ", " + sets + "x" + reps + ", " + weight + "kg");
                    dismiss(); // Cierra el diálogo
                }
            });
        }

        // Usar la vista inflada para el diálogo y no añadir botones por defecto (null)
        // porque ya los tenemos en nuestro layout
        builder.setView(view);
        // No establecemos título ni botones aquí si ya están en el layout

        Log.d(TAG, "Creando AlertDialog.");
        return builder.create();
    }
}