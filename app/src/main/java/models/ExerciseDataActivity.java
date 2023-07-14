package models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.smartworkout.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import utilities.Exercise;
import utilities.ExerciseList;

public class ExerciseDataActivity extends AppCompatActivity {

    private AppCompatEditText exName, exSets, exRep, exWeight;
    private MaterialTextView textName, textSets, textRep, textWeight;
    private ExtendedFloatingActionButton saveButton;
    private ExerciseList allExercises;
    private String userName;
    private int position;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_data);
        editExerciseData();
        getUserName();
        findViews();
        initViews();

        saveButton.setOnClickListener(v->saveToDatabase());
    }
    private void getUserName(){
         Intent i = getIntent();
         userName = i.getStringExtra("userName");
    }
    private void saveToDatabase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if (!editExerciseData()) { //if false than add new exercise
            Exercise newExercise= createExercise();
            allExercises.getAllExercises().add(newExercise);
            DatabaseReference exercisesRef = db.getReference().
                    child("users").child(userName).child("exercises");
            exercisesRef.setValue(allExercises);


        } else {
            Exercise editedExercise = editExercise();

            allExercises.getAllExercises().set(position, editedExercise);
            DatabaseReference exercisesRef =
                    db.getReference().child("users").child(userName).
                            child("exercises").child("allExercises").child(""+position);
            exercisesRef.setValue(allExercises.getAllExercises().get(position));

        }
        moveActivity();
    }

    private boolean editExerciseData() {
        Intent i = getIntent();
        edit = i.getBooleanExtra("EDIT_EXERCISE", false);
        position = i.getIntExtra("position", 0);

        return edit;
    }


    private Exercise createExercise() {
        String enterName = exName.getText().toString();
        String enterSet = exSets.getText().toString();
        String enterReps = exRep.getText().toString();
        String enterWeight = exWeight.getText().toString();

        return new Exercise(enterName, enterSet, enterReps, enterWeight);
    }

    private Exercise editExercise() {

        Exercise exerciseToEdit = allExercises.getAllExercises().get(position);
        exerciseToEdit.setName(exName.getText().toString());
        exerciseToEdit.setNumOfSets(exSets.getText().toString());
        exerciseToEdit.setNumOfReps(exRep.getText().toString());
        exerciseToEdit.setWeight(exWeight.getText().toString());

        return exerciseToEdit;
    }

    private void showCurrentData(Exercise exerciseToEdit) {
        if (exerciseToEdit != null) {
            exName.setText(exerciseToEdit.getName());
            exSets.setText(exerciseToEdit.getNumOfSets());
            exRep.setText(exerciseToEdit.getNumOfReps());
            exWeight.setText(exerciseToEdit.getWeight());
        }
    }


    private void moveActivity() {
        Intent in = new Intent(this, MainActivity.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String str = user.getDisplayName();
            in.putExtra("username", str);
            startActivity(in);
            finish();
        }
    }
    private void initViews() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference exercisesRef = db.getReference()
                .child("users").child(userName).child("exercises");
        exercisesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ExerciseList exerciseList = snapshot.getValue(ExerciseList.class);
                if (exerciseList != null) {

                    allExercises = exerciseList;
                    allExercises.setAllExercises(exerciseList.getAllExercises());
                    if (edit) {
                        edit=false;
                        Exercise temp = allExercises.getAllExercises().get(position);
                        showCurrentData(temp);
                    }


                } else
                    allExercises = new ExerciseList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findViews() {
        exName = findViewById(R.id.name_field);
        exSets = findViewById(R.id.set_field);
        exRep = findViewById(R.id.repetitions_field);
        exWeight = findViewById(R.id.weight_field);
        textName = findViewById(R.id.exercise_text_name);
        textRep = findViewById(R.id.exercise_text_repetition);
        textSets = findViewById(R.id.exercise_text_set);
        textWeight = findViewById(R.id.exercise_text_weight);

        saveButton = findViewById(R.id.save_button);

    }

    public void onBackPressed() {
        super.onBackPressed();
        moveActivity();
    }


}