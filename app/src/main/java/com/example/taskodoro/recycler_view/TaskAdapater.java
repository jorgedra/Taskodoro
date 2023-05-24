package com.example.taskodoro.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskodoro.R;
import com.example.taskodoro.classes.Task;


import java.util.List;

public class TaskAdapater extends RecyclerView.Adapter<TaskViewHolder> {

    Context context;
    List<Task> tasks;

    public TaskAdapater(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.txt_task_name.setText(tasks.get(position).getTaskName());
        holder.cb_task_status.setChecked(tasks.get(position).getTaskStatus());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
