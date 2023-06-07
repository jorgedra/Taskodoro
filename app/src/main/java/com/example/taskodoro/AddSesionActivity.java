package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.taskodoro.classes.Session;
import com.example.taskodoro.classes.Task;
import com.example.taskodoro.recycler_view.TaskAdapater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSesionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    private TaskAdapater taskAdapter;

    private Button bt_start_session;

    private EditText edt_task_name;

    private EditText edt_session_name;

    private FloatingActionButton bt_add_task;

    private RecyclerView rv_task = null;
    private Map<String, Task> taskMap = new HashMap<String, Task>();

    private List<Task> tasks = new ArrayList<Task>();

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_sesion);

        bt_start_session = (Button) findViewById(R.id.bt_start_session);
        bt_add_task = (FloatingActionButton) findViewById(R.id.bt_add_task);
        edt_task_name = (EditText) findViewById(R.id.edt_task_name);
        edt_session_name = (EditText) findViewById(R.id.edt_session_name);
        rv_task = (RecyclerView) findViewById(R.id.rv_task);

        rv_task.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapater(getApplicationContext(),tasks);
        rv_task.setAdapter(taskAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        List<String> usedNames =  getSessionsName(currentUser);
        bt_start_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sessionName = String.valueOf(edt_session_name.getText());
                if(TextUtils.isEmpty(sessionName)){
                    edt_session_name.setError("Session name can't be empty");
                } else if (usedNames.contains(sessionName)) {
                    edt_session_name.setError("Session name already exists");
                } else{
                    addSessionToDatabse(sessionName);
                }
            }
        });

        bt_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = String.valueOf(edt_task_name.getText());
                if(TextUtils.isEmpty(taskName)){
                    edt_task_name.setError("Task name can't be empty");
                }
                else{
                    Task newTask = createTask(taskName);
                    taskMap.put(newTask.getTaskName(), newTask);
                    tasks.add(newTask);
                    taskAdapter.notifyDataSetChanged();
                    edt_task_name.setText("");
                }
            }
        });
    }


    private void addSessionToDatabse(String sessionName) {
        Session newSession = new Session(sessionName,taskMap);
        myRef.child("sessions").child(currentUser).child("Session " + newSession.getSessionName()).setValue(newSession);

        Intent intent = new Intent(AddSesionActivity.this, MainActivity.class);
        intent.putExtra("sessionName", "Session " + newSession.getSessionName());
        startActivity(intent);
    }

    private Task createTask(String taskName){
        return new Task(taskName,false);
    }

    private List<String> getSessionsName(String currentUser){
        List<String> sessionsNames = new ArrayList<String>();
        myRef.child("sessions").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String sessionName = ds.child("sessionName").getValue().toString();
                        sessionsNames.add(sessionName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return sessionsNames;
    }
}