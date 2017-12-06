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

import com.example.student.cooper_assign2.Adapters.TeacherAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.student.cooper_assign2.R.id.teacherImage;
import static com.example.student.cooper_assign2.R.id.teacherImageEdit;

public class Edit_Teacher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Instance Variables
    List<Teacher> teacherList;
    DBHelper myDBHelper;
    Spinner teacherSpinner;
    ArrayAdapter<Teacher> teacherAdapter;
    Teacher currentTeacher;
    EditText txtFName, txtLName, txtEmail, txtPhone;
    TextView lblTeachID;
    ImageView imgTeacher;
    private Bitmap teacherImage;
    private final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__teacher);
        //get reference to database
        myDBHelper = new DBHelper(this);
        //get references to elements on activity
        txtFName = (EditText)findViewById(R.id.txtTFirstName);
        txtLName = (EditText)findViewById(R.id.txtTLastName);
        txtEmail = (EditText)findViewById(R.id.txtTEmail);
        txtPhone = (EditText)findViewById(R.id.txtTPhone);
        imgTeacher =(ImageView)findViewById(R.id.teacherImageEdit);
        lblTeachID = (TextView)findViewById(R.id.lblTeacherID);
        //populate teacher list from database
        teacherList = myDBHelper.getAllTeachers();
        teacherSpinner = (Spinner)findViewById(R.id.spinEditTeacher);
        //Sets the adapter for the teacher spinner
        teacherAdapter = new TeacherAdapter(getApplicationContext(), R.layout.spinner_item, teacherList);
        teacherAdapter.setDropDownViewResource(R.layout.spinner_item);
        teacherSpinner.setAdapter(teacherAdapter);
        teacherSpinner.setOnItemSelectedListener(this);
    }

    protected void onResume()
    {
        super.onResume();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        //Gets the selected teacher object
        currentTeacher = teacherAdapter.getItem(position);

        //fill in the textviews with teacher info
        txtFName.setText(currentTeacher.getFirstName());
        txtLName.setText(currentTeacher.getLastName());
        txtEmail.setText(currentTeacher.getEmail());
        txtPhone.setText(currentTeacher.getPhoneNum());
        //set teacher image
        imgTeacher.setImageBitmap(ImageUtils.getImage(currentTeacher.getTeacherImage()));
        teacherImage = ImageUtils.getImage(currentTeacher.getTeacherImage());
        //display teacher id
        lblTeachID.setText("Teacher ID: " + Integer.toString(currentTeacher.getId()));
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
        String firstName = txtFName.getText().toString();
        String lastName = txtLName.getText().toString();
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
            currentTeacher.setFirstName(firstName);
            currentTeacher.setLastName(lastName);
            currentTeacher.setEmail(email);
            currentTeacher.setPhoneNum(phone);
            currentTeacher.setTeacherImage(ImageUtils.getBytes(teacherImage));
            //add the teacher to the database
            myDBHelper.updateTeacher(currentTeacher);
            //Toast to let user know that Teacher was added successfully
            Toast.makeText(getApplicationContext(), "Teacher updated successfully added.", Toast.LENGTH_SHORT).show();
        }
    }

    //cancel
    //cancel button click event handler
    //starts the list activity
    public void cancel(View view)
    {
        startActivity(new Intent(Edit_Teacher.this, TeacherListActivity.class));
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
                    teacherImage = image;
                    imgTeacher.setImageBitmap(image);
                }
            }
        }
    }
}
