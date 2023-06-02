package com.example.taskodoro.classes;

import java.util.List;
import java.util.Map;

public class Sesion {

    private String sesionName;
    private Map<String,Task> tasks;
    private String timeSpend;

    public Sesion(String sesionName, Map<String, Task> tasks, String timeSpend) {
        this.sesionName = sesionName;
        this.tasks = tasks;
        this.timeSpend = timeSpend;
    }

    public Sesion(String sesionName, Map<String, Task> tasks) {
        this.sesionName = sesionName;
        this.tasks = tasks;
        this.timeSpend = "";
    }

    public Sesion(String sesionName, String timeSpend) {
        this.sesionName = sesionName;
        this.timeSpend = timeSpend;
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

    public String getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(String timeSpend) {
        this.timeSpend = timeSpend;
    }
}
