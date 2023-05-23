package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.taskodoro.classes.Sesion;
import com.example.taskodoro.classes.Task;
import com.example.taskodoro.recycler_view.Task_adapater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowLogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    private RecyclerView rv_sesions = null;

    private List<Sesion> sesions = new ArrayList<Sesion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);

        rv_sesions = (RecyclerView) findViewById(R.id.rv_sesions);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rv_sesions.setLayoutManager(new LinearLayoutManager(this));
//        rv_sesions.setAdapter(new Sesion_adapter(getApplicationContext(),sesions));// for rv, need to take data from firebase, look the video on tfg bookmark
    }

    private void getSesionsFor(String currentUser){
        myRef.child("sesions").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                       String sesionName = ds.child("sesionName").getValue().toString();
                       Long timeSpend = Long.valueOf(ds.child("timeSpend").getValue().toString());
                       Map<String, Task> tasks = (Map<String, Task>) ds.child("tasks").getValue();
                       sesions.add(new Sesion(sesionName, tasks, timeSpend));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}