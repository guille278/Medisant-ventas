package com.example.medisantventas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisantventas.adapters.OrdersAdapter;
import com.example.medisantventas.models.Order;

import org.json.JSONArray;

public class TabDeliveredFragment extends Fragment {
    Order order = new Order();
    RecyclerView recyclerView;
    RequestQueue queue;
    private String token;

    public TabDeliveredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_delivered, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = view.getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        recyclerView = view.findViewById(R.id.rv_received);
        queue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest request = order.getOrders(
                listener -> {
                    recyclerView.setAdapter(new OrdersAdapter((JSONArray) listener, view.getContext()));
                },
                error -> {
                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                order.DELIVERED,
                token
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        queue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        JsonArrayRequest request = order.getOrders(
                listener -> {
                    recyclerView.setAdapter(new OrdersAdapter((JSONArray) listener, getContext()));
                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                },
                order.DELIVERED,
                token
        );
        queue.add(request);
    }
}