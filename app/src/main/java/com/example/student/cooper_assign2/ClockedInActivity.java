//Author: Chris Frye
//Date modified: 11/8/2017
//Activity that can be viewed while a student is currently clocked in for a task
package com.example.student.cooper_assign2;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ClockedInActivity extends AppCompatActivity {

    //instance variables
    private DBHelper myDBHelper;
    private Timer timer;
    MyTimerTask timerTask;
    private long startingTime;
    private long currentTime;
    //references to text views on the activity
    TextView lblTimer;
    TextView lblWelcome;
    TextView lblCurrentTask;
    TextView lblTaskDescr;
    //Timer delay affects how long between timer ticks
    final int TIMER_DELAY = 1000;
    Student currentStudent;
    Task currentTask;
    Calendar cal;
    //Datebase variables
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    String startTime;
    String finishTime;
    String date;
    static String timeSpentOnTask;
    static long elapsedMs;

    //ON CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocked_in);
        //get reference to DB
        myDBHelper = new DBHelper(this);
        //get the student that is currently clocked in from the previous activity
        Bundle data = getIntent().getExtras();
        currentStudent = (Student) data.getParcelable("student");
        currentTask = (Task) data.getParcelable("task");
        //get references to the text views
        lblTimer = (TextView)findViewById(R.id.lblTimer);
        lblWelcome = (TextView)findViewById(R.id.lblWelcome);
        lblTaskDescr = (TextView)findViewById(R.id.lblTaskDescription);
        //display a welcome message to the user
        lblWelcome.setText("Welcome " + currentStudent.getFirstName() + " " + currentStudent.getLastName());

        //get a reference to the current task label and display the current task
        lblCurrentTask = (TextView)findViewById(R.id.lblCurrentTask);
        lblCurrentTask.setText("Current Task: " + currentTask.getTaskName());
        lblTaskDescr.setText("Task Description: \n" + currentTask.getDescription());
        //get the starting time
        startingTime = SystemClock.uptimeMillis();
        //Set up string formatting in order to pass in data to database
        cal = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("MM/dd/YYYY");
        date = dateFormat.format(cal.getTime());
        final String calStart = timeFormat.format(cal.getTime());
        startTime = calStart;
        //instantiate the timer and timer task
        //start the timer
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask,TIMER_DELAY, TIMER_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //get a new instance of the calendar object
        cal = null;
        cal = Calendar.getInstance();
        //Cancels the timer if it already exists
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        final String calFinish = timeFormat.format(cal.getTime());
        finishTime = calFinish;
        //Adds the completed task to the database
        myDBHelper.addCompletedTask(currentStudent, currentTask, startTime, finishTime, date, timeSpentOnTask);
    }

    //ClockOut method
    //Method that is called when the student clocks out
    //returns the user to the main activty and adds their clock in time and clock out time in DB
    public void clockOut(View v)
    {
        //get new instance of calendar object
        cal = null;
        cal = Calendar.getInstance();
        //Cancels the timer if it already exists
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        final String calFinish = timeFormat.format(cal.getTime());
        finishTime = calFinish;
        //Adds the completed task to the database
        myDBHelper.addCompletedTask(currentStudent, currentTask, startTime, finishTime, date, timeSpentOnTask);
        ClockedInActivity.this.finish();
    }

    //MyTimerTask
    //private inner class that handles timer tick event
    //used to update the on screen timer
    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            //gets the number of milliseconds since system was booted
            currentTime = SystemClock.uptimeMillis();
            //calc the elapsed milliseconds, seconds, mins and hours
            elapsedMs = currentTime - startingTime;
            final int seconds = (int) ((elapsedMs /1000)%60);
            final int minutes = (int) ((elapsedMs /60000)%60);
            final int hours = (int) (elapsedMs / 3600000);
            //display the time to the user
            //this runs on the user interface thread
            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    timeSpentOnTask = (String.format("%02d", hours) + ":" +
                            String.format("%02d", minutes) + ":" +
                            String.format("%02d", seconds));
                    lblTimer.setText(timeSpentOnTask);
                }
            });
        }
    }
}
