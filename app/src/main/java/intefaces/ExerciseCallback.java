package intefaces;

import utilities.Exercise;

public interface ExerciseCallback {
    void itemClick(Exercise exercise, int position);
    void removeClick(Exercise exercise,int position);
    void editClick(Exercise exercise,int position);
}
