package com.example.taskodoro.classes;

public class Task {

    private String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public Task( ) {
        this.taskName = "";
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
