package com.gonakli.railradar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class Home_Screen_Activity extends AppCompatActivity {
Toolbar toolbar;
DrawerLayout drawerLayout;
NavigationView navigationView;
FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        findAllID();
        setUpApplicationToolBar();
        frameLayoutSetUp();

    }

    private void frameLayoutSetUp() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Combined_Home_Screen_Dashboard combinedHomeScreenDashboard = new Combined_Home_Screen_Dashboard();
        fragmentTransaction.add(R.id.home_frame_layout, combinedHomeScreenDashboard);
        fragmentTransaction.commit();


    }

    private void setUpApplicationToolBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,drawerLayout, toolbar, R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void findAllID() {
        toolbar = findViewById(R.id.home_tool_bar);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.home_navigation_view);
        frameLayout = findViewById(R.id.home_frame_layout);
    }
}
