package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //get a reference to the spinner
    Spinner studentSpinner;
    DBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBHelper = new DBHelper(this);
    }

    protected void onResume(){
        super.onResume();
        //get list of students
        List<Student> studentList = myDBHelper.getAllStudents();
        studentSpinner = (Spinner)findViewById(R.id.spinStudent);
        //get arraylist of all students names and ids
        //populate the student spinner with values
        List<String> studentNames = new ArrayList<String>();
        for(Student aStudent: studentList)
        {
            String temp;
            temp = aStudent.getFirstName() + " " + aStudent.getLastName();
            //add the student to the student name list
            studentNames.add(temp);
        }
        ArrayAdapter<String> studentAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, studentNames);
        studentAdapter.setDropDownViewResource(R.layout.spinner_item);
        studentSpinner.setAdapter(studentAdapter);
    }
    //function to open the teacher activity
    public void startTeacherActivity(View view){
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }

    //function to open the student activity
    public void startStudentActivity(View view){
        startActivity(new Intent(MainActivity.this, StudentListActivity.class));
    }
    public void openTeacherActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }
    public void openStudentActivity(View view) {
        startActivity(new Intent(MainActivity.this, StudentListActivity.class));
    }
}
