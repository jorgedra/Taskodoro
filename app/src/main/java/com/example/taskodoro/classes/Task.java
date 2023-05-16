package com.example.taskodoro.classes;

public class Task {

    private String taskName;
    private Boolean taskStatus;

    public Task(String taskName, Boolean taskStatus) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
    }

    public Task(String taskName) {
        this.taskName = taskName;
        this.taskStatus = false;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }
}
