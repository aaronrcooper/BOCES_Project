package com.example.student.cooper_assign2;

/**
 * Created by Aaron Cooper on 10/24/2017.
 */

public class Task {
    private String taskName;
    private int taskID;
    private String description;


    public Task()
    {

    }
    //overloaded constructor
    public Task(String name, String descr)
    {
        taskName = name;
        description = descr;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
