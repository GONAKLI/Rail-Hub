package com.gonakli.railHub.TrainNumber_NameSearch;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.gonakli.railHub.ADAPTERS.Train_List_Recycler_Adapter;
import com.gonakli.railHub.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_List_Structure_New;

import java.util.ArrayList;

public class Show_Trains extends AppCompatActivity {
    SearchView showTrainSearchView;
    LottieAnimationView loadingTrainAnimation;
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
                new Thread(() -> {
                    try (Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Show_Trains.this)) {
                        ArrayList<Train_List_Structure_New> arrTrainList = dbHelper.getTrainSearchResult(newText);
                        if (recyclerAdapter != null) {
                            runOnUiThread(() -> {
                                recyclerAdapter.updateList(arrTrainList);
                            });

                        }
                    } // ✅ filter call

                }).start();
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
        startLoadingAnimation();
        new Thread(() -> {

            try (Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Show_Trains.this);) {
                arrTrains = dbHelper.getTrainSearchResult(null);
                recyclerAdapter = new Train_List_Recycler_Adapter(Show_Trains.this, arrTrains);
                runOnUiThread(() -> {
                    stopLoadingAnimation();
                    showTrainRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    showTrainRecyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.setSearchView(showTrainSearchView);
                });

            }
        }).start();

    }

    private void find_all_by_id() {
        showTrainSearchView = findViewById(R.id.showTrainSearchView);
        showTrainRecyclerView = findViewById(R.id.showTrainsRecyclerView);
        loadingTrainAnimation = findViewById(R.id.loadingTrainAnimation);
    }

    private void startLoadingAnimation() {
        if (loadingTrainAnimation != null) {
            loadingTrainAnimation.setAnimation(R.raw.loading_panda);
            loadingTrainAnimation.playAnimation();
            loadingTrainAnimation.setRepeatCount(LottieDrawable.INFINITE);
            loadingTrainAnimation.setRepeatMode(LottieDrawable.RESTART);
            loadingTrainAnimation.setVisibility(View.VISIBLE);
        }
    }

    private void stopLoadingAnimation() {
        if (loadingTrainAnimation != null) {
            loadingTrainAnimation.cancelAnimation();
            loadingTrainAnimation.setVisibility(View.GONE);
        }
    }

}
