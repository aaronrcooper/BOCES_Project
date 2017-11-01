package com.example.student.cooper_assign2;
//https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Global variables
    protected DBHelper myDBHelper;
    private List<Student> studentsList;
    private StudentAdapter adapter;
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
    List<Teacher> teacherList;

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

    }

    @Override
    //Determines what happens when the app is resumed
    protected void onResume() {
        super.onResume();
        studentsList = myDBHelper.getAllStudents();
        teacherList = myDBHelper.getAllTeachers();
        //Instantiated an adapter
        adapter = new StudentAdapter(this, R.layout.activity_list_item, studentsList);
        ListView listStudents = (ListView) findViewById(R.id.lstStudentsView);
        listStudents.setAdapter(adapter);
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherAdapter.setDropDownViewResource(R.layout.spinner_item);
        teacherListSpinner.setAdapter(teacherAdapter);
    }
    //Clears all students from the database
    public void removeAllStudents(View view){
        myDBHelper.removeAllStudents(studentsList);
        adapter.notifyDataSetChanged();

    }

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
        else
        {
            //create a student object with the correct attributes
            Student aStudent = new Student(firstName, lastName, Integer.parseInt(age), teacherId, year);




            //add the student to the database
            myDBHelper.addStudent(aStudent);
            //add the student to the list
            adapter.add(aStudent);
            //update the listview
            adapter.notifyDataSetChanged();
            //Toast to let user know that Student was added successfully
            Toast.makeText(getApplicationContext(), "Student successfully added.", Toast.LENGTH_SHORT).show();
            //clear all fields
            txtFirstName.setText("");
            txtLastName.setText("");
            txtAge.setText("");
            txtYear.setText("");
            //TODO Diagnose error that this seems to throw
        }
    }

    //Button click handler for remove student
    //gets the student id from the selected item in the list view and
    //passes that id into remove student in dbhelper
    public void removeStudent()
    {
        Student aStudent = (Student)lstStudents.getSelectedItem();
        myDBHelper.removeStudent(aStudent.getStudentID());
        adapter.remove(aStudent);
        adapter.notifyDataSetChanged();
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
            CheckBox isDoneChBx = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_list_item, parent, false);
                isDoneChBx = (CheckBox) convertView.findViewById(R.id.chkListItem);
                convertView.setTag(isDoneChBx);

            } else {
                isDoneChBx = (CheckBox) convertView.getTag();
            }
            Student current = studentList.get(position);
            isDoneChBx.setText(current.getFirstName() + " " + current.getLastName() + " " + current.getTeacherID());
            isDoneChBx.setTag(current);
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
