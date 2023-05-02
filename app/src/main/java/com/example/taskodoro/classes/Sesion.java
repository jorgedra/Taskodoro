package com.example.taskodoro.classes;

public class Sesion {

    private String sesionName;
    private Task task;
    private int timeSpend;

    public Sesion(String sesionName, Task task, int timeSpend) {
        this.sesionName = sesionName;
        this.task = task;
        this.timeSpend = timeSpend;
    }

    public String getSesionName() {
        return sesionName;
    }

    public void setSesionName(String sesionName) {
        this.sesionName = sesionName;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(int timeSpend) {
        this.timeSpend = timeSpend;
    }
}
