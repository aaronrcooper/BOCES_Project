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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView imgTeacherImage;
    protected DBHelper myDBHelper;
    private List<Teacher> teacherList;
    private MyAdapter adapter;
    private Bitmap teacherImage;
    public static final int PICK_IMAGE = 1;

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
        imgTeacherImage = (ImageView)findViewById(R.id.teacherImage);

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

    //************FUNCTION TO REMOVE ALL TEACHERS FROM THE DB*************
    //Clears all teachers from the database
    /*public void removeAllTeachers(View view){
        //prompt the user to confirm that he or she wants to remove all teachers
        myDBHelper.removeAllTeachers(teacherList);
        adapter.notifyDataSetChanged();
    }*********************************************************************/

    //Clear a single teacher from the database
    /*public void removeTeacher(Teacher aTeacher){
        myDBHelper.removeTeacher(aTeacher);
        adapter.remove(aTeacher);
        adapter.notifyDataSetChanged();
    }*/

    //removeCheckedTeachers
    public void removeCheckedTeachers(View view)
    {
        for(Teacher aTeacher: teacherList)
        {
            if(aTeacher.isDeletable())
            {
                myDBHelper.removeTeacher(aTeacher);
                //adapter.remove(aTeacher);
                //
                teacherList = myDBHelper.getAllTeachers();
            }
        }
        //Instantiated an adapter
        adapter = new MyAdapter(this, R.layout.activity_list_item, teacherList);
        ListView listTeachers = (ListView) findViewById(R.id.lstTeachersView);
        listTeachers.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

    }

    public void editTeacher(View view)
    {

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
                    imgTeacherImage.setImageBitmap(image);
                    //store the image in current student image
                    teacherImage = image;
                }
            }
        }
    }



    //Button Click handler for add students button
    //creates a Student object and stores all
    //of the student information, then adds
    //the student object to the database
    public void addTeacher(View view)
    {
        //store all of the variables from the edittexts
        String firstName = txtFirstName.getText().toString();
        String lastName = txtLastName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();

        //check that all textviews are filled
        if(firstName.isEmpty() ||
                lastName.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty())
        {
            //Toast yo
            Toast.makeText(getApplicationContext(), "All fields must be filled in.", Toast.LENGTH_SHORT).show();
        }
        else if (teacherImage == null)
        {
            Toast.makeText(getApplicationContext(), "An image must be selected for the teacher", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //create a teacher object with correct attributes
            Teacher aTeacher = new Teacher(firstName, lastName, email, phone);
            aTeacher.setTeacherImage(ImageUtils.getBytes(teacherImage));
            //add the teacher to the database
            myDBHelper.addTeacher(aTeacher);
            //update listview of teachers
            adapter.add(aTeacher);
            adapter.notifyDataSetChanged();
            onResume();
            //Toast to let user know that Teacher was added successfully
            Toast.makeText(getApplicationContext(), "Teacher successfully added.", Toast.LENGTH_SHORT).show();
            teacherImage = null;
            imgTeacherImage.setImageResource(R.mipmap.noimgavail);
            //clear all fields
            txtFirstName.setText("");
            txtLastName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
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
            CheckBox deleteChBx = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_list_item, parent, false);
                deleteChBx = (CheckBox) convertView.findViewById(R.id.chkListItem);
                convertView.setTag(deleteChBx);
                //Checkbox listener anonymous inner class
                //Deletes checked teachers and leaves unchecked teachers
                deleteChBx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        Teacher changeTeacher = (Teacher) cb.getTag();
                        changeTeacher.setDeletable(cb.isChecked() == true ? true : false);
                        txtFirstName.setText(changeTeacher.getFirstName());
                        txtLastName.setText(changeTeacher.getLastName());
                        txtPhone.setText(changeTeacher.getPhoneNum());
                        txtEmail.setText(changeTeacher.getEmail());
                        imgTeacherImage.setImageBitmap(ImageUtils.getImage(changeTeacher.getTeacherImage()));
                    }
                });
            }
            else
            {
                deleteChBx = (CheckBox) convertView.getTag();
            }
            Teacher current = teacherList.get(position);
            deleteChBx.setText(current.getFirstName() + " " + current.getLastName());
            deleteChBx.setTag(current);
            return convertView;
        }
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
    }



}
