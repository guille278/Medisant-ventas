package com.example.medisantventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medisantventas.adapters.OrderViewPagerAdapter;
import com.example.medisantventas.config.Config;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    OrderViewPagerAdapter viewPagerAdapter;
    private String token;
    SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("token", MODE_PRIVATE);
        if (!preferences.contains("token")) {
            Intent intent = new Intent(this.getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        token = preferences.getString("token", "");
        List<Fragment> tabs = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        titles.add("Recibidos");
        titles.add("En camino");
        titles.add("Entregados");

        tabs.add(new TabReceivedFragment());
        tabs.add(new TabShippedFragment());
        tabs.add(new TabDeliveredFragment());

        viewPagerAdapter = new OrderViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        tabLayout = findViewById(R.id.app_tabLayout);
        viewPager = findViewById(R.id.app_viewPager);


        viewPagerAdapter.setFragmentList(tabs, titles);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        RequestQueue queue = Volley.newRequestQueue(this);
        switch (item.getItemId()) {
            case R.id.menu_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Esta seguro que desea cerrar sesión?");
                builder.setPositiveButton("Continuar", (dialogInterface, i) -> {
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Config.URL + "api/auth/admin/logout",
                            response -> {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear().apply();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            },
                            error -> {
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return new Config().getHeaders(token);
                        }
                    };
                    queue.add(stringRequest);
                });
                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}