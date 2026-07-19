package com.gonakli.railradar.TrainNumber_NameSearch;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.R;

public class Show_Trains extends AppCompatActivity {
SearchView showTrainSearchView;
RecyclerView showTrainRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_trains_list);
        find_all_by_id();

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

    private void find_all_by_id() {
        showTrainSearchView = findViewById(R.id.showTrainSearchView);
        showTrainRecyclerView = findViewById(R.id.showTrainsRecyclerView);
    }

}
