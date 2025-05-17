package com.example.gauchogymtrackerlite;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private static final String TAG = "ExerciseAdapter";
    private List<Exercise> exerciseList;
    private ExerciseInteractionListener listener;

    public interface ExerciseInteractionListener {
        void onDeleteExercise(int position);
    }

    public ExerciseAdapter(List<Exercise> exerciseList, ExerciseInteractionListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise currentExercise = exerciseList.get(position);
        holder.bind(currentExercise);
    }

    @Override
    public int getItemCount() {
        return exerciseList != null ? exerciseList.size() : 0;
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView detailsTextView;
        ImageButton deleteButton;

        ExerciseViewHolder(@NonNull View itemView, ExerciseInteractionListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemExerciseNameTextView);
            detailsTextView = itemView.findViewById(R.id.itemExerciseDetailsTextView);
            deleteButton = itemView.findViewById(R.id.deleteExerciseButton);

            if (deleteButton != null && listener != null) {
                 deleteButton.setOnClickListener(v -> {
                     int position = getAdapterPosition();
                     if (position != RecyclerView.NO_POSITION) {
                         listener.onDeleteExercise(position);
                     }
                 });
            }
        }

        void bind(Exercise exercise) {
            nameTextView.setText(exercise.getName());
            String weightStr = "";
            if (exercise.getWeight() > 0) {
                 if (exercise.getWeight() == (long) exercise.getWeight()) {
                    weightStr = String.format(Locale.getDefault(), " - %dkg", (long)exercise.getWeight());
                 } else {
                    weightStr = String.format(Locale.getDefault(), " - %.1fkg", exercise.getWeight());
                 }
            }
            String details = String.format(Locale.getDefault(), "%dx%s%s",
                                       exercise.getSets(),
                                       exercise.getReps(),
                                       weightStr);
            detailsTextView.setText(details);
        }
    }
}