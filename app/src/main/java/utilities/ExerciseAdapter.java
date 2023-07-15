package utilities;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartworkout.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import intefaces.ExerciseCallback;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;

    private ArrayList<Exercise> exerciseList;

    private ExerciseCallback exerciseCallback;


    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    public void setExerciseCallback(ExerciseCallback exerciseCallback) {
        this.exerciseCallback = exerciseCallback;
    }

    public void addExercises(ArrayList<Exercise> list) {
        exerciseList=list;
    }
    public void removeExercise(int p) {
        exerciseList.remove(p);

    }
   // public void addExercise(Exercise ex) {
    //    exerciseList.add(ex);
  //  }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        if (exercise != null) {
            holder.theName.setText(exercise.getName());
            holder.theSet.setText(exercise.getNumOfSets());
            holder.theRepetitions.setText(exercise.getNumOfReps());
            holder.theWeight.setText(exercise.getWeight());
        }

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public Exercise getItem(int position) {
        return this.exerciseList.get(position);
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView exerciseNameText, theName;
        public MaterialTextView setText, theSet;
        public MaterialTextView repetitionsText, theRepetitions;
        public MaterialTextView weightText, theWeight;
        public MaterialTextView removeText, editText;
        public ShapeableImageView removeImage, editImage;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            findItemsView();

            removeImage.setOnClickListener(v -> {
                if (exerciseCallback != null)
                    exerciseCallback.removeClick(getItem(getAdapterPosition()), getAdapterPosition());
            });
            removeText.setOnClickListener(v -> {
                if (exerciseCallback != null)
                    exerciseCallback.removeClick(getItem(getAdapterPosition()), getAdapterPosition());
            });

            itemView.setOnClickListener(v -> {
                if (exerciseCallback != null)
                    exerciseCallback.itemClick(getItem(getAdapterPosition()), getAdapterPosition());

            });

            editText.setOnClickListener(v -> {
                if (exerciseCallback != null)
                    exerciseCallback.editClick(getItem(getAdapterPosition()), getAdapterPosition());

            });

            editImage.setOnClickListener(v -> {
                if (exerciseCallback != null)
                    exerciseCallback.editClick(getItem(getAdapterPosition()), getAdapterPosition());

            });
        }

        private void findItemsView() {
            exerciseNameText = itemView.findViewById(R.id.exercise_name);
            theName = itemView.findViewById(R.id.the_name);

            setText = itemView.findViewById(R.id.set_text);
            theSet = itemView.findViewById(R.id.set_number);

            repetitionsText = itemView.findViewById(R.id.repetitions_text);
            theRepetitions = itemView.findViewById(R.id.repetitions_number);

            weightText = itemView.findViewById(R.id.weight_text);
            theWeight = itemView.findViewById(R.id.weight_number);

            removeText = itemView.findViewById(R.id.remove);
            editText = itemView.findViewById(R.id.edit);

            removeImage = itemView.findViewById(R.id.remove_image);
            editImage = itemView.findViewById(R.id.edit_image);
        }
    }
}