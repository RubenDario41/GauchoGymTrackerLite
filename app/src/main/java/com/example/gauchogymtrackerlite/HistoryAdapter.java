package com.example.gauchogymtrackerlite; // <-- REEMPLAZA con tu paquete

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast; // Para clicks futuros (opcional ahora)
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = "HistoryAdapter";
    private List<HistoryEntry> historyList;
    private Context context;

    // Constructor
    public HistoryAdapter(Context context, List<HistoryEntry> historyList) {
        this.context = context;
        this.historyList = historyList;
        Log.d(TAG, "Adapter creado con " + (historyList != null ? historyList.size() : 0) + " items.");
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Inflando item_history...");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryEntry currentEntry = historyList.get(position);
        Log.d(TAG, "onBindViewHolder: Posición " + position + ", Fecha: " + currentEntry.date);

        holder.dateTextView.setText(currentEntry.date);
        holder.summaryTextView.setText(currentEntry.summary);
    }

    @Override
    public int getItemCount() {
        int count = historyList != null ? historyList.size() : 0;
        // Log.d(TAG, "getItemCount: Devolviendo " + count);
        return count;
    }

    // --- ViewHolder Interno ---
    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView summaryTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Log.d(TAG, "HistoryViewHolder: Constructor llamado.");
            dateTextView = itemView.findViewById(R.id.historyDateTextView);
            summaryTextView = itemView.findViewById(R.id.historySummaryTextView);

            if (dateTextView == null) {
                Log.e(TAG, "HistoryViewHolder: ERROR - historyDateTextView no encontrado!");
            }
             if (summaryTextView == null) {
                Log.e(TAG, "HistoryViewHolder: ERROR - historySummaryTextView no encontrado!");
             }

            // --- Placeholder para Click Listener Futuro ---
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && historyList != null) {
                    HistoryEntry clickedEntry = historyList.get(position);
                    Log.d(TAG, "itemView onClick: Entrada seleccionada - Fecha: " + clickedEntry.date);
                    Toast.makeText(context, "Historial: " + clickedEntry.date, Toast.LENGTH_SHORT).show();
                    // Aquí podrías navegar a una pantalla de detalle del historial
                }
            });
            // --- Fin Placeholder ---
        }
    }
    // --- Fin ViewHolder ---
}