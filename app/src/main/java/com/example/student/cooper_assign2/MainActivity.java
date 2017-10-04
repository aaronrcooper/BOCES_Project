package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //button click handler for the teachers button
    //this button opens the teacherlist activity
    public void moveToTeacher(){
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }
}
