package com.example.student.cooper_assign2.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.cooper_assign2.ImageUtils;
import com.example.student.cooper_assign2.R;
import com.example.student.cooper_assign2.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Cooper on 11/30/2017.
 */

public class StudentAdapter extends ArrayAdapter<Student> {

    Context context;
    List<Student> studentList = new ArrayList<Student>();

    //Constructor
    public StudentAdapter(Context c, int rId, List<Student> objects) {
        super(c, rId, objects);
        studentList = objects;
        context = c;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView student = null;
        ImageView studentImage = null;
        //Gets convertView if null
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);


        }
        //Sets view objects
        student = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
        studentImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
        convertView.setTag(student);

        Student current = studentList.get(position);
        //Sets TextView properties
        student.setText(current.getFirstName() + " " + current.getLastName());
        student.setTag(current);
        //Sets imageView properties
        //resize image so all images are consistent
        Bitmap studentResizedImage = ImageUtils.getImage(current.getStudentImage());
        studentResizedImage = ImageUtils.resizeImage(studentResizedImage);
        studentImage.setImageBitmap(studentResizedImage);
        studentImage.setTag(current);
        //return
        return convertView;
    }
    //Gets dropdown view
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    public Student getPosition(int position)
    {
        return studentList.get(position);
    }
}
