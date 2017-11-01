package com.example.student.cooper_assign2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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

    //***********************************
    ImageView test;

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
        //get arraylist of all students names and ids
        //populate the student spinner with values
        List<String> studentNames = new ArrayList<String>();
        for(Student aStudent: studentList)
        {
            String temp;
            temp = aStudent.getFirstName() + " " + aStudent.getLastName();
            //add the student to the student name list
            studentNames.add(temp);
        }
        studentAdapter = new ArrayAdapter<Student>(getApplicationContext(), R.layout.spinner_item, studentList);
        studentAdapter.setDropDownViewResource(R.layout.spinner_item);
        studentSpinner.setAdapter(studentAdapter);



        List<String> teacherNames = new ArrayList<String>();
        for(Teacher aTeacher: teacherList)
        {
            String temp;
            temp = aTeacher.getFirstName() + " " + aTeacher.getLastName();
            teacherNames.add(temp);
        }
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, teacherNames);
        studentAdapter.setDropDownViewResource(R.layout.spinner_item);
        teacherSpinner.setAdapter(teacherAdapter);



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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        //Gets the selected teacher object
        Teacher teacher = teacherAdapter.getItem(position);
        //Filters the student adapter by the teacher ID
        studentAdapter.getFilter().filter(Long.toString(teacher.getId()),new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {

            }
        });
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
