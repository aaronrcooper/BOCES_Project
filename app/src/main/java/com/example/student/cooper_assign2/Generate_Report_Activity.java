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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
        //Calendar object for date logging
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM:dd:yyyy_hh:mm", Locale.US);
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
                //Creates a PDF table
                PdfPTable table = new PdfPTable(4);
                //Sets the column headers
                table.addCell(createCell("Student", 1, 1, Element.ALIGN_CENTER));
                table.addCell(createCell("Task", 1, 1, Element.ALIGN_CENTER));
                table.addCell(createCell("Date Completed", 1, 1, Element.ALIGN_CENTER));
                table.addCell(createCell("Time", 1, 1, Element.ALIGN_CENTER));
                for (Completed_Task task : tasks)
                {
                    table.addCell(createCell(myDBHelper.getStudent(task.getStudentID()).getFullName(), 1, 1, Element.ALIGN_CENTER));
                    table.addCell(createCell(myDBHelper.getTask(task.getTaskID()).getTaskName(), 1, 1, Element.ALIGN_CENTER));
                    table.addCell(createCell(task.getDate_completed(), 1, 1, Element.ALIGN_CENTER));
                    table.addCell(createCell(task.getTimeSpent(), 1, 1, Element.ALIGN_CENTER));
                }
                document.add(table);
                //Close document
                document.close();
                //display toast on success
                Toast.makeText(getApplicationContext(), "Document successfully created in " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Toast.LENGTH_SHORT).show();

            }
            catch (IOException ex)
            {
                displayToastError();
            }
            catch (IllegalStateException exc)
            {
                displayToastError();
            }
        }
        else
        {
            displayToastError();
        }
    }

    public void displayToastError()
    {
        Toast.makeText(getApplicationContext(), "An error occurred while generating the report.", Toast.LENGTH_LONG).show();
    }


    //Method to create table cells in a PDF
    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
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
