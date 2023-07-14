package models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartworkout.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import utilities.SignalGenerator;

public class TimerActivity extends AppCompatActivity {
    private MaterialTextView title;
    private MaterialTextView timer;
    private ExtendedFloatingActionButton startButton;
    private ExtendedFloatingActionButton finishButton;
    private ExtendedFloatingActionButton backButton;

    private AppCompatImageView backgroundImage;
    private int startTime = 0;
    private final int DELAY=1000;
    private int seconds=0,minutes=0,hours=0;
    private String userName;
    private boolean pressed =false;
    private static final String URL_LINK="https://img.freepik.com/free-photo/dumbbells-wooden-floor_23-2147688480.jpg?size=626&ext=jpg&ga=GA1.1.1635363902.1669992793&semt=ais";
    private final Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, DELAY);
            if(pressed)
                updateTimeUI();

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        findViews();
        getUserName();
        Glide
                .with(this)
                .load(URL_LINK)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(backgroundImage);
        initView();
    }
    private void getUserName(){
        Intent i = getIntent();
        userName = i.getStringExtra("username");
    }

    private void initView() {
        startButton.setOnClickListener(v -> startTime());
        finishButton.setOnClickListener(v -> stopTime());
        backButton.setOnClickListener(v->backToMainActivity());
        setText();
    }

    private void backToMainActivity() {
        Intent intent =new Intent(TimerActivity.this,MainActivity.class);
        intent.putExtra("username",userName);
        startActivity(intent);
        finish();
    }

    private void stopTime() {

        handler.removeCallbacks(runnable);
        if (minutes < 1 && seconds < 5)
            SignalGenerator.getInstance().toast("too fast!!", 0);
        else if (minutes < 1 && seconds > 5 && seconds < 10)
            SignalGenerator.getInstance().toast("great time!!", 0);
        else
            SignalGenerator.getInstance().toast("too slow!!", 0);

    }

    private void startTime() {
        startTime = 0;
        seconds=0;
        minutes=0;
        hours=0;
        timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        pressed =true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(!handler.hasCallbacks(runnable)){
                handler.postDelayed(runnable,DELAY);

            }
        }

    }


    private void findViews() {
        title = findViewById(R.id.title);
        timer = findViewById(R.id.timer);
        startButton = findViewById(R.id.start_button);
        finishButton = findViewById(R.id.finish_button);
        backgroundImage = findViewById(R.id.background);
        backButton = findViewById(R.id.back_button);
    }

    private void updateTimeUI() {

        startTime++;
        if(startTime%60 !=0) {
            seconds = startTime % 60;
        }
        else {
            minutes++;
            seconds=0;
            startTime=0;
        }

        if(minutes != 0 && minutes%60==0){
            hours++;
            minutes=0;
            seconds=0;
            startTime=0;
        }

        timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void setText(){
        Intent i = getIntent();
        String newTitle = i.getStringExtra("EXERCISE_NAME");
        title.setText(newTitle);

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        backToMainActivity();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

