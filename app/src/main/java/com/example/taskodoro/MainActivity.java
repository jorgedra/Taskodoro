package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskodoro.classes.Task;
import com.example.taskodoro.recycler_view.TaskMainAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class MainActivity extends AppCompatActivity {

    private final String DEFAULT_TIME = "25:00";

    private DatabaseReference myRef;

    private FirebaseDatabase database;

    private TaskMainAdapter taskMainAdapter;

    private Vibrator vibrator;
    private TextView txt_counter_text;

    private TextView txt_sesion_title;
    private Button button_start_work;
    private Button button_set_time;

    private EditText editText_custom_time;

    private RecyclerView rv_task_list;

    private CountDownTimer countDownTimer;

    private long timeLeftInMilliseconds = 0;

    private long secondsLeftInMilliseconds = 0;

    private long[] vibrationPattern = {500, 550, 500, 550};
    private boolean timerRunning;

    private String custom_time;

    private ArrayList<Task> tasksList = new ArrayList<>();

    private ArrayList<Long> minutesSpentArray = new ArrayList<>();

    private ArrayList<Long> secondsSpentArray = new ArrayList<>();

    private Instant instantStart;

    private Instant instantEnd;

    private Long timeSpent;

    private String currentUser;

    private String sesionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        txt_counter_text = (TextView) findViewById(R.id.txt_work_counter);
        button_start_work = (Button) findViewById(R.id.bt_start_work);
        button_set_time = (Button) findViewById(R.id.bt_set_time);
        editText_custom_time = (EditText) findViewById(R.id.edt_custom_time);
        txt_sesion_title = (TextView) findViewById(R.id.txt_sesion_title);
        rv_task_list = (RecyclerView) findViewById(R.id.rv_task_list);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            sesionName = extras.getString("sesionName");

            txt_sesion_title.setText(sesionName);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rv_task_list.setLayoutManager(layoutManager);

            getTaskFromSesion(sesionName, currentUser);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_task_list.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            rv_task_list.addItemDecoration(dividerItemDecoration);



            //regex for time input
            String pattern = "([0-5][0-9]):([0-5][0-9])"; // regex patter that gets the group of minutes and the group of seconds
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(DEFAULT_TIME);

            if (m.find()) {
                txt_counter_text.setText(DEFAULT_TIME);
                timeLeftInMilliseconds = Long.parseLong(m.group(1)) * 60000; //should be on a method
            }
            button_set_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custom_time = String.valueOf(editText_custom_time.getText());
                    Matcher m = r.matcher(custom_time); // THIS SHOULD BE ON A METHOD

                    if (m.find()) {
                        txt_counter_text.setText(custom_time);
                        timeLeftInMilliseconds = Long.parseLong(m.group(1)) * 60000; // with the regex we retrive the group of digits before the (:), this would be the minutes
                        secondsLeftInMilliseconds = Long.parseLong(m.group(2)) * 1000; // with the regex we retrive the group of digits after the (:), this would be the seconds
                        timeLeftInMilliseconds = timeLeftInMilliseconds + secondsLeftInMilliseconds; // adding both we get the total time left in milliseconds that we need in coundDownTimer
                        messageToast("Timer value changed");
                    } else {
                        editText_custom_time.setError("Format has to be: (xx:xx) ");
                    }
                }
            });

            button_start_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startStop();
                }
            });
        }
    }

    public void startStop() {
        if (timerRunning) {
            stopTimer();
            instantEnd = Instant.now();
            long timeElapsed = Duration.between(instantStart, instantEnd).toMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
            timeElapsed -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
            minutesSpentArray.add(minutes);
            secondsSpentArray.add(seconds);
        } else {
            startTimer();
            instantStart = Instant.now();
            button_set_time.setVisibility(View.INVISIBLE);// has to change into another button that cancel the timer
            editText_custom_time.setVisibility(View.INVISIBLE);
        }
    }

    private void stopTimer() {
        countDownTimer.cancel();
        button_start_work.setText("WORK");
        timerRunning = false;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0));
                instantEnd = Instant.now();
                long timeElapsed = Duration.between(instantStart, instantEnd).toMillis();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
                timeElapsed -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
                minutesSpentArray.add(minutes);
                secondsSpentArray.add(seconds);
            }
        }.start();

        button_start_work.setText("STOP");
        timerRunning = true;
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        txt_counter_text.setText(timeLeftText);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        vibrator.cancel();
        return super.dispatchTouchEvent(ev);
    }

    private void getTaskFromSesion(String sesionName, String currentUser){
        myRef.child("sesions").child(currentUser).child(sesionName).child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String taskName = String.valueOf(ds.child("taskName").getValue());
                        Boolean taskStatus = (Boolean) ds.child("taskStatus").getValue();
                        tasksList.add(new Task(taskName, taskStatus));
                    }
                    taskMainAdapter = new TaskMainAdapter(R.layout.task_main_view, tasksList);
                    rv_task_list.setAdapter(taskMainAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void messageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        long totalMinutesTimeSpent = 0;
        long totalSecondsTimeSpent = 0;
        for (long minutesTimeValue : minutesSpentArray) {
            totalMinutesTimeSpent += minutesTimeValue;
        }
        for (long secondsTimeValue : secondsSpentArray) {
            totalSecondsTimeSpent += secondsTimeValue;
        }
        String timeSpentToString = String.format("%02d:%02d", totalMinutesTimeSpent, totalSecondsTimeSpent);
//        String timeSpentToString = String.valueOf(totalMinutesTimeSpent + ":" + totalSecondsTimeSpent);
        myRef.child("sesions").child(currentUser).child(sesionName).child("timeSpend").setValue(timeSpentToString);
    }
}