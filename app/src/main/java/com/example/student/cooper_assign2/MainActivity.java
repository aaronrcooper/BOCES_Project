package com.example.student.cooper_assign2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    ImageView imgTeacher, imgStudent, imgTask;


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
        //TODO change to spinner with image
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_with_image_item, teacherList);
        teacherAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherSpinner.setOnItemSelectedListener(this);
        //Sets the adapter for the student spinner
        //TODO change value to spinner with image
        studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_with_image_item, studentList);
        studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        studentSpinner.setAdapter(studentAdapter);
        studentSpinner.setOnItemSelectedListener(this);
        //sets the adapter for the task spinner
        //TODO change value to spinner with image
        taskAdapter = new TaskAdapter(getApplicationContext(), R.layout.spinner_with_image_item, taskList);
        taskAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setOnItemSelectedListener(this);
        //get references to imageviews
        imgTeacher = (ImageView) findViewById(R.id.imgMainTeacher);
        imgStudent = (ImageView) findViewById(R.id.imgMainStudent);
        //*****************UNCOMMENT THIS WHEN ADDING TASK IMAGES**********
        //imgTask = (ImageView) findViewById(R.id.imgMainTask);

        btnClockIn = (Button) findViewById(R.id.btnClockIn);
        studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
        teacherSpinner.setAdapter(teacherAdapter);
        if (teacherList.isEmpty() || studentList.isEmpty() || taskList.isEmpty())
        {
            btnClockIn.setEnabled(false);
        }
        else
        {
            btnClockIn.setEnabled(true);
        }

    }


    //************CLICK EVENT HANDLERS*********************
    //Admin button
    //checks teacher id and takes teacher to admin view
    public void startAdminView(View view)
    {
        startActivity(new Intent(MainActivity.this, AdminView.class));
    }

    //Clock in Button
    //Takes the student to the clocked in activity
    public void startClockedInView(View view)
    {
        Intent myIntent = new Intent(MainActivity.this, ClockedInActivity.class);
        myIntent.putExtra("student", currentStudent);
        myIntent.putExtra("task", currentTask);
        startActivity(myIntent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinTeachers)
        {
            //Gets the selected teacher object
            currentTeacher = teacherAdapter.getItem(position);
            imgTeacher.setImageBitmap(ImageUtils.getImage(currentTeacher.getTeacherImage()));
            studentList = myDBHelper.getStudentsByTeacher(currentTeacher);
            //Sets the adapter to null (for garbage collection) and creates a new adapter for the new list
            studentAdapter = null;
            //TODO change values to spinner with image
            studentAdapter = new StudentAdapter(getApplicationContext(), R.layout.spinner_with_image_item, studentList);
            studentAdapter.setDropDownViewResource(R.layout.spinner_with_image_item);
            studentSpinner.setAdapter(studentAdapter);
        }
        else if (spinner.getId() == R.id.spinStudent)
        {
            currentStudent = studentAdapter.getItem(position);
            if(currentStudent.getStudentImage() != null)
                imgStudent.setImageBitmap(ImageUtils.getImage(currentStudent.getStudentImage()));
        }
        else if(spinner.getId() == R.id.spinTask)
        {
            currentTask = taskAdapter.getItem(position);
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
            ImageView studentImage = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                student = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                studentImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
                convertView.setTag(student);

            } else {
                student = (TextView) convertView.getTag();
                studentImage = (ImageView) convertView.getTag();
            }
            Student current = studentList.get(position);
            //Sets TextView properties
            student.setText(current.getFirstName() + " " + current.getLastName());
            student.setTag(current);
            //Sets imageView properties
            studentImage.setImageBitmap(ImageUtils.getImage(current.getStudentImage()));
            studentImage.setTag(current);
            //return
            return convertView;
        }
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView student = null;
            ImageView studentImage = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                student = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                studentImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
                convertView.setTag(student);

            } else {
                student = (TextView) convertView.getTag();
                studentImage = (ImageView) convertView.getTag();
            }
            //Student current = studentList.get(position);
            //Sets TextView properties
            student.setText(studentList.get(position).getFirstName() + " " + studentList.get(position).getLastName());
            //Sets imageView properties
            studentImage.setImageBitmap(ImageUtils.getImage(studentList.get(position).getStudentImage()));
            //return
            return convertView;
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
            ImageView teacherImage = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                teacher = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                teacherImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
                convertView.setTag(teacher);

            } else {
                teacher = (TextView) convertView.getTag();
                teacherImage = (ImageView) convertView.getTag();
            }
            Teacher current = teacherList.get(position);
            //Sets text
            teacher.setText(current.getFirstName() + " " + current.getLastName());
            teacher.setTag(current);
            //Sets image
            teacherImage.setImageBitmap(ImageUtils.getImage(current.getTeacherImage()));
            teacherImage.setTag(current);
            return convertView;
        }
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView teacherView =null;
            ImageView teacherImage = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                teacherView = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                convertView.setTag(teacherView);

            } else {
                teacherView = (TextView) convertView.getTag();
                teacherImage = (ImageView) convertView.getTag();
            }
            teacherView.setText(teacherList.get(position).getFirstName() + " " + teacherList.get(position).getLastName());
            teacherView.setHeight(60);
            teacherImage.setImageBitmap(ImageUtils.getImage(teacherList.get(position).getTeacherImage()));
            return convertView;
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
            ImageView taskImage = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                task = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                taskImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
                convertView.setTag(task);
            }
            else
            {
                task = (TextView) convertView.getTag();
                taskImage = (ImageView) convertView.getTag();
            }
            Task current = taskList.get(position);
            //Sets textview properties
            task.setText(current.getTaskName() + " " + current.getDescription());
            task.setTag(current);
            //sets imageview properties
            taskImage.setImageBitmap(ImageUtils.getImage(current.getTaskImage()));
            taskImage.setTag(current);
            return convertView;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            TextView task = null;
            ImageView taskImage = null;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
                task = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
                taskImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
                convertView.setTag(task);
            }
            else
            {
                task = (TextView) convertView.getTag();
                taskImage = (ImageView) convertView.getTag();
            }
            //Task current = taskList.get(position);
            //Sets textview properties
            task.setText(taskList.get(position).getTaskName() + " " + taskList.get(position).getDescription());
            //sets imageview properties
            taskImage.setImageBitmap(ImageUtils.getImage(taskList.get(position).getTaskImage()));
            return convertView;
        }
        public Task getPosition(int position)
        {
            return taskList.get(position);
        }
    }
}
