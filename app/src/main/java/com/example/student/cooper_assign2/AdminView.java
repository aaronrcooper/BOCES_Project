//AdminView
//Serves as a hub for teachers. Teachers can navigate to pages where they can edit, update, or
//delete tasks, students, and teachers. Teachers can also navigate to an activity to generate a
//report
//Created by Chris Frye on 11/20/2017
package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AdminView extends AppCompatActivity {
    //instance variables
    Teacher loggedTeacher; //Reference to the teacher whose ID was entered on the sign in page
    TextView lblWelcome;
    //ONCREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        //get teacher that logged in from previous activity
        Bundle data = getIntent().getExtras();
        loggedTeacher = (Teacher) data.getParcelable("teacher");
        //set teacher name on welcome message
        lblWelcome = (TextView)findViewById(R.id.lblWelcomeTeacher);
        lblWelcome.setText("Welcome " + loggedTeacher.getFirstName() + "!");
    }

    //ONRESUME
    @Override
    protected void onResume() {
        super.onResume();
        //get references to button and DB
        Button btnStudents = (Button)findViewById(R.id.btnEditStudents);
        DBHelper myDBHelper = new DBHelper(this);
        //populate the list of teachers currently in the DB
        List<Teacher> teacherList = myDBHelper.getAllTeachers();
        // if there are no teachers currently in the DB, disable the add student button
        if(teacherList.isEmpty())
        {
            btnStudents.setEnabled(false);
        }
        else
        {
            btnStudents.setEnabled(true);
        }
    }

    //function to open the teacher activity
    public void startTeacherActivity(View view){
        startActivity(new Intent(AdminView.this, TeacherListActivity.class));
    }

    //function to open the student activity
    public void startStudentActivity(View view){
        startActivity(new Intent(AdminView.this, StudentListActivity.class));
    }

    //function to open the task activity
    public  void startTaskActivity(View view){
        startActivity(new Intent(AdminView.this, TaskListActivity.class));
    }
    //function to open the generate report activity
    public void startGenerateReportActivity(View view)
    {
        startActivity(new Intent(AdminView.this, Generate_Report_Activity.class));
    }
}
