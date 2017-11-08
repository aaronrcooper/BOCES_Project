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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //get a reference to the spinner
    Spinner studentSpinner;
    Spinner teacherSpinner;
    Spinner taskSpinner;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDBHelper = new DBHelper(this);
    }

    protected void onResume(){
        super.onResume();
        //get list of students, teachers, and tasks
        studentList = myDBHelper.getAllStudents();
        teacherList = myDBHelper.getAllTeachers();
        taskList = myDBHelper.getAllTasks();
        //get references to all spinners
        studentSpinner = (Spinner)findViewById(R.id.spinStudent);
        teacherSpinner = (Spinner)findViewById(R.id.spinTeachers);
        taskSpinner = (Spinner)findViewById(R.id.spinTask);
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
        //sets the adapter for the task spinner
        taskAdapter = new TaskAdapter(getApplicationContext(), R.layout.spinner_item, taskList);
        taskAdapter.setDropDownViewResource(R.layout.spinner_item);
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setOnItemSelectedListener(this);


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


    //************CLICK EVENT HANDLERS*********************
    //Clock in button
    //TODO add clock in button functionality


    //Admin button
    //checks teacher id and takes teacher to admin view
    public void startAdminView(View view)
    {
        startActivity(new Intent(MainActivity.this, AdminView.class));
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
        }
        else if (spinner.getId() == R.id.spinStudent)
        {
            currentStudent = studentAdapter.getItem(position);
        }
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



    //**************TASK ADAPTER*********************************
    private class TaskAdapter extends ArrayAdapter<Task> {
        Context context;
        List<Task> taskList = new ArrayList<Task>();

        //Constructor
        public TaskAdapter(Context c, int rId, List<Task> objects)
        {
            super(c, rId, objects);
            taskList = objects;
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            TextView task = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                task = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(task);
            }
            else
            {
                task = (TextView) convertView.getTag();
            }
            Task current = taskList.get(position);
            task.setText(current.getTaskName() + " " + current.getDescription());
            task.setTag(current);
            return convertView;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            TextView view = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                view = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(view);
            }
            else
            {
                view = (TextView) convertView.getTag();
            }
            view.setText(taskList.get(position).getTaskName() + " " + taskList.get(position).getDescription());
            view.setHeight(60);
            return view;
        }
        public Task getPosition(int position)
        {
            return taskList.get(position);
        }
    }
}
