//Created By: Chris Frye and Aaron Cooper
//Main Activity -- First activity that is encountered when the app is loaded
//Allows students to clock in to tasks and contains a teacher login field to move teacher
//to the admin view activity
package com.example.student.cooper_assign2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.student.cooper_assign2.Adapters.StudentAdapter;
import com.example.student.cooper_assign2.Adapters.TaskAdapter;
import com.example.student.cooper_assign2.Adapters.TeacherAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Instance variables
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
        setContentView(R.layout.activity_main);
        //get a reference to the database
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
        //set the student and teacher adapters
        studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        teacherSpinner.setAdapter(teacherAdapter);
        //disable the clock in button if one of the lists is empty
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
        //variable that represents the teacher that is trying to log in
        Teacher logTeacher = null;
        //ERROR CHECKING
        //if the user does not fill in a specific field
        if(teacherEmail.isEmpty() || teacherIDString.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "All login fields must be filled in", Toast.LENGTH_SHORT).show();
        }
        else{   //parse the teacher id
            teacherID = Integer.parseInt(teacherIDString);
        }
        //valid login will be set to false if one of the conditions of a valid login
        //is not met
        boolean validLogin = true;

        if(teacherID!= -1) { //if the teacher id was successfully parsed
            //get the teacher based on the id
            logTeacher = myDBHelper.getTeacher(teacherID);
        }
        if(logTeacher == null)//if the teacher did not exist in the database
        {
            Toast.makeText(getApplicationContext(), "An invalid teacher id was entered", Toast.LENGTH_SHORT).show();
            validLogin = false;
        }
        //check if the entered email matches the email in the DB
        else if(!logTeacher.getEmail().equalsIgnoreCase(teacherEmail))
        {
            Toast.makeText(getApplicationContext(), "Email and Teacher ID do not match", Toast.LENGTH_SHORT).show();
            validLogin = false;
        }
        //if none of the conditions for an invalid login were met,
        //proceed to the administration view
        if(validLogin)
        {
            startAdminView(logTeacher);
        }
    }


    //Admin button
    //checks teacher id and takes teacher to admin view
    public void startAdminView(Teacher logTeacher) {
        //create an intent
        Intent myIntent = new Intent(MainActivity.this, AdminView.class);
        //add the current teacher to the intent
        myIntent.putExtra("teacher", logTeacher);
        //start the activity
        startActivity(myIntent);
    }

    //************CLICK EVENT HANDLERS*********************
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
        //determine which spinner was changed
        if (spinner.getId() == R.id.spinTeachers)
        {
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




