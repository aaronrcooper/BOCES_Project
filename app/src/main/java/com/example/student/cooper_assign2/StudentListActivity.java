package com.example.student.cooper_assign2;
//https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class StudentListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Global variables
    protected DBHelper myDBHelper;
    private List<Student> studentsList;
    private StudentAdapter studentAdapter;
    private ArrayAdapter<Teacher> teacherAdapter;
    //Variable to store the current teacher selected in the spinner
    Teacher currentTeacher;
    //get references to all elements on the page
    //EditTexts
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtAge;
    EditText txtYear;
    //ListView
    ListView lstStudents;
    //Spinner
    Spinner teacherListSpinner;
    //ImageView
    ImageView imgStudentImage;
    List<Teacher> teacherList;
    Bitmap studentImage = null;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        //Creates the database object
        myDBHelper = new DBHelper(this);
        //get references to all elements
        //EditTexts
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtAge = (EditText)findViewById(R.id.txtAge);
        txtYear = (EditText)findViewById(R.id.txtYear);
        teacherListSpinner = (Spinner) findViewById(R.id.spinTeacherList);
        //ListView
        lstStudents = (ListView)findViewById(R.id.lstStudentsView);
        teacherListSpinner.setOnItemSelectedListener(this);
        imgStudentImage = (ImageView)findViewById(R.id.studentImage);

    }

    @Override
    //Determines what happens when the app is resumed
    protected void onResume() {
        super.onResume();
        studentsList = myDBHelper.getAllStudents();
        teacherList = myDBHelper.getAllTeachers();
        //Instantiated a studentAdapter
        studentAdapter = new StudentAdapter(this, R.layout.activity_list_item, studentsList);
        ListView listStudents = (ListView) findViewById(R.id.lstStudentsView);
        listStudents.setAdapter(studentAdapter);
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherAdapter.setDropDownViewResource(R.layout.spinner_item);
        teacherListSpinner.setAdapter(teacherAdapter);
    }

    //*****************FUNCTION TO REMOVE ALL STUDENTS FROM THE DB*************
    //Clears all students from the database
    /*public void removeAllStudents(View view){
        myDBHelper.removeAllStudents(studentsList);
        studentAdapter.notifyDataSetChanged();

    }**************************************************************************/

    //Button Click handler for add students button
    //creates a Student object and stores all
    //of the student information, then adds
    //the student object to the database
    public void addStudent(View view)
    {
        //store all of the variables from the edit texts
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String age = txtAge.getText().toString();
        String year = txtYear.getText().toString();
        int teacherId = currentTeacher.getId();


        //check that all textviews are filled
        if(firstName.isEmpty() ||
                lastName.isEmpty() ||
                age.isEmpty() ||
                year.isEmpty() )
        {
            //Toast that displays error if all fields are not entered
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if(studentImage == null)
        {
            Toast.makeText(getApplicationContext(), "An image must be selected for the student", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //create a student object with the correct attributes
            Student aStudent = new Student(firstName, lastName, Integer.parseInt(age), teacherId, year);
            //convert student image to a byte array and add to student object
            byte[] imageToStore = ImageUtils.getBytes(studentImage);
            aStudent.setStudentImage(imageToStore);
            //add the student to the database
            myDBHelper.addStudent(aStudent);
            //add the student to the list
            studentAdapter.add(aStudent);
            //update the listview
            studentAdapter.notifyDataSetChanged();
            onResume();
            //Toast to let user know that Student was added successfully
            Toast.makeText(getApplicationContext(), "Student successfully added.", Toast.LENGTH_SHORT).show();
            studentImage= null;
            imgStudentImage.setImageResource(R.mipmap.noimgavail);
            //clear all fields
            txtFirstName.setText("");
            txtLastName.setText("");
            txtAge.setText("");
            txtYear.setText("");
        }
    }

    public void editStudent(View view)
    {
        startActivity(new Intent(StudentListActivity.this, Edit_Student.class));
    }


    //removeCheckedStudents
    //removes all checked students from the database
    public void removeCheckedStudents(View v)
    {
        for(Student aStudent: studentsList)
        {
            if(aStudent.isDeletable()) {
                //myDBHelper.removeStudent(aStudent);
                //studentAdapter.remove(aStudent);
                myDBHelper.removeStudent(aStudent);
                //adapter.remove(aTeacher);
                //
                studentsList = myDBHelper.getAllStudents();
                //Instantiated an adapter
                studentAdapter = new StudentAdapter(this, R.layout.activity_list_item, studentsList);
                ListView listTeachers = (ListView) findViewById(R.id.lstStudentsView);
                listTeachers.setAdapter(studentAdapter);
                studentAdapter.notifyDataSetChanged();
            }
        }
    }

    //addPicture
    //opens a the gallery and allows a photo to be selected
    //adds the photo to the database for the selected student
    public void getImage(View view)
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
                    imgStudentImage.setImageBitmap(image);
                    //store the image in current student image
                    studentImage = image;
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentTeacher = teacherAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
            CheckBox deleteStudent = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_list_item, parent, false);
                deleteStudent = (CheckBox) convertView.findViewById(R.id.chkListItem);
                convertView.setTag(deleteStudent);
                deleteStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        Student studentToDelete = (Student) cb.getTag();
                        studentToDelete.setDeletable(cb.isChecked() == true ? true : false);
                        txtFirstName.setText(studentToDelete.getFirstName());
                        txtLastName.setText(studentToDelete.getLastName());
                        txtAge.setText(Integer.toString(studentToDelete.getAge()));
                        txtYear.setText(studentToDelete.getYear());
                        imgStudentImage.setImageBitmap(ImageUtils.getImage(studentToDelete.getStudentImage()));
                    }
                });
            } else {
                deleteStudent = (CheckBox) convertView.getTag();
            }
            Student current = studentList.get(position);
            deleteStudent.setText(current.getFirstName() + " " + current.getLastName());
            deleteStudent.setTag(current);
            return convertView;
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
}
