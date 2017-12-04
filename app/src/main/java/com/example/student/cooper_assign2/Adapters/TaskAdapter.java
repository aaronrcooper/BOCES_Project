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
import com.example.student.cooper_assign2.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Cooper on 11/30/2017.
 */

//**************TASK ADAPTER*********************************
public class TaskAdapter extends ArrayAdapter<Task> {
    Context context;
    List<Task> taskList = new ArrayList<Task>();

    //Constructor
    public TaskAdapter(Context c, int rId, List<Task> objects)
    {
        super(c, rId, objects);
        taskList = objects;
        context = c;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        TextView task = null;
        ImageView taskImage = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_with_image_item, parent, false);

        }
        //Sets view objects
        task = (TextView) convertView.findViewById(R.id.spinnerItemTextView);
        taskImage = (ImageView) convertView.findViewById(R.id.spinnerImageView);
        convertView.setTag(task);
        //Sets properties of views
        Task current = taskList.get(position);
        task.setText(current.getTaskName() + " " + current.getDescription());
        task.setTag(current);
        //resize image so all images are consistent
        Bitmap taskResizedImage = ImageUtils.getImage(current.getTaskImage());
        taskResizedImage = ImageUtils.resizeImage(taskResizedImage);
        taskImage.setImageBitmap(taskResizedImage);
        taskImage.setTag(current);
        return convertView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }
    public Task getPosition(int position)
    {
        return taskList.get(position);
    }
}

