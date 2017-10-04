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

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    protected DBHelper myDBHelper;
    private List<Student> studentsList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        //Creates the database object
        myDBHelper = new DBHelper(this);

        //get references to all elements on the page
        EditText txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        EditText txtLastName = (EditText)findViewById(R.id.txtLastName);
        EditText txtAge = (EditText)findViewById(R.id.txtAge);
        EditText txtYear = (EditText)findViewById(R.id.txtYear);
        EditText txtTeacherId = (EditText)findViewById(R.id.txtTeacherID);
    }

    @Override
    //Determines what happens when the app is resumed
    protected void onResume() {
        super.onResume();
        studentsList = myDBHelper.getAllStudents();
        //Instantiated an adapter
        adapter = new MyAdapter(this, R.layout.activity_list_item, studentsList);
        ListView listTeachers = (ListView) findViewById(R.id.lstStudentsView);
        listTeachers.setAdapter(adapter);
    }
    //Clears all teachers from the database
    public void clearTeacher(){
//        myDBHelper.clearAll(teacherList);
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
            Student current = studentList.get(position);
            isDoneChBx.setText(current.getFirstName() + " " + current.getLastName());
//            isDoneChBx.setChecked(current.getIs_done() == 1 ? true : false);
            isDoneChBx.setTag(current);
            return convertView;
        }


        //Button Click handler for save button
        //creates a Student object and stores all
        //of the student information, then adds
        //the student object to the database
        public void addStudent()
        {
            //check that all textviews are filled

        }



//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
    }
}
