package com.example.taskodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txt_counter_text;
    private Button button_start_work;

    private CountDownTimer countDownTimer;

    private long timeLeftInMilliseconds = 1500000; //25 minutos, debo poner aqui el valor del timer y hacer la division
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_counter_text = (TextView) findViewById(R.id.txt_work_counter);
        button_start_work = (Button) findViewById(R.id.bt_start_work);

        button_start_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });
    }

    public void startStop()
    {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
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
}