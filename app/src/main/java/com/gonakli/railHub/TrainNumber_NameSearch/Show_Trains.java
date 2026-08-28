package com.gonakli.railHub.TrainNumber_NameSearch;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railHub.ADAPTERS.Train_List_Recycler_Adapter;
import com.gonakli.railHub.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_List_Structure_New;

import java.util.ArrayList;

public class Show_Trains extends AppCompatActivity {
    SearchView showTrainSearchView;
    RecyclerView showTrainRecyclerView;
    Train_List_Recycler_Adapter recyclerAdapter;
    ArrayList<Train_List_Structure_New> arrTrains;

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
                try(Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Show_Trains.this)) {
                   ArrayList<Train_List_Structure_New> arrTrainList = dbHelper.getTrainSearchResult(newText);
                    if(recyclerAdapter != null){
                        recyclerAdapter.updateList(arrTrainList);
                    }
                } // ✅ filter call
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
        Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Show_Trains.this);
        arrTrains = dbHelper.getTrainSearchResult(null);
        recyclerAdapter = new Train_List_Recycler_Adapter(Show_Trains.this, arrTrains);
        showTrainRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        showTrainRecyclerView.setAdapter(recyclerAdapter);
        dbHelper.close();
    }

    private void find_all_by_id() {
        showTrainSearchView = findViewById(R.id.showTrainSearchView);
        showTrainRecyclerView = findViewById(R.id.showTrainsRecyclerView);
    }


}
