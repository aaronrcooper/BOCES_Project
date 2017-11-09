package com.example.student.cooper_assign2;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ClockedInActivity extends AppCompatActivity {

    //timer
    private Timer timer;
    MyTimerTask timerTask;
    private long startingTime;
    private long currentTime;
    TextView lblTimer;
    TextView lblWelcome;
    TextView lblCurrentTask;
    final int TIMER_DELAY = 1000;
    Student currentStudent;
    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocked_in);

        //get the student
        Bundle data = getIntent().getExtras();
        currentStudent = (Student) data.getParcelable("student");
        currentTask = (Task) data.getParcelable("task");
        //get a reference to the timer label
        lblTimer = (TextView)findViewById(R.id.lblTimer);
        lblWelcome = (TextView)findViewById(R.id.lblWelcome);
        lblWelcome.setText("Welcome " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        lblCurrentTask = (TextView)findViewById(R.id.lblCurrentTask);
        lblCurrentTask.setText("Current Task: " + currentTask.getTaskName());
        //get the starting time
        startingTime = SystemClock.uptimeMillis();

        //instantiate the timer and timer task
        //start the timer
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask,TIMER_DELAY, TIMER_DELAY);
    }
    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            //gets the number of milliseconds since system was booted
            currentTime = SystemClock.uptimeMillis();
            //calc the elapsed milliseconds, seconds, mins and hours
            long elapsedMs = currentTime - startingTime;
            final int seconds = (int) (elapsedMs /1000);
            final int minutes = (int) (seconds /60);
            final int hours = (int) (minutes / 60);
            //display the time to the user

            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    lblTimer.setText(String.format("%02d", hours) + ":" +
                            String.format("%02d", minutes) + ":" +
                            String.format("%02d", seconds));
                }
            });

        }
    }
}
