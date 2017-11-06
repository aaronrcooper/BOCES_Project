package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
    }

    //function to open the teacher activity
    public void startTeacherActivity(View view){
        startActivity(new Intent(AdminView.this, TeacherListActivity.class));
    }

    //function to open the student activity
    public void startStudentActivity(View view){
        startActivity(new Intent(AdminView.this, StudentListActivity.class));
    }
}
