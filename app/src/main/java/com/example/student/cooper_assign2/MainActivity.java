package com.example.student.cooper_assign2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.cooper_assign2.Adapters.StudentAdapter;
import com.example.student.cooper_assign2.Adapters.TaskAdapter;
import com.example.student.cooper_assign2.Adapters.TeacherAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //get a reference to the spinner
    Spinner studentSpinner;
    Spinner teacherSpinner;
    Spinner taskSpinner;
    Button btnClockIn;
    DBHelper myDBHelper;
    ArrayAdapter<Teacher> teacherAdapter;
    ArrayAdapter<Student> studentAdapter;
    ArrayAdapter<Task> taskAdapter;
    List<Student> studentList;
    List<Teacher> teacherList;
    List<Task> taskList;
    Teacher currentTeacher;
    Student currentStudent;
    Task currentTask;
    EditText txtTeacherEmail, txtTeacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hides action bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        myDBHelper = new DBHelper(this);

    }

    protected void onResume() {
        super.onResume();
        //get list of students, teachers, and tasks
        studentList = myDBHelper.getAllStudents();
        teacherList = myDBHelper.getAllTeachers();
        taskList = myDBHelper.getAllTasks();
        //get references to all spinners
        studentSpinner = (Spinner) findViewById(R.id.spinStudent);
        teacherSpinner = (Spinner) findViewById(R.id.spinTeachers);
        taskSpinner = (Spinner) findViewById(R.id.spinTask);
        //Sets the adapter for the teacher spinner
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_with_image_item, teacherList);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherSpinner.setOnItemSelectedListener(this);
        //Sets the adapter for the student spinner
        studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_with_image_item, studentList);
        studentSpinner.setAdapter(studentAdapter);
        studentSpinner.setOnItemSelectedListener(this);
        //sets the adapter for the task spinner
        taskAdapter = new TaskAdapter(getApplicationContext(), R.layout.spinner_with_image_item, taskList);
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setOnItemSelectedListener(this);
        //get refs to login text fields
        txtTeacherEmail = (EditText)findViewById(R.id.txtTeacherEmailLog);
        txtTeacherID = (EditText)findViewById(R.id.txtTeacherID);
        //clear login text
        txtTeacherEmail.setText("");
        txtTeacherID.setText("");
        btnClockIn = (Button) findViewById(R.id.btnClockIn);
        studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        teacherSpinner.setAdapter(teacherAdapter);
        if (teacherList.isEmpty() || studentList.isEmpty() || taskList.isEmpty()) {
            btnClockIn.setEnabled(false);
        } else {
            btnClockIn.setEnabled(true);
        }
    }


    //handleLogin
    //click event handler for the admin login button
    public void handleLogin(View view)
    {
        //get the entered teacher id and email
        String teacherEmail = txtTeacherEmail.getText().toString();
        String teacherIDString = txtTeacherID.getText().toString();
        int teacherID =-1;
        Teacher logTeacher = null;
        if(teacherEmail.isEmpty() || teacherIDString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "All login fields must be filled in", Toast.LENGTH_SHORT).show();
        }
        else{
            teacherID = Integer.parseInt(teacherIDString);
        }
        boolean validLogin = true;
        if(teacherID!= -1) {
            //get the teacher based on the id
            logTeacher = myDBHelper.getTeacher(teacherID);
        }
        if(logTeacher == null)
        {
            Toast.makeText(getApplicationContext(), "An invalid teacher id was entered", Toast.LENGTH_SHORT).show();
            validLogin = false;
        }
        else if(!logTeacher.getEmail().equalsIgnoreCase(teacherEmail))
        {
            Toast.makeText(getApplicationContext(), "Email and Teacher ID do not match", Toast.LENGTH_SHORT).show();
            validLogin = false;
        }

        if(teacherEmail.equalsIgnoreCase("Admin") && teacherID == 99999)
        {
            validLogin = true;
            logTeacher = new Teacher("Admin", "ln", "1", "1");
            logTeacher.setTeacherImage(null);
            logTeacher.setDeletable(false);
            logTeacher.setId(99999);
        }
        if(validLogin)
        {
            startAdminView(logTeacher);
        }
    }

    //************CLICK EVENT HANDLERS*********************
    //Admin button
    //checks teacher id and takes teacher to admin view
    public void startAdminView(Teacher logTeacher) {
        Intent myIntent = new Intent(MainActivity.this, AdminView.class);
        myIntent.putExtra("teacher", logTeacher);
        startActivity(myIntent);
    }

    //Clock in Button
    //Takes the student to the clocked in activity
    public void startClockedInView(View view) {
        Intent myIntent = new Intent(MainActivity.this, ClockedInActivity.class);
        myIntent.putExtra("student", currentStudent);
        myIntent.putExtra("task", currentTask);
        startActivity(myIntent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinTeachers) {
            //Gets the selected teacher object
            currentTeacher = teacherAdapter.getItem(position);
            studentList = myDBHelper.getStudentsByTeacher(currentTeacher);
            //Sets the adapter to null (for garbage collection) and creates a new adapter for the new list
            studentAdapter = null;
            studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_with_image_item, studentList);
            studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
            studentSpinner.setAdapter(studentAdapter);
        } else if (spinner.getId() == R.id.spinStudent) {
            currentStudent = studentAdapter.getItem(position);
        } else if (spinner.getId() == R.id.spinTask) {
            currentTask = taskAdapter.getItem(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}




