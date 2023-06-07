package com.example.taskodoro.recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskodoro.R;
import com.example.taskodoro.classes.Session;

import java.util.ArrayList;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private int resource;
    private ArrayList<Session> sessions;

    public SessionAdapter(ArrayList<Session> sessions, int resource){
        this.sessions = sessions;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.txt_view_sesion_name.setText(session.getSessionName());
        holder.txt_view_time_spent.setText(String.valueOf(session.getTimeSpend()));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_view_sesion_name;
        private TextView txt_view_time_spent;
        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            this.txt_view_sesion_name = (TextView) view.findViewById(R.id.txt_view_sesion_name);
            this.txt_view_time_spent = (TextView) view.findViewById(R.id.txt_view_time_spent);
        }
    }
}
