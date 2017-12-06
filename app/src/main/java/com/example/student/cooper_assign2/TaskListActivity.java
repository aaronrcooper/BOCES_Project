//Created by Chris Frye and Aaron Cooper
//Allows admins to edit, remove, and add tasks
package com.example.student.cooper_assign2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    //Instance variables
    ListView lstTaskView;
    private MyAdapter adapter;
    DBHelper myDBHelper;
    private List<Task> taskList;
    EditText txtTaskName;
    EditText txtTaskDescr;
    Button btnEdit;
    public static final int PICK_IMAGE = 1;
    ImageView imgTaskImage;
    Bitmap taskImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        //get a reference to the DB
        myDBHelper = new DBHelper(this);
        //get references to views and buttons
        txtTaskName = (EditText)findViewById(R.id.txtTaskName);
        txtTaskDescr = (EditText)findViewById(R.id.txtTaskDescr);
        imgTaskImage = (ImageView)findViewById(R.id.taskImage);
        btnEdit = (Button)findViewById(R.id.btnEditTask);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //populate the task list from DB
        taskList = myDBHelper.getAllTasks();
        //check if task list is empty, if true disable edit button
        if(taskList.isEmpty())
            btnEdit.setEnabled(false);
        //set the task adapter
        adapter = new MyAdapter(this, R.layout.activity_list_item, taskList);
        lstTaskView = (ListView) findViewById(R.id.lstTasks);
        lstTaskView.setAdapter(adapter);
    }

    //editTask
    //button click for edit
    //opens edit activity
    public void editTask(View view)
    {
        startActivity(new Intent(TaskListActivity.this, Edit_Task.class));
    }

    //method to add a task to the database
    public void addTask(View view)
    {
        //store all of the variables from the edittexts
        String taskName = txtTaskName.getText().toString();
        String taskDescr = txtTaskDescr.getText().toString();

        //check that all textviews are filled
        if(taskName.isEmpty() || taskDescr.isEmpty())
        {
            //Toast yo
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if(taskImage == null)
        {
            Toast.makeText(getApplicationContext(), "An image must be selected for the task", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //create a task object with correct attributes
            Task aTask = new Task(taskName, taskDescr);
            byte[] imageToStore = ImageUtils.getBytes(taskImage);
            aTask.setTaskImage(imageToStore);
            //add the teacher to the database
            myDBHelper.addTask(aTask);
            //update listview of teachers
            adapter.add(aTask);
            adapter.notifyDataSetChanged();
            //Toast to let user know that Teacher was added successfully
            Toast.makeText(getApplicationContext(), "Task successfully added.", Toast.LENGTH_SHORT).show();
            //clear all fields
            txtTaskName.setText("");
            txtTaskDescr.setText("");
            imgTaskImage.setImageResource(R.mipmap.noimgavail);
            //enable the edit button
            btnEdit.setEnabled(true);
        }
    }

    //method to remove tasks
    public void removeTasks(View view)
    {
        for(Task aTask: taskList)
        {
            if(aTask.isDeletable())
            {
                myDBHelper.removeTask(aTask);
                //adapter.remove(aTeacher);
                //
                taskList = myDBHelper.getAllTasks();
            }
        }
        //Instantiated an adapter
        adapter = new TaskListActivity.MyAdapter(this, R.layout.activity_list_item, taskList);
        ListView listTasks = (ListView) findViewById(R.id.lstTasks);
        listTasks.setAdapter(adapter);
        if(taskList.isEmpty())
            btnEdit.setEnabled(false);
        imgTaskImage.setImageResource(R.mipmap.noimgavail);
        //clear all fields
        txtTaskDescr.setText("");
        txtTaskName.setText("");
        adapter.notifyDataSetChanged();
        if(taskList.isEmpty())
            btnEdit.setEnabled(false);
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
    //After the user returns from selecting an image, this function
    //is executed
    //the image is the set to the current task
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
                    imgTaskImage.setImageBitmap(image);
                    //store the image in current student image
                    taskImage = image;
                }
            }
        }
    }

    //*******************ADAPTER**************************
    private class MyAdapter extends ArrayAdapter<Task> {
        Context context;
        List<Task> taskList = new ArrayList<Task>();
        //Constructor
        public MyAdapter(Context c, int rId, List<Task> objects) {
            super(c, rId, objects);
            taskList = objects;
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            CheckBox isDoneChBx = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_list_item, parent, false);
                isDoneChBx = (CheckBox) convertView.findViewById(R.id.chkListItem);
                convertView.setTag(isDoneChBx);
                isDoneChBx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        Task changeTask = (Task) cb.getTag();
                        changeTask.setDeletable(true);
                        txtTaskName.setText(changeTask.getTaskName());
                        txtTaskDescr.setText(changeTask.getDescription());
                    }
                });
            } else {
                isDoneChBx = (CheckBox) convertView.getTag();
            }
            Task current = taskList.get(position);
            isDoneChBx.setText(current.getTaskName() + " " + current.getDescription());
//            isDoneChBx.setChecked(current.getIs_done() == 1 ? true : false);
            isDoneChBx.setTag(current);
            return convertView;
        }
    }
}
