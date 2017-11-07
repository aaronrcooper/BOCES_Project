package com.example.student.cooper_assign2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    ListView lstTaskView;
    private MyAdapter adapter;
    DBHelper myDBHelper;
    private List<Task> taskList;
    EditText txtTaskName;
    EditText txtTaskDescr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        myDBHelper = new DBHelper(this);

        txtTaskName = (EditText)findViewById(R.id.txtTaskName);
        txtTaskDescr = (EditText)findViewById(R.id.txtTaskDescr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList = myDBHelper.getAllTasks();
        adapter = new MyAdapter(this, R.layout.activity_list_item, taskList);
        lstTaskView = (ListView) findViewById(R.id.lstTasks);
        lstTaskView.setAdapter(adapter);
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
        else
        {
            //create a task object with correct attributes
            Task aTask = new Task(taskName, taskDescr);
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
        }
    }

    //method to remove tasks
    //TODO change this from remove all tasks to remove 1 task
    public void removeTasks(View view)
    {
        myDBHelper.removeAllTasks(taskList);
        adapter.notifyDataSetChanged();
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
