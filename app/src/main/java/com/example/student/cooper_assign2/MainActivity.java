package com.example.student.cooper_assign2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //get a reference to the spinner
    Spinner studentSpinner;
    Spinner teacherSpinner;
    DBHelper myDBHelper;
    ArrayAdapter<Teacher> teacherAdapter;
    ArrayAdapter<Student> studentAdapter;
    List<Student> studentList;
    List<Teacher> teacherList;
    Teacher currentTeacher;
    Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBHelper = new DBHelper(this);
    }

    protected void onResume(){
        super.onResume();
        //get list of students
        studentList = myDBHelper.getAllStudents();
        teacherList = myDBHelper.getAllTeachers();
        studentSpinner = (Spinner)findViewById(R.id.spinStudent);
        teacherSpinner = (Spinner)findViewById(R.id.spinTeachers);
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
    //function to open the teacher activity
    public void startTeacherActivity(View view){
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }

    //function to open the student activity
    public void startStudentActivity(View view){
        startActivity(new Intent(MainActivity.this, StudentListActivity.class));
    }
    public void openTeacherActivity(View view)
    {
        startActivity(new Intent(MainActivity.this, TeacherListActivity.class));
    }
    public void openStudentActivity(View view) {
        startActivity(new Intent(MainActivity.this, StudentListActivity.class));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinTeachers)
        {
            //Gets the selected teacher object
            currentTeacher = teacherAdapter.getItem(position);
            studentList = myDBHelper.getStudentsByTeacher(currentTeacher);
            //Sets the adapter to null (for garbage collection) and creates a new adapter for the new list
            studentAdapter = null;
            studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_item, studentList);
            studentAdapter.setDropDownViewResource(R.layout.spinner_item);
            studentSpinner.setAdapter(studentAdapter);
            //If student list is populated, sets the current student to the first student for the selected teacher
            if (studentList.size() != 0)
            {
                currentStudent = studentList.get(0);
            }
        }

        //Determines what happens when the student is selected from a spinner
        else if (spinner.getId() == R.id.spinStudent)
        {
            //Sets currentStudent to the selected student
            currentStudent = studentAdapter.getItem(position);
        }
        //Updates the spinner
        studentAdapter.notifyDataSetChanged();
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
