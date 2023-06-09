package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.MotionEvent;
import android.view.View;
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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String DEFAULT_WORK_TIME = "25:00";

    private final String DEFAULT_BREAK_TIME = "05:00";

    MediaPlayer mediaPlayer;

    private String pattern = "([0-5][0-9]):([0-5][0-9])";// regex patter that gets the group of minutes and the group of seconds

    private Pattern r = Pattern.compile(pattern);

    private DatabaseReference myRef;

    private FirebaseDatabase database;

    private TaskMainAdapter taskMainAdapter;

    private Vibrator vibrator;
    private TextView txt_counter_text;

    private TextView txt_session_title;
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

    private String currentUser;

    private String sessionName;

    boolean isABreak = false;

    @Override
    protected void onStop() { // to save the spent time when user stops the activity execution
        super.onStop();
        long totalMinutesTimeSpent = 0;
        long totalSecondsTimeSpent = 0;
        for (long minutesTimeValue : minutesSpentArray) {
            totalMinutesTimeSpent += minutesTimeValue;
        }
        for (long secondsTimeValue : secondsSpentArray) {
            totalSecondsTimeSpent += secondsTimeValue;
        }
        long totalMinutesSpent = totalMinutesTimeSpent - totalMinutesTimeSpent/2; //this is because when resetting the timer we perform a click on the button and make the timeSpent duplicate
        long totalSecondsSpent = totalSecondsTimeSpent - totalSecondsTimeSpent/2;
        String timeSpentToString = String.format("%02d:%02d", totalMinutesSpent, totalSecondsSpent);
        myRef.child("sessions").child(currentUser).child(sessionName).child("timeSpend").setValue(timeSpentToString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_counter_text = (TextView) findViewById(R.id.txt_work_counter);
        button_start_work = (Button) findViewById(R.id.bt_start_work);
        button_set_time = (Button) findViewById(R.id.bt_set_time);
        editText_custom_time = (EditText) findViewById(R.id.edt_custom_time);
        txt_session_title = (TextView) findViewById(R.id.txt_session_title);
        rv_task_list = (RecyclerView) findViewById(R.id.rv_task_list);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.timer_end_sound);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            sessionName = extras.getString("sessionName");

            txt_session_title.setText(sessionName);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rv_task_list.setLayoutManager(layoutManager);

            getTasksFromSession(sessionName, currentUser);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_task_list.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            rv_task_list.addItemDecoration(dividerItemDecoration);

            //------Logic of the activity------

            Matcher m = r.matcher(DEFAULT_WORK_TIME);

            if (m.find()) {
                txt_counter_text.setText(DEFAULT_WORK_TIME);
                timeLeftInMilliseconds = Long.parseLong(m.group(1)) * 60000;
            }
            button_set_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custom_time = String.valueOf(editText_custom_time.getText());
                    Matcher m = r.matcher(custom_time); // THIS SHOULD BE ON A METHOD

                    if (m.find() && !custom_time.equalsIgnoreCase("00:00")) {
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

    //------Methods------

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

            button_set_time.setVisibility(View.VISIBLE);
            editText_custom_time.setVisibility(View.VISIBLE);
        } else {
            startTimer();
            instantStart = Instant.now();
            button_set_time.setVisibility(View.INVISIBLE);
            editText_custom_time.setVisibility(View.INVISIBLE);
        }
    }

    private void stopTimer() {
        countDownTimer.cancel();
        if(isABreak) {
            button_start_work.setText("BREAK");
        }
        else {
            button_start_work.setText("WORK");
        }
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
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.timer_end_sound);
                }
                mediaPlayer.start();

                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0));

                instantEnd = Instant.now();
                long timeElapsed = Duration.between(instantStart, instantEnd).toMillis();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
                timeElapsed -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
                minutesSpentArray.add(minutes);
                secondsSpentArray.add(seconds);

                if(!isABreak){
                    Matcher m = r.matcher(DEFAULT_BREAK_TIME);

                    if (m.find()) {
                        txt_counter_text.setText(DEFAULT_BREAK_TIME);
                        timeLeftInMilliseconds = Long.parseLong(m.group(1)) * 60000;
                        button_start_work.setText("BREAK");
                        isABreak = true;
                        countDownTimer.cancel();
                        button_start_work.performClick();
                    }

                }
                else
                {
                    Matcher m = r.matcher(DEFAULT_WORK_TIME);

                    if (m.find()) {
                        txt_counter_text.setText(DEFAULT_WORK_TIME);
                        timeLeftInMilliseconds = Long.parseLong(m.group(1)) * 60000;
                        button_start_work.setText("WORK");
                        isABreak = false;
                        countDownTimer.cancel();
                        button_start_work.performClick();
                    }
                }

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

    private void getTasksFromSession(String sessionName, String currentUser){
        myRef.child("sessions").child(currentUser).child(sessionName).child("tasks").addValueEventListener(new ValueEventListener() {
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
}