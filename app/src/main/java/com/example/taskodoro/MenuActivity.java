package com.example.taskodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private DatabaseReference myRefUsers;
    private Button bt_log_out;

    private  Button bt_create_sesion;

    private Button bt_show_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_log_out = (Button) findViewById(R.id.bt_log_out);
        bt_create_sesion = (Button) findViewById(R.id.bt_create_sesion);
        bt_show_log = (Button) findViewById(R.id.bt_show_log);




        bt_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutuser();
            }
        });

        bt_create_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AddSesionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void logOutuser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MenuActivity.this, "se cerró la sesión correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}