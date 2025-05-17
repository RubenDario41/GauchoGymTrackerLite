package com.example.gauchogymtrackerlite;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import com.example.gauchogymtrackerlite.Routine; // Asegurar import

public class SelectRoutineDialogFragment extends DialogFragment {

    private static final String TAG = "SelectRoutineDialog";
    private static final String ARG_ROUTINE_NAMES = "arg_routine_names";
    private static final String ARG_ROUTINES_LIST = "arg_routines_list";

    public interface SelectRoutineListener {
        void onRoutineSelected(Routine routine);
    }

    private SelectRoutineListener listener;
    private ArrayList<String> displayableRoutineNames;
    private ArrayList<Routine> fullRoutinesList;

    // Método factory para crear una nueva instancia y pasar datos
    public static SelectRoutineDialogFragment newInstance(ArrayList<Routine> routines) {
        SelectRoutineDialogFragment fragment = new SelectRoutineDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINES_LIST, routines); // Pasamos la lista de objetos Routine
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SelectRoutineListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SelectRoutineListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Seleccionar Rutina para Iniciar");

        if (getArguments() != null) {
            fullRoutinesList = (ArrayList<Routine>) getArguments().getSerializable(ARG_ROUTINES_LIST);
        }

        if (fullRoutinesList != null && !fullRoutinesList.isEmpty()) {
            displayableRoutineNames = new ArrayList<>();
            for (Routine r : fullRoutinesList) {
                displayableRoutineNames.add(r.getName());
            }
            CharSequence[] items = displayableRoutineNames.toArray(new CharSequence[0]);
            builder.setItems(items, (dialog, which) -> {
                if (listener != null && which >= 0 && which < fullRoutinesList.size()) {
                    listener.onRoutineSelected(fullRoutinesList.get(which));
                }
            });
        } else {
            builder.setMessage("No hay rutinas disponibles.");
        }

        builder.setNegativeButton("Cancelar", (dialog, which) -> dismiss());
        return builder.create();
    }
}