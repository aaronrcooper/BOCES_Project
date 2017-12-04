package com.example.student.cooper_assign2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.cooper_assign2.ImageUtils;
import com.example.student.cooper_assign2.R;
import com.example.student.cooper_assign2.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Cooper on 11/30/2017.
 */

//*******************TEACHER ADAPTER**************************
public class TeacherAdapter extends ArrayAdapter<Teacher> {
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
        ImageView teacherImage = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
            teacher = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
            teacherImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
            convertView.setTag(teacher);

        }
        //Sets view objects
        teacher = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
        teacherImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
        convertView.setTag(teacher);
        //Sets properties of views
        Teacher current = teacherList.get(position);
        teacher.setText(current.getFirstName() + " " + current.getLastName());
        teacher.setTag(current);
        teacherImage.setImageBitmap(ImageUtils.getImage(current.getTeacherImage()));
        teacherImage.setTag(current);
        return convertView;
    }
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    public Teacher getPosition(int position)
    {
        return teacherList.get(position);
    }
}