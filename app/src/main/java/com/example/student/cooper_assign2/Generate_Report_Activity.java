package com.example.student.cooper_assign2;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Generate_Report_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Instance variables
    private Teacher currentTeacher;
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;
    private DBHelper myDBHelper;
    private Spinner teacherSpinner;
    private List<Completed_Task> tasks;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate__report_);
        //DB helper instance
        myDBHelper = new DBHelper(this);
    }
    protected void onResume()
    {
        super.onResume();
        //Gets teacher spinner on reports page and sets adapter and listener
        teacherSpinner = (Spinner) findViewById(R.id.report_teacher_spinner);
        teacherList = myDBHelper.getAllTeachers();
        teacherAdapter = new TeacherAdapter(this.getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherAdapter.notifyDataSetChanged();
        teacherSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        //Gets the selected teacher object
        currentTeacher = teacherAdapter.getItem(position);
        tasks = null;
        tasks = myDBHelper.getCompletedTasksByTeacher(currentTeacher);
        //Sets the path for the PDF report to the current teachers name_report.pdf
        //path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        path = getApplicationContext().getFilesDir().getPath() + "/"
                + currentTeacher.getFirstName() + "_" + currentTeacher.getLastName()+"_report.pdf";
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createPDF(View view) throws DocumentException, FileNotFoundException
    {
        //String[] permissions = {"android.permissions.WRITE_EXTERNAL_STORAGE"};
        //requestPermissions(permissions, 200);
        //Creates a file object to output to
        File file = new File(path);
        //File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException ex)
            {
                Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
            }
        }
        //Sets font type
        Font fontType = new Font(Font.FontFamily.TIMES_ROMAN, 14);
        Document document = new Document();
        try
        {
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "File path not found", Toast.LENGTH_SHORT).show();
        }
        catch (DocumentException ex)
        {
            Toast.makeText(getApplicationContext(), "Unable to create document", Toast.LENGTH_SHORT).show();
        }
        document.open();
        //Loops through tasks list and outputs data from each completed task into document
        for(Completed_Task task : tasks)
        {
            try
            {
                document.add(new Paragraph("Student: " + task.getStudentID() + "\n" +
                    "Task: " + task.getTaskID() + "\n " +
                    "Date Completed: " + task.getDate_completed() + "\n" +
                    "Time Spent on Task: " + task.getTimeSpent() + "\n"));
            }
            catch (DocumentException ex)
            {
                Toast.makeText(getApplicationContext(), "An error occurred while generating your document", Toast.LENGTH_SHORT);
            }
        }
        document.close();
        Toast.makeText(getApplicationContext(), "Document successfully created!", Toast.LENGTH_SHORT);
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
            //if view is null, create the view
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner_item, parent, false);
                teacher = (TextView) convertView.findViewById(R.id.spinnerItem);
                convertView.setTag(teacher);

            }
            //Sets the view if not null
            else {
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
