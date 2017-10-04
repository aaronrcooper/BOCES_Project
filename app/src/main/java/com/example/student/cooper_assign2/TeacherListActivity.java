package com.example.student.cooper_assign2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TeacherListActivity extends AppCompatActivity {

    //references to all elements on activity
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmail;
    EditText txtPhone;

    protected DBHelper myDBHelper;
    private List<Teacher> teacherList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        //Creates the database object
        myDBHelper = new DBHelper(this);

        //get references to the elements on activity
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPhone = (EditText)findViewById(R.id.txtPhone);

    }
    //Determines what happens when the app is resumed
    protected void onResume() {
        super.onResume();
    teacherList = myDBHelper.getAllTeachers();
        //Instantiated an adapter
        adapter = new MyAdapter(this, R.layout.activity_list_item, teacherList);
        ListView listTeachers = (ListView) findViewById(R.id.lstTeachersView);
        listTeachers.setAdapter(adapter);
    }
    //Clears all teachers from the database
    public void clearTeacher(){
//        myDBHelper.removeTeacher(teacherList);
        adapter.notifyDataSetChanged();
    }





    //Button Click handler for add students button
    //creates a Student object and stores all
    //of the student information, then adds
    //the student object to the database
    public void addStudent(View view)
    {
        //store all of the variables from the edittexts
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String age = txtEmail.getText().toString();
        String year = txtPhone.getText().toString();
        int teacherId;
        try {
            teacherId = Integer.parseInt(txtTeacherId.getText().toString());
        }
        catch(Exception e)
        {
            teacherId = 0;
            //Toast.makeText(getApplicationContext(), "That shit broke", Toast.LENGTH_SHORT).show();
        }

        //check that all textviews are filled
        if(firstName.isEmpty() ||
                lastName.isEmpty() ||
                age.isEmpty() ||
                year.isEmpty() ||
                txtTeacherId.getText().toString().isEmpty())
        {
            //Toast yo
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Student aStudent = new Student(firstName, lastName, Integer.parseInt(age), teacherId, year);
            myDBHelper.addStudent(aStudent);
            adapter.add(aStudent);
            adapter.notifyDataSetChanged();
        }
    }


    //*******************ADAPTER**************************
    private class MyAdapter extends ArrayAdapter<Teacher> {
        Context context;
        List<Teacher> teacherList = new ArrayList<Teacher>();
        //Constructor
        public MyAdapter(Context c, int rId, List<Teacher> objects) {
            super(c, rId, objects);
            teacherList = objects;
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
                        Teacher changeTeacher = (Teacher) cb.getTag();
//                        changeTeacher.setIs_done(cb.isChecked() == true ? 1 : 0);
//                        myDBHelper.updateTeacher(changeTeacher);
                    }
                });
            } else {
                isDoneChBx = (CheckBox) convertView.getTag();
            }
            Teacher current = teacherList.get(position);
            isDoneChBx.setText(current.getFirstName() + " " + current.getLastName());
//            isDoneChBx.setChecked(current.getIs_done() == 1 ? true : false);
            isDoneChBx.setTag(current);
            return convertView;
        }
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
    }
}
