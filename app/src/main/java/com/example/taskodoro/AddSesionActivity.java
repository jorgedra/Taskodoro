package com.example.taskodoro;

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
import com.example.taskodoro.recycler_view.Task_adapater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSesionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    private Button bt_start_sesion;

    private EditText edt_task_name;

    private EditText edt_sesion_name;

    private FloatingActionButton bt_add_task;

    private RecyclerView rv_task = null;
    private Map<String, Task> taskMap = new HashMap<String, Task>();

    private List<Task> tasks = new ArrayList<Task>();

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
        rv_task.setAdapter(new Task_adapater(getApplicationContext(),tasks));


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        bt_start_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sesionName = String.valueOf(edt_sesion_name.getText());
                if(TextUtils.isEmpty(sesionName)){
                    edt_sesion_name.setError("La sesion debe de tener nombre");
                }
                else{
                    addSesionToDatabse(sesionName);
                }
            }
        });

        bt_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = String.valueOf(edt_task_name.getText());
                if(TextUtils.isEmpty(taskName)){
                    edt_task_name.setError("La tarea debe de tener nombre");
                }
                else{
                    Task newTask = createTask(taskName);
                    taskMap.put(newTask.getTaskName(), newTask);
                    tasks.add(newTask);
                }
            }
        });
    }


    private void addSesionToDatabse(String sesionName) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Sesion newSesion = new Sesion(sesionName,taskMap);
        myRef.child("sesions").child(currentUser).child("Sesion" + newSesion.getSesionName()).setValue(newSesion);

        Intent intent = new Intent(AddSesionActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private Task createTask(String taskName){
        return new Task(taskName,false);
    }
}