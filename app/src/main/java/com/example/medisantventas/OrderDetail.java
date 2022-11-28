package com.example.medisantventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisantventas.adapters.OrderDetailAdapter;
import com.example.medisantventas.models.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrderDetail extends AppCompatActivity {

    SharedPreferences preferences;
    String token;
    Order order = new Order();
    Spinner orderStatus;
    RecyclerView products;
    TextView userName, userEmail, userAddress, userPhone, orderDate, orderUpdated, orderTotal;
    Button cancel, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        preferences = getSharedPreferences("token", MODE_PRIVATE);
        if (!preferences.contains("token")) {
            startActivity(new Intent(this, Login.class));
            finish();
        }

        orderStatus = findViewById(R.id.sp_order_status);
        products = findViewById(R.id.rv_order_detail);
        userName = findViewById(R.id.tv_user_name);
        userEmail = findViewById(R.id.tv_user_email);
        userAddress = findViewById(R.id.tv_user_address);
        userPhone = findViewById(R.id.tv_user_phone);
        orderDate = findViewById(R.id.tv_order_date);
        orderUpdated = findViewById(R.id.tv_order_updated);
        orderTotal = findViewById(R.id.tv_order_total);
        cancel = findViewById(R.id.btn_cancel_order);
        update = findViewById(R.id.btn_update_order);


        token = preferences.getString("token", "");
        order.setId(Integer.valueOf(getIntent().getExtras().getString("order_id")));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_status, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        orderStatus.setAdapter(adapter);
        orderStatus.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = order.getOrder(
                listener -> {
                    JSONObject data = (JSONObject) listener;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        try {

                            userName.setText("Nombre: " + data.getJSONObject("user").getString("name"));
                            userEmail.setText("Correo electrónico: " + data.getJSONObject("user").getString("email"));
                            userAddress.setText("Dirección: " + data.getJSONObject("user").getString("address") + ", " + data.getJSONObject("user").getString("city"));
                            userPhone.setText("Teléfono: " + data.getJSONObject("user").getString("phone"));

                            OffsetDateTime createdAt = OffsetDateTime.ofInstant(Instant.parse(data.getString("created_at")), ZoneId.systemDefault());
                            OffsetDateTime updatedAt = OffsetDateTime.ofInstant(Instant.parse(data.getString("updated_at")), ZoneId.systemDefault());
                            orderDate.setText("Fecha pedido: " + DateTimeFormatter.ofPattern("dd MMMM yyyy - hh:mm:ssa").format(createdAt));
                            orderUpdated.setText("Última actualización: " + DateTimeFormatter.ofPattern("dd MMMM yyyy - hh:mm:ssa").format(updatedAt));
                            products.setAdapter(new OrderDetailAdapter(data.getJSONArray("products"), getApplicationContext()));
                            orderTotal.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(data.getDouble("total")));
                            if (data.getInt("status") != 3) {
                                cancel.setVisibility(View.VISIBLE);
                                update.setVisibility(View.VISIBLE);
                                orderStatus.setEnabled(true);
                            }
                            orderStatus.setSelection(data.getInt("status") - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    products.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                token
        );
        queue.add(request);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Actualizando...", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancelando...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}