package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to elements on the activity
        Button btnTeachers = (Button)findViewById(R.id.btnTeachers);
        Button btnStudents = (Button)findViewById(R.id.btnStudents);


        //Button click handler for btnTeacher
        btnTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
            }
        });
    }

}
