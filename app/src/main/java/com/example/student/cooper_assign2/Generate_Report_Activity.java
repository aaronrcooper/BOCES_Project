package com.example.student.cooper_assign2;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.student.cooper_assign2.Adapters.StudentAdapter;
import com.example.student.cooper_assign2.Adapters.TeacherAdapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Generate_Report_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private final int REQUEST_CODE = 200;
    //Instance variables
    private Teacher currentTeacher;
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;
    private DBHelper myDBHelper;
    private Spinner teacherSpinner;
    private List<Completed_Task> tasks;
    private List<Student> studentList;
    private String path;
    private CheckBox chkAllStudents;
    private Spinner spinStudents;
    private Student currentStudent;
    private StudentAdapter studentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate__report_);
        //DB helper instance
        myDBHelper = new DBHelper(this);
        chkAllStudents = (CheckBox)findViewById(R.id.chkAllStudents);
        spinStudents = (Spinner)findViewById(R.id.spinStudentReport);
    }
    protected void onResume()
    {
        super.onResume();
        //Gets teacher spinner on reports page and sets adapter and listener
        teacherSpinner = (Spinner) findViewById(R.id.report_teacher_spinner);
        teacherList = myDBHelper.getAllTeachers();
        studentList= myDBHelper.getStudentsByTeacher(teacherList.get(0));

        teacherAdapter = new TeacherAdapter(this.getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherAdapter.notifyDataSetChanged();
        teacherSpinner.setOnItemSelectedListener(this);

        studentAdapter = new StudentAdapter(this.getApplicationContext(), R.layout.spinner_item, studentList);
        spinStudents.setAdapter(studentAdapter);
        studentAdapter.notifyDataSetChanged();
        spinStudents.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner)parent;
        //Gets the selected teacher object
        if(spinner.getId() == R.id.report_teacher_spinner) {
            currentTeacher = teacherAdapter.getItem(position);
            studentList = myDBHelper.getStudentsByTeacher(currentTeacher);
            studentAdapter = null;
            studentAdapter = new StudentAdapter(this.getApplicationContext(), R.layout.spinner_item, studentList);
            spinStudents.setAdapter(studentAdapter);
            studentAdapter.notifyDataSetChanged();
        }
        else if(spinner.getId() == R.id.spinStudentReport)
            currentStudent = studentAdapter.getItem(position);



        //currentTeacher = teacherAdapter.getItem(position);
        //tasks = null;
        //tasks = myDBHelper.getCompletedTasksByTeacher(currentTeacher);
        //Calendar object for date logging
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM:dd:yyyy_hh:mm:ss", Locale.US);
        String dateTime = format.format(cal.getTime());
        //Sets the path for the PDF report to the current teachers name_report.pdf
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ "/"
                 + currentTeacher.getFirstName() + "_" + currentTeacher.getLastName()+"_report_ " + dateTime + ".pdf" ;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //Creates a PDF File for the report
    public void createPDF(View view) throws DocumentException, FileNotFoundException
    {
        tasks = null;
        if(chkAllStudents.isChecked())
            tasks = myDBHelper.getCompletedTasksByTeacher(currentTeacher);
        else
            tasks = myDBHelper.getCompletedTasksByStudent(currentStudent);
        // Prompts the user for permission to write
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }
        }
        //Creates a file object to output to
        File file = new File(path);
        //Creates a new file if the file does not already exist
        if(!file.exists() && !tasks.isEmpty())
        {
            try
            {
                //Creates a new file
                file.createNewFile();
                //Creates new document type
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
                document.open();
                for (Completed_Task task : tasks)
                {
                    //if(task.getDate_completed() > )
                    document.add(new Paragraph("Student: " + myDBHelper.getStudent(task.getStudentID()).getFullName() + "\n" +
                            "    Task: " + task.getTaskID() + "\n " +
                            "    Date Completed: " + task.getDate_completed() + "\n" +
                            "    Time Spent on Task: " + task.getTimeSpent() + "\n"));
                }
                //Close document
                document.close();
                //display toast on success
                Toast.makeText(getApplicationContext(), "Document successfully created in " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Toast.LENGTH_SHORT).show();

            }
            catch (IOException ex)
            {
                Toast.makeText(getApplicationContext(), "An error occurred while generating the report.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if(tasks.isEmpty())
                Toast.makeText(getApplicationContext(), "There are no tasks to be reported.", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "An error occurred while generating the report.", Toast.LENGTH_LONG).show();
        }


    }

}
