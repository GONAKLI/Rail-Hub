package com.gonakli.railradar.HomeActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gonakli.railradar.DrawerItemsWork.Drawer_Item;
import com.gonakli.railradar.R;
import com.gonakli.railradar.ThemePreference.ThemeSelectionOnStartUp;
import com.google.android.material.navigation.NavigationView;

public class Home_Screen_Activity extends AppCompatActivity {
    private long prevTime = 0;
Toolbar toolbar;
DrawerLayout drawerLayout;
NavigationView navigationView;
FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        get_application_theme();
        findAllID();
        setUpApplicationToolBar();
        frameLayoutSetUp();
        setActionOnNavigationItems();



    }

    private void get_application_theme() {
        ThemeSelectionOnStartUp themeSelector = new ThemeSelectionOnStartUp(Home_Screen_Activity.this);
        themeSelector.show_theme_chooser_dialogue();
    }
    private void frameLayoutSetUp() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Home_Activity_All_Fragment_Manager combinedHomeScreenDashboard = new Home_Activity_All_Fragment_Manager();
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

    private void setActionOnNavigationItems(){

        Drawer_Item drawerItem = new Drawer_Item(Home_Screen_Activity.this, drawerLayout, navigationView);

    }


    @Override
    public void onBackPressed() {
       
        long currentTime = System.currentTimeMillis();

        if(drawerLayout.isOpen()){
        drawerLayout.close();
    }else{
            if(currentTime - prevTime < 2000)
            {
                super.onBackPressed();
            }else{
                Toast.makeText(this, "Tap back button again to exit", Toast.LENGTH_SHORT).show();
                prevTime = currentTime;
            }

    }

    }

}
