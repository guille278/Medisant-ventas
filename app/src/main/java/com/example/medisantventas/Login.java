package com.example.medisantventas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisantventas.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        if (preferences.contains("token")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        signin = findViewById(R.id.btn_signin);

        signin.setOnClickListener(listener -> {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject user = new JSONObject();
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Campos vacios");
                builder.setMessage("Email y contraseña requeridos");
                builder.setPositiveButton("Aceptar", null);
                builder.create().show();
            } else {
                try {
                    user.put("email", email.getText().toString());
                    user.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Config.URL + "api/auth/admin/login",
                        user,
                        response -> {
                            SharedPreferences.Editor editor = preferences.edit();
                            try {
                                editor.putString("token", response.getString("token"));
                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        },
                        error -> {
                            Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                        }
                );
                queue.add(request);
            }
        });
    }


}