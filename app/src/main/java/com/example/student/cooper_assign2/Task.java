package com.example.student.cooper_assign2;

/**
 * Created by Aaron Cooper on 10/24/2017.
 */

public class Task {
    private String taskName;
    private String taskID;
    private String taskDescription;

    public Task(String taskname, String taskDescription)
    {
        this.taskName = taskname;
        this.taskDescription = taskDescription;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
