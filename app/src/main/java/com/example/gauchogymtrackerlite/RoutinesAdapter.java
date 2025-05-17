package com.example.gauchogymtrackerlite;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.RoutineViewHolder> {

    private static final String TAG = "RoutinesAdapter";
    private List<Routine> routineList;
    private Context context;

    public RoutinesAdapter(Context context, List<Routine> routineList) {
        this.context = context;
        this.routineList = routineList != null ? routineList : new ArrayList<>();
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine, parent, false);
        return new RoutineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        if (routineList == null || position >= routineList.size()) {
            return;
        }
        Routine currentRoutine = routineList.get(position);
        holder.bind(currentRoutine);
    }

    @Override
    public int getItemCount() {
        return routineList != null ? routineList.size() : 0;
    }

    public void setRoutines(List<Routine> newRoutineList) {
        if (newRoutineList == null) {
            this.routineList = new ArrayList<>();
        } else {
            this.routineList = newRoutineList;
        }
        notifyDataSetChanged();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descTextView;

        RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.routineNameTextView);
            descTextView = itemView.findViewById(R.id.routineDescTextView);

            itemView.setOnClickListener(v -> {
                 int position = getAdapterPosition();
                 if (position != RecyclerView.NO_POSITION && RoutinesAdapter.this.routineList != null) {
                     Routine clickedRoutine = RoutinesAdapter.this.routineList.get(position);
                     Intent intent = new Intent(context, EditRoutineActivity.class);
                     intent.putExtra("IS_NEW_ROUTINE", false);
                     intent.putExtra("ROUTINE_ID", clickedRoutine.routineId); // Pasar ID
                     intent.putExtra("ROUTINE_NAME", clickedRoutine.getName());
                     intent.putExtra("ROUTINE_DESCRIPTION", clickedRoutine.getDescription());
                     context.startActivity(intent);
                 }
            });
        }

        void bind(Routine routine) {
            nameTextView.setText(routine.getName());
            descTextView.setText(routine.getDescription());
        }
    }
}