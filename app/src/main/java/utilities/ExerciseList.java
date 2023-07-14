package utilities;

import java.util.ArrayList;

public class ExerciseList {
    private ArrayList<Exercise> allExercises;

    public ExerciseList() {
        this.allExercises = new ArrayList<>();
    }

    public ArrayList<Exercise> getAllExercises() {
        return allExercises;
    }

    public void setAllExercises(ArrayList<Exercise> allExercises) {
        this.allExercises = allExercises;
    }

    @Override
    public String toString() {
        return "ExerciseList{" +
                "allExercises=" + allExercises +
                '}';
    }
}

