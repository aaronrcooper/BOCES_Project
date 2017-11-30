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

import java.util.ArrayList;
import java.util.List;

public class Edit_Student extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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
        myDBHelper = new DBHelper(this);
        txtFName = (EditText)findViewById(R.id.txtSFirstName);
        txtLName = (EditText)findViewById(R.id.txtSLastName);
        txtAge = (EditText)findViewById(R.id.txtSAge);
        txtYear = (EditText)findViewById(R.id.txtSYear);
        imgStudent =(ImageView)findViewById(R.id.studentImageEdit);
        lblStudentID = (TextView)findViewById(R.id.lblStudentID);
        teacherList = myDBHelper.getAllTeachers();
        studentList = myDBHelper.getAllStudents();
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
            //create a teacher object with correct attributes
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
            imgStudent.setImageBitmap(ImageUtils.getImage(currentStudent.getStudentImage()));
            studentImage = ImageUtils.getImage(currentStudent.getStudentImage());
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
                    studentImage = image;
                    imgStudent.setImageBitmap(image);
                }
            }
        }
    }


    //*******************TEACHER ADAPTER**************************
    private class TeacherAdapter extends ArrayAdapter<Teacher> {
        Context context;
        List<Teacher> teacherList = new ArrayList<Teacher>();

        //Constructor
        public TeacherAdapter(Context c, int rId, List<Teacher> objects) {
            super(c, rId, objects);
            teacherList = objects;
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView teacher = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                teacher = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(teacher);

            } else {
                teacher = (TextView) convertView.getTag();
            }
            Teacher current = teacherList.get(position);
            teacher.setText(current.getFirstName() + " " + current.getLastName());
            teacher.setTag(current);
            return convertView;
        }
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view =null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                view = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(view);

            } else {
                view = (TextView) convertView.getTag();
            }
            view.setText(teacherList.get(position).getFirstName() + " " + teacherList.get(position).getLastName());
            view.setHeight(60);

            return view;
        }
        public Teacher getPosition(int position)
        {
            return teacherList.get(position);
        }
    }

    //*******************STUDENT ADAPTER**************************
    private class StudentAdapter extends ArrayAdapter<Student> {
        Context context;
        List<Student> studentList = new ArrayList<Student>();

        //Constructor
        public StudentAdapter(Context c, int rId, List<Student> objects) {
            super(c, rId, objects);
            studentList = objects;
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView student = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                student = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(student);

            } else {
                student = (TextView) convertView.getTag();
            }
            Student current = studentList.get(position);
            student.setText(current.getFirstName() + " " + current.getLastName());
            student.setTag(current);
            return convertView;
        }
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view =null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                view = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(view);

            } else {
                view = (TextView) convertView.getTag();
            }
            view.setText(studentList.get(position).getFirstName() + " " + studentList.get(position).getLastName());
            view.setHeight(60);

            return view;
        }
        public Student getPosition(int position)
        {
            return studentList.get(position);
        }
    }
}
