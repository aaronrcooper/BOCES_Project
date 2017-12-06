//Created By: Chris Frye
//Activity where teachers can edit students in the database
//provides capability to change values associated with specific students in the DB
package com.example.student.cooper_assign2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.cooper_assign2.Adapters.StudentAdapter;
import com.example.student.cooper_assign2.Adapters.TeacherAdapter;

import java.util.ArrayList;
import java.util.List;

public class Edit_Student extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Instance Variables
    List<Teacher> teacherList;
    List<Student> studentList;
    DBHelper myDBHelper;
    Spinner teacherSpinner;
    Spinner studentSpinner;
    ArrayAdapter<Teacher> teacherAdapter;
    ArrayAdapter<Student> studentAdapter;
    Student currentStudent;
    EditText txtFName, txtLName, txtAge, txtYear;
    TextView lblStudentID;
    ImageView imgStudent;
    Teacher currentTeacher;
    private Bitmap studentImage;
    private final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__student);
        //get a reference to the database
        myDBHelper = new DBHelper(this);
        //get references to elements on the activity
        txtFName = (EditText)findViewById(R.id.txtSFirstName);
        txtLName = (EditText)findViewById(R.id.txtSLastName);
        txtAge = (EditText)findViewById(R.id.txtSAge);
        txtYear = (EditText)findViewById(R.id.txtSYear);
        imgStudent =(ImageView)findViewById(R.id.studentImageEdit);
        lblStudentID = (TextView)findViewById(R.id.lblStudentID);
        //populate the list of teachers and students from the database
        teacherList = myDBHelper.getAllTeachers();
        studentList = myDBHelper.getAllStudents();
        //get references to the spinners
        teacherSpinner = (Spinner)findViewById(R.id.spinEditStudentsTeacher);
        studentSpinner = (Spinner)findViewById(R.id.spinEditStudent);
        //Sets the adapter for the teacher spinner
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherAdapter.setDropDownViewResource(R.layout.spinner_item);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherSpinner.setOnItemSelectedListener(this);
        //Sets the adapter for the student spinner
        studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_item, studentList);
        studentAdapter.setDropDownViewResource(R.layout.spinner_item);
        studentSpinner.setAdapter(studentAdapter);
        studentSpinner.setOnItemSelectedListener(this);
    }

    //saveStudent
    //button click handler for save button
    //updates current students values then updates database
    //using updated student object
    public void save(View view)
    {
        //store all of the variables from the edittexts
        String firstName = txtFName.getText().toString();
        String lastName = txtLName.getText().toString();
        String age = txtAge.getText().toString();
        String year = txtYear.getText().toString();
        int teacherID = currentTeacher.getId();
        //check that all textviews are filled
        if(firstName.isEmpty() ||
                lastName.isEmpty() ||
                age.isEmpty() ||
                year.isEmpty())
        {
            //Toast yo
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if (studentImage == null)
        {
            Toast.makeText(getApplicationContext(), "An image must be selected for the student", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //create a student object with correct attributes
            currentStudent.setFirstName(firstName);
            currentStudent.setLastName(lastName);
            currentStudent.setAge(Integer.parseInt(age));
            currentStudent.setYear(year);
            currentStudent.setStudentImage(ImageUtils.getBytes(studentImage));
            currentStudent.setTeacherID(teacherID);
            //add the teacher to the database
            myDBHelper.updateStudent(currentStudent);
            //Toast to let user know that Teacher was added successfully
            Toast.makeText(getApplicationContext(), "Student updated successfully added.", Toast.LENGTH_SHORT).show();
        }
    }

    //cancel
    //cancel button click event handler
    //starts the list activity
    public void cancel(View view)
    {
        startActivity(new Intent(Edit_Student.this, StudentListActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        //Determine which spinner was changed
        if(spinner.getId() == R.id.spinEditStudentsTeacher)
        {
            currentTeacher = teacherAdapter.getItem(i);
        }
        else {
            //Gets the selected student object
            currentStudent = studentAdapter.getItem(i);
            //fill in the textviews with teacher info
            txtFName.setText(currentStudent.getFirstName());
            txtLName.setText(currentStudent.getLastName());
            txtAge.setText(Integer.toString(currentStudent.getAge()));
            txtYear.setText(currentStudent.getYear());
            //display student image
            imgStudent.setImageBitmap(ImageUtils.getImage(currentStudent.getStudentImage()));
            studentImage = ImageUtils.getImage(currentStudent.getStudentImage());
            //display student id
            lblStudentID.setText("Student ID: " + Integer.toString(currentStudent.getStudentID()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //addPicture
    //opens a the gallery and allows a photo to be selected
    //adds the photo to the database for the selected student
    public void getImageEdit(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }
    //onActivityResults
    //executes when the user closes the image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode, data);
        //get the results from the image chooser activity
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_IMAGE) {
                if (data == null) {//ensure data is not null
                    Toast.makeText(getApplicationContext(), "No photo was selected.", Toast.LENGTH_SHORT).show();
                } else {
                    //get returned data and convert it to a bitmap
                    Uri imageUri = data.getData();
                    Bitmap image = ImageUtils.decodeUriToBitmap(this.getApplicationContext(), imageUri);
                    //toast to show image was successfully loaded
                    Toast.makeText(getApplicationContext(), "Photo loaded successfully", Toast.LENGTH_SHORT).show();
                    //store and display the image
                    studentImage = image;
                    imgStudent.setImageBitmap(image);
                }
            }
        }
    }
}
