package com.example.student.cooper_assign2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    protected DBHelper myDBHelper;
    private List<Student> studentsList;
    private MyAdapter adapter;

    //get references to all elements on the page
    //EditTexts
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtAge;
    EditText txtYear;
    EditText txtTeacherId;
    //ListView
    ListView lstStudents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        //Creates the database object
        myDBHelper = new DBHelper(this);

        //get references to all elements
        //EditTexts
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtAge = (EditText)findViewById(R.id.txtAge);
        txtYear = (EditText)findViewById(R.id.txtYear);
        txtTeacherId = (EditText)findViewById(R.id.txtTeacherID);
        //ListView
        lstStudents = (ListView)findViewById(R.id.lstStudentsView);

    }

    @Override
    //Determines what happens when the app is resumed
    protected void onResume() {
        super.onResume();
        studentsList = myDBHelper.getAllStudents();
        //Instantiated an adapter
        adapter = new MyAdapter(this, R.layout.activity_list_item, studentsList);
        ListView listStudents = (ListView) findViewById(R.id.lstStudentsView);
        listStudents.setAdapter(adapter);
    }
    //Clears all students from the database
    public void removeAllStudents(View view){
        myDBHelper.removeAllStudents(studentsList);
        adapter.notifyDataSetChanged();
    }

    //Button Click handler for add students button
    //creates a Student object and stores all
    //of the student information, then adds
    //the student object to the database
    public void addStudent(View view)
    {
        //store all of the variables from the edit texts
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String age = txtAge.getText().toString();
        String year = txtYear.getText().toString();
        String teacherId = txtTeacherId.getText().toString();


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
            Student aStudent = new Student(firstName, lastName, Integer.parseInt(age), Integer.parseInt(teacherId), year);
            myDBHelper.addStudent(aStudent);
            adapter.add(aStudent);
            adapter.notifyDataSetChanged();
        }
    }

    //Button click handler for remove student
    //gets the student id from the selected item in the list view and
    //passes that id into remove student in dbhelper
    public void removeStudent()
    {
        Student aStudent = (Student)lstStudents.getSelectedItem();
        myDBHelper.removeStudent(aStudent.getStudentID());
        adapter.remove(aStudent);
        adapter.notifyDataSetChanged();
    }



    //*******************ADAPTER**************************
    private class MyAdapter extends ArrayAdapter<Student> {
        Context context;
        List<Student> studentList = new ArrayList<Student>();
        //Constructor
        public MyAdapter(Context c, int rId, List<Student> objects) {
            super(c, rId, objects);
            studentList = objects;
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
                /*isDoneChBx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        Student changeStudent = (Student) cb.getTag();
//                        changeTeacher.setIs_done(cb.isChecked() == true ? 1 : 0);
//                        myDBHelper.updateTeacher(changeTeacher);
                    }
                });*/
            } else {
                isDoneChBx = (CheckBox) convertView.getTag();
            }
            Student current = studentList.get(position);
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
