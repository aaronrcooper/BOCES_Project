package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class AdminView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if there are no teachers, disable the add student button
        Button btnStudents = (Button)findViewById(R.id.btnEditStudents);
        DBHelper myDBHelper = new DBHelper(this);
        List<Teacher> teacherList = myDBHelper.getAllTeachers();
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

    public void startGenerateReportActivity(View view)
    {
        startActivity(new Intent(AdminView.this, Generate_Report_Activity.class));
    }
}
