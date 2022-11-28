package com.example.medisantventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class OrderDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        if (!preferences.contains("token")){
            startActivity(new Intent(this, Login.class));
            finish();
        }
        Toast.makeText(this, ""+getIntent().getExtras().getString("order_id"), Toast.LENGTH_SHORT).show();
    }
}