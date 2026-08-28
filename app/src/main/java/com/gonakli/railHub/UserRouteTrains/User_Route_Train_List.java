package com.gonakli.railHub.UserRouteTrains;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.gonakli.railHub.ADAPTERS.User_Route_Train_Recycler_View_Adapter;
import com.gonakli.railHub.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class User_Route_Train_List extends AppCompatActivity {
    String from_Station_Value, to_Station_Value;
    TextView fromStation, toStation;
    RecyclerView recyclerView;
    LinearLayout no_Train_Found_Container;
    Toolbar toolbar;
    LottieAnimationView trainLoadingWaitTimeLottieAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_route_train_list);
        find_all_id();
        startLoadingAnimation();
        set_Station_sName_In_Ui();
        setToolBar();
        recyclerViewSetup();


    }

    private void startLoadingAnimation() {
        if(trainLoadingWaitTimeLottieAnimation != null){
            trainLoadingWaitTimeLottieAnimation.setAnimation(R.raw.loading_panda);
            trainLoadingWaitTimeLottieAnimation.playAnimation();
            trainLoadingWaitTimeLottieAnimation.setRepeatCount(LottieDrawable.INFINITE);
            trainLoadingWaitTimeLottieAnimation.setRepeatMode(LottieDrawable.REVERSE);
        }
    }
    private void stopLoadingAnimation(){
        if(trainLoadingWaitTimeLottieAnimation != null){
            trainLoadingWaitTimeLottieAnimation.cancelAnimation();
            trainLoadingWaitTimeLottieAnimation.setVisibility(View.GONE);
        }
    }

    private void recyclerViewSetup() {
        new Thread(() -> {
            Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(getApplicationContext());
            ArrayList<Train_Schedule_Structure> arrSchedule = dbHelper.getTrainsBetweenStations(from_Station_Value, to_Station_Value);
            runOnUiThread(this::stopLoadingAnimation);
            if (!arrSchedule.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                arrSchedule.sort((t1, t2) -> {
                    String time1 = "", time2 = "";
                    for (int i = 0; i < t1.getStationList().size(); i++) {
                        if (t1.getStationList().get(i).getStationCode().trim().equalsIgnoreCase(from_Station_Value)) {
                            time1 = t1.getStationList().get(i).getArrivalTime();
                            if (time1 == null || time1.equalsIgnoreCase("--")) {
                                time1 = t1.getStationList().get(i).getDepartureTime();
                            }
                            break;
                        }
                    }
                    for (int i = 0; i < t2.getStationList().size(); i++) {
                        if (t2.getStationList().get(i).getStationCode().trim().equalsIgnoreCase(from_Station_Value)) {
                            time2 = t2.getStationList().get(i).getArrivalTime();
                            if (time2 == null || time2.equalsIgnoreCase("--")) {
                                time2 = t2.getStationList().get(i).getDepartureTime();
                            }
                            break;
                        }
                    }

                    LocalTime lt1 = LocalTime.parse(time1, formatter);
                    LocalTime lt2 = LocalTime.parse(time2, formatter);

                    return lt1.compareTo(lt2);
                });


                User_Route_Train_Recycler_View_Adapter recycler_adapter = new User_Route_Train_Recycler_View_Adapter(User_Route_Train_List.this, arrSchedule, from_Station_Value, to_Station_Value);
                this.runOnUiThread(() -> {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(recycler_adapter);
                    no_Train_Found_Container.setVisibility(View.GONE);
                });


            } else {
                runOnUiThread(() -> {
                    no_Train_Found_Container.setVisibility(View.VISIBLE);
                });

            }
        }).start();


    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void set_Station_sName_In_Ui() {
        Intent intent = getIntent();
        from_Station_Value = intent.getStringExtra("fromStation");
        to_Station_Value = intent.getStringExtra("toStation");
        fromStation.setText(from_Station_Value);
        toStation.setText(to_Station_Value);
    }

    private void find_all_id() {
        fromStation = findViewById(R.id.user_route_from_station_textView);
        toStation = findViewById(R.id.user_route_to_station_textView);
        recyclerView = findViewById(R.id.trainScheduleRecyclerView);
        toolbar = findViewById(R.id.application_custom_toolbar);
        no_Train_Found_Container = findViewById(R.id.no_Train_Found_Container);
        trainLoadingWaitTimeLottieAnimation = findViewById(R.id.trainLoadingWaitTimeLottieAnimation);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
