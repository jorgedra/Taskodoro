package com.example.taskodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText edt_email;

    private EditText edt_password;

    private Button bt_sign_up;

    private Button bt_log_in;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        bt_log_in = (Button) findViewById(R.id.bt_log_in);
        bt_sign_up = (Button) findViewById(R.id.bt_sign_up);

        mAuth = FirebaseAuth.getInstance();

        bt_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(edt_email.getText()).trim();
                String password = String.valueOf(edt_password.getText()).trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "text fiels can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        bt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(edt_email.getText()).trim();
                String password = String.valueOf(edt_password.getText()).trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "text fiels can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(email, password);
                }
            }
        });
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "login completed", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "error on login", Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                }
            }
        });
    }

    public void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "user signed in", Toast.LENGTH_SHORT).show();
//                            // updateUI(user);
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "user couldn't be signed in", Toast.LENGTH_SHORT).show();
                    //  updateUI(null);
                }
            }

        });
    }

}