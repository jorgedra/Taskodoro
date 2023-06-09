package com.example.taskodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    private final String api_url = "https://api.goprogram.ai/inspiration";

    private Button bt_log_out;

    private  Button bt_create_session;

    private Button bt_show_log;

    private TextView txt_api_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_log_out = (Button) findViewById(R.id.bt_log_out);
        bt_create_session = (Button) findViewById(R.id.bt_create_session);
        bt_show_log = (Button) findViewById(R.id.bt_show_log);
        txt_api_text = (TextView) findViewById(R.id.txt_api_text);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(api_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    txt_api_text.setText(response.getString("quote"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt_api_text.setText("Improve 1% every day");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        bt_show_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ShowLogActivity.class);
                startActivity(intent);
            }
        });

        bt_create_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AddSessionActivity.class);
                startActivity(intent);
            }
        });

        bt_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutuser();
            }
        });

    }

    public void logOutuser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MenuActivity.this, "user signed out", Toast.LENGTH_SHORT).show();
        finish();
    }
}