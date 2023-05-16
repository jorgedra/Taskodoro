package com.example.taskodoro.classes;

import java.util.List;
import java.util.Map;

public class Sesion {

    private String sesionName;
    private Map<String,Task> tasks;
    private int timeSpend;

    public Sesion(String sesionName, Map<String, Task> tasks, int timeSpend) {
        this.sesionName = sesionName;
        this.tasks = tasks;
        this.timeSpend = timeSpend;
    }

    public Sesion(String sesionName, Map<String, Task> tasks) {
        this.sesionName = sesionName;
        this.tasks = tasks;
        this.timeSpend = 0;
    }

    public String getSesionName() {
        return sesionName;
    }

    public void setSesionName(String sesionName) {
        this.sesionName = sesionName;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public int getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(int timeSpend) {
        this.timeSpend = timeSpend;
    }
}
