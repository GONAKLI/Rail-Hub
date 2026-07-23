package com.gonakli.railradar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Train_Schedule;
import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Trains_And_Stations_List;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.DB_WORK.Train_List_DB_Helper;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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
        add_All_Stations_In_DB();
        add_All_Trains_In_DB();
        findAllID();
        setUpApplicationToolBar();
        frameLayoutSetUp();
        setActionOnNavigationItems();



    }



    private void add_All_Trains_In_DB() {
        new Thread(() ->{
        Train_List_DB_Helper dbHelper = new Train_List_DB_Helper(getApplicationContext());

            SQLiteDatabase db = dbHelper.getReadableDatabase();
           Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Train_List_DB_Helper.TABLE_NAME, null);
           cursor.moveToFirst();

           int count = cursor.getInt(0);
           if(count == 0){
               dbHelper.addTrainsInDB();
           }
            cursor.close();
           db.close();
        }).start();
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

    private void setActionOnNavigationItems(){
        navigationView.setNavigationItemSelectedListener(item ->{
            Toast.makeText(getApplicationContext(), ""+item.getItemId(), Toast.LENGTH_SHORT).show();
            return true;
        });
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
