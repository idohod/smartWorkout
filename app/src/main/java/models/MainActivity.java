package models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.smartworkout.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import intefaces.ExerciseCallback;
import utilities.Exercise;
import utilities.ExerciseAdapter;
import utilities.ExerciseList;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton addExerciseButton;
    private ExtendedFloatingActionButton changeUserButton;
    private MaterialTextView title;
    private RecyclerView recyclerView;
    private ExerciseAdapter exerciseAdapter;
    private ExerciseList allExercises;
    private String userName;
    private AppCompatImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        Glide
                .with(this)
                .load("https://e1.pxfuel.com/desktop-wallpaper/560/535/desktop-wallpaper-pin-on-gym-pics-gym-fitness-mobile.jpg")
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(backgroundImage);

        getUserName();
        loadExercisesFromDb();
        initViews();

        changeUserButton.setOnClickListener(v -> changeUser());
        addExerciseButton.setOnClickListener(v -> addExercise());
    }

    private void addExercise() {
        Intent i = new Intent(this, ExerciseDataActivity.class);
        i.putExtra("EDIT_EXERCISE", false);
        i.putExtra("userName", userName);
        startActivity(i);
        finish();
    }

    private void changeUser() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void getUserName() {
        Intent i = getIntent();
        userName = i.getStringExtra("username");
        String temp = userName;
        String newTitle = "'s plan";
        title.setText(temp += newTitle);

    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        if (allExercises == null) {
            allExercises = new ExerciseList();
        }
        exerciseAdapter = new ExerciseAdapter(this, allExercises.getAllExercises());

        recyclerView.setAdapter(exerciseAdapter);
        exerciseAdapter.setExerciseCallback(new ExerciseCallback() {
            @Override
            public void itemClick(Exercise exercise, int position) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                String value = exercise.getName();
                value += " timer";
                intent.putExtra("EXERCISE_NAME", value);
                intent.putExtra("username", userName);

                startActivity(intent);
                finish();

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void removeClick(Exercise exercise, int position) {

                allExercises.getAllExercises().remove(position);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference exsRef =db.getReference().child("users").child(userName).child("exercises");
                exsRef.setValue(allExercises);
                exerciseAdapter.notifyDataSetChanged();
            }

            @Override
            public void editClick(Exercise exercise, int position) {
                Intent intent = new Intent(getApplicationContext(), ExerciseDataActivity.class);
                intent.putExtra("EDIT_EXERCISE", true);
                intent.putExtra("position", position);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadExercisesFromDb() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference exercisesRef = db.getReference().child("users").child(userName).child("exercises");
        exercisesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ExerciseList exerciseList = snapshot.getValue(ExerciseList.class);
                if (exerciseList != null) {

                    allExercises = exerciseList;
                    exerciseAdapter.addExercises(allExercises.getAllExercises());

                    allExercises.setAllExercises(exerciseList.getAllExercises());
                    exerciseAdapter.notifyDataSetChanged();

                } else {
                    allExercises = new ExerciseList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findViews() {
        backgroundImage = findViewById(R.id.background);
        title = findViewById(R.id.title);
        changeUserButton = findViewById(R.id.change_user_button);
        addExerciseButton = findViewById(R.id.add_exercise_button);
        recyclerView = findViewById(R.id.exercises_list);
    }

    public void onBackPressed() {
        return;
    }
}

