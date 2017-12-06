//Created by Chris Frye
//Allows teachers to edit tasks in the database
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

import com.example.student.cooper_assign2.Adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class Edit_Task extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Instance Variables
    List<Task> taskList;
    TaskAdapter taskAdapter;
    Spinner taskSpinner;
    DBHelper myDBHelper;
    EditText txtTaskName, txtDescription;
    ImageView imgTask;
    Bitmap taskImage;
    Task currentTask;
    private final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__task);
        //get reference to the database
        myDBHelper = new DBHelper(this);
        //get references to elements on the page
        taskSpinner = (Spinner)findViewById(R.id.spinEditTask);
        taskList = myDBHelper.getAllTasks();//populate task list from DB
        txtTaskName = (EditText)findViewById(R.id.txtTaskName);
        txtDescription = (EditText)findViewById(R.id.txtTaskDescr);
        imgTask = (ImageView)findViewById(R.id.taskImageEdit);
        //sets the adapter for the task spinner
        taskAdapter = new TaskAdapter(getApplicationContext(), R.layout.spinner_item, taskList);
        taskAdapter.setDropDownViewResource(R.layout.spinner_item);
        taskSpinner.setAdapter(taskAdapter);
        taskSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        //Gets the selected teacher object
        currentTask = taskAdapter.getItem(i);

        //fill in the textviews with teacher info
        txtTaskName.setText(currentTask.getTaskName());
        txtDescription.setText(currentTask.getDescription());
        imgTask.setImageBitmap(ImageUtils.getImage(currentTask.getTaskImage()));
        taskImage = ImageUtils.getImage(currentTask.getTaskImage());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    //save
    //save button click event handler
    //gets the values from the text boxes,
    //stores them in the current teacher and updates DB
    public void save(View view)
    {
        //store all of the variables from the edittexts
        String taskName = txtTaskName.getText().toString();
        String descr = txtDescription.getText().toString();
        //check that all textviews are filled
        if(taskName.isEmpty() ||
                descr.isEmpty())
        {
            //Toast yo
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if (taskImage == null)
        {
            Toast.makeText(getApplicationContext(), "An image must be selected for the teacher", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //create a teacher object with correct attributes
            currentTask.setTaskName(taskName);
            currentTask.setDescription(descr);
            currentTask.setTaskImage(ImageUtils.getBytes(taskImage));
            //add the teacher to the database
            myDBHelper.updateTask(currentTask);
            //Toast to let user know that Task was added successfully
            Toast.makeText(getApplicationContext(), "Teacher updated successfully added.", Toast.LENGTH_SHORT).show();
        }
    }

    //cancel
    //cancel button click event handler
    //starts the list activity
    public void cancel(View view)
    {
        startActivity(new Intent(Edit_Task.this, TaskListActivity.class));
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
                    taskImage = image;
                    imgTask.setImageBitmap(image);
                }
            }
        }
    }
}
