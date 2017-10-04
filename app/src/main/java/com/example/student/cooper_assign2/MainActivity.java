package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openTeacherActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }
    public void openStudentActivity(View view) {
        startActivity(new Intent(MainActivity.this, StudentListActivity.class));
    }
}
