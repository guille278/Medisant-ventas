package com.example.medisantventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.medisantventas.adapters.OrderViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    OrderViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
        if (!preferences.contains("token")) {
            Intent intent = new Intent(this.getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

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
}