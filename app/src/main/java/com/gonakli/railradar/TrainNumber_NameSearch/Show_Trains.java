package com.gonakli.railradar.TrainNumber_NameSearch;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.Recycler_Adapter;
import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.gonakli.railradar.DB_WORK.Train_List_DB_Helper;
import com.gonakli.railradar.R;

import java.util.ArrayList;

public class Show_Trains extends AppCompatActivity {
SearchView showTrainSearchView;
RecyclerView showTrainRecyclerView;
ArrayList<Train_List_Structure> arrTrains;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_trains_list);
        find_all_by_id();
        setRecyclerView();
        

        showTrainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void setRecyclerView() {
        Train_List_DB_Helper dbHelper = new Train_List_DB_Helper(getApplicationContext());
        arrTrains = dbHelper.getTrainList();

        Recycler_Adapter recyclerAdapter = new Recycler_Adapter(getApplicationContext(), arrTrains);

        showTrainRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showTrainRecyclerView.setAdapter(recyclerAdapter);
    }

    private void find_all_by_id() {
        showTrainSearchView = findViewById(R.id.showTrainSearchView);
        showTrainRecyclerView = findViewById(R.id.showTrainsRecyclerView);
    }




}
