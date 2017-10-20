package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to elements on the activity
        Button btnTeachers = (Button)findViewById(R.id.btnTeachers);
        Button btnStudents = (Button)findViewById(R.id.btnStudents);

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
