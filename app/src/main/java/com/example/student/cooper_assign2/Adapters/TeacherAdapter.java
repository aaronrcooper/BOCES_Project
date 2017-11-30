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
 * Created by Student on 11/30/2017.
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

        } else {
            teacher = (TextView) convertView.getTag();
            teacherImage = (ImageView) convertView.getTag();
        }
        Teacher current = teacherList.get(position);
        //Sets text
        teacher.setText(current.getFirstName() + " " + current.getLastName());
        teacher.setTag(current);
        //Sets image
        teacherImage.setImageBitmap(ImageUtils.getImage(current.getTeacherImage()));
        teacherImage.setTag(current);
        return convertView;
    }
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView teacherView =null;
        ImageView teacherImage = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);
            teacherView = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
            convertView.setTag(teacherView);

        } else {
            teacherView = (TextView) convertView.getTag();
            teacherImage = (ImageView) convertView.getTag();
        }
        teacherView.setText(teacherList.get(position).getFirstName() + " " + teacherList.get(position).getLastName());
        teacherView.setHeight(60);
        teacherImage.setImageBitmap(ImageUtils.getImage(teacherList.get(position).getTeacherImage()));
        return convertView;
    }
    public Teacher getPosition(int position)
    {
        return teacherList.get(position);
    }
}