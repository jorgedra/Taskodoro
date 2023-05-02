package com.example.taskodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSesionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRefUsers;

    private Button bt_start_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sesion);

        bt_start_sesion = (Button) findViewById(R.id.bt_start_sesion);

        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference();

        bt_start_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSesionToDatabse();
            }
        });
    }


    private void addSesionToDatabse() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myRefUsers.child("sesions").child(currentUser).setValue("hola");
    }
}