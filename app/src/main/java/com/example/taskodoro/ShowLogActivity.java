package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.Window;

import com.example.taskodoro.classes.Sesion;
import com.example.taskodoro.classes.Task;
import com.example.taskodoro.recycler_view.SesionAdapter;
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

    private SesionAdapter sesionAdapter;

    private DatabaseReference myRef;

    private RecyclerView rv_sesions = null;

    private ArrayList<Sesion> sesions = new ArrayList<Sesion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_log);

        rv_sesions = (RecyclerView) findViewById(R.id.rv_sesions);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_sesions.setLayoutManager(layoutManager);
        getSesionsFromDatabase(currentUser);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_sesions.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        rv_sesions.addItemDecoration(dividerItemDecoration);
    }

    private void getSesionsFromDatabase(String currentUser){
        myRef.child("sesions").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String sesionName = ds.child("sesionName").getValue().toString();
                        String timeSpend = ds.child("timeSpend").getValue().toString();
                        sesions.add(new Sesion(sesionName, timeSpend));
                    }
                    sesionAdapter = new SesionAdapter(sesions, R.layout.sesion_view);
                    rv_sesions.setAdapter(sesionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}