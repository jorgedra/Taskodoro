package com.example.taskodoro.recycler_view;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskodoro.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    TextView txt_task_name;
    CheckBox cb_task_status;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_task_name = itemView.findViewById(R.id.txt_task_name);
        cb_task_status = itemView.findViewById(R.id.cb_task_status);
    }

    public TextView getTxt_task_name() {
        return txt_task_name;
    }

    public void setTxt_task_name(TextView txt_task_name) {
        this.txt_task_name = txt_task_name;
    }

    public CheckBox getCb_task_status() {
        return cb_task_status;
    }

    public void setCb_task_status(CheckBox cb_task_status) {
        this.cb_task_status = cb_task_status;
    }
}
