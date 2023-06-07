package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;

import com.example.taskodoro.classes.Session;
import com.example.taskodoro.recycler_view.SessionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowLogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private SessionAdapter sessionAdapter;

    private DatabaseReference myRef;

    private RecyclerView rv_sessions = null;

    private ArrayList<Session> sessions = new ArrayList<Session>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_log);

        rv_sessions = (RecyclerView) findViewById(R.id.rv_sessions);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_sessions.setLayoutManager(layoutManager);
        getSesionsFromDatabase(currentUser);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_sessions.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        rv_sessions.addItemDecoration(dividerItemDecoration);
    }

    private void getSesionsFromDatabase(String currentUser){
        myRef.child("sessions").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String sessionName = ds.child("sessionName").getValue().toString();
                        String timeSpend = ds.child("timeSpend").getValue().toString();
                        sessions.add(new Session(sessionName, timeSpend));
                    }
                    sessionAdapter = new SessionAdapter(sessions, R.layout.sesion_view);
                    rv_sessions.setAdapter(sessionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}