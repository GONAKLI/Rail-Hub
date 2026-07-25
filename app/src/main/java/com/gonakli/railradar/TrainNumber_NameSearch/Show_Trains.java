package com.gonakli.railradar.TrainNumber_NameSearch;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.Train_List_Recycler_Adapter;
import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.gonakli.railradar.DB_WORK.Train_List_DB_Helper;
import com.gonakli.railradar.R;

import java.util.ArrayList;

public class Show_Trains extends AppCompatActivity {
    SearchView showTrainSearchView;
    RecyclerView showTrainRecyclerView;
    Train_List_Recycler_Adapter recyclerAdapter;
    ArrayList<Train_List_Structure> arrTrains;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_trains_list);
        find_all_by_id();
        focusOnSearchView();
        setRecyclerView();

        showTrainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.filterSearchResult(newText); // ✅ filter call
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void focusOnSearchView() {
        showTrainSearchView.requestFocus();
    }

    private void setRecyclerView() {
        Train_List_DB_Helper dbHelper = new Train_List_DB_Helper(getApplicationContext());
        arrTrains = dbHelper.getTrainList();

        recyclerAdapter = new Train_List_Recycler_Adapter(getApplicationContext(), arrTrains);
        showTrainRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showTrainRecyclerView.setAdapter(recyclerAdapter);
    }

    private void find_all_by_id() {
        showTrainSearchView = findViewById(R.id.showTrainSearchView);
        showTrainRecyclerView = findViewById(R.id.showTrainsRecyclerView);
    }


}
