package com.example.taskodoro.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskodoro.R;
import com.example.taskodoro.classes.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TaskMainAdapter extends RecyclerView.Adapter<TaskMainAdapter.ViewHolder>{

    private int resource;
    private ArrayList<Task> tasks;

    private DatabaseReference myRef;

    private FirebaseDatabase database;

    public TaskMainAdapter(int resource, ArrayList<Task> tasks) {
        this.resource = resource;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new TaskMainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Task task = tasks.get(position);
        holder.txt_main_task_name.setText(task.getTaskName());
        holder.cb_main_task_status.setChecked(task.getTaskStatus());
        holder.cb_main_task_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myRef.child("sesions").child(currentUser).child(sesionName).child("tasks").child(task.getTaskName()).child("taskStatus").setValue(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_main_task_name;
        private CheckBox cb_main_task_status;

        public View view;


        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.txt_main_task_name = (TextView) view.findViewById(R.id.txt_main_task_name);
            this.cb_main_task_status = (CheckBox) view.findViewById(R.id.cb_main_task_status);
        }
    }
}
