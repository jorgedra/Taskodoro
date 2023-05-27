package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.taskodoro.classes.Sesion;
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

    private Button bt_start_sesion;

    private EditText edt_task_name;

    private EditText edt_sesion_name;

    private FloatingActionButton bt_add_task;

    private RecyclerView rv_task = null;
    private Map<String, Task> taskMap = new HashMap<String, Task>();

    private List<Task> tasks = new ArrayList<Task>();

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sesion);

        bt_start_sesion = (Button) findViewById(R.id.bt_start_sesion);
        bt_add_task = (FloatingActionButton) findViewById(R.id.bt_add_task);
        edt_task_name = (EditText) findViewById(R.id.edt_task_name);
        edt_sesion_name = (EditText) findViewById(R.id.edt_sesion_name);
        rv_task = (RecyclerView) findViewById(R.id.rv_task);

        rv_task.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapater(getApplicationContext(),tasks);
        rv_task.setAdapter(taskAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        List<String> usedNames =  getSesionsName(currentUser);
        bt_start_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(usedNames);
                String sesionName = String.valueOf(edt_sesion_name.getText());
                if(TextUtils.isEmpty(sesionName)){
                    edt_sesion_name.setError("Sesion name can't be empty");
                } else if (usedNames.contains(sesionName)) {
                    edt_sesion_name.setError("Sesion name already exists");
                } else{
                    addSesionToDatabse(sesionName);
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
                }
            }
        });
    }


    private void addSesionToDatabse(String sesionName) {
        Sesion newSesion = new Sesion(sesionName,taskMap);
        myRef.child("sesions").child(currentUser).child("Sesion " + newSesion.getSesionName()).setValue(newSesion);

        Intent intent = new Intent(AddSesionActivity.this, MainActivity.class);
        intent.putExtra("sesionName", "Sesion " + newSesion.getSesionName());
        startActivity(intent);
    }

    private Task createTask(String taskName){
        return new Task(taskName,false);
    }

    private List<String> getSesionsName(String currentUser){
        List<String> sesionsNames = new ArrayList<String>();
        myRef.child("sesions").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String sesionName = ds.child("sesionName").getValue().toString();
                        sesionsNames.add(sesionName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return sesionsNames;
    }
}