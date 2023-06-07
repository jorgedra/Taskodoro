package com.example.taskodoro.classes;

import java.util.Map;

public class Session {

    private String sessionName;
    private Map<String,Task> tasks;
    private String timeSpend;

    public Session(String sessionName, Map<String, Task> tasks, String timeSpend) {
        this.sessionName = sessionName;
        this.tasks = tasks;
        this.timeSpend = timeSpend;
    }

    public Session(String sessionName, Map<String, Task> tasks) {
        this.sessionName = sessionName;
        this.tasks = tasks;
        this.timeSpend = "";
    }

    public Session(String sessionName, String timeSpend) {
        this.sessionName = sessionName;
        this.timeSpend = timeSpend;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
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
