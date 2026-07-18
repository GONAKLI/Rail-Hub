package com.gonakli.railradar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
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
        add_All_Stations_In_DB();
        findAllID();
        setUpApplicationToolBar();
        frameLayoutSetUp();

    }


    private void add_All_Stations_In_DB() {
        new Thread(()-> {
            Station_List_DB_Helper DB_Helper = new Station_List_DB_Helper(getApplicationContext());
            SQLiteDatabase db = DB_Helper.getReadableDatabase();
           Cursor cursor = db.rawQuery(
                    "select count(*) from STATION_LIST_TABLE " , null );
           cursor.moveToFirst();
           int count = cursor.getInt(0);
           cursor.close();
           if(count == 0){
               DB_Helper.addStationInDB();
           }

        }).start();

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
