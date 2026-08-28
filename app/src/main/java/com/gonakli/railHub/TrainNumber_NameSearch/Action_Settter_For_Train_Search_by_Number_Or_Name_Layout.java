package com.gonakli.railHub.TrainNumber_NameSearch;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gonakli.railHub.R;

public class Action_Settter_For_Train_Search_by_Number_Or_Name_Layout extends LinearLayout {
    Context context;
    TextView searchInputArea;
    ImageButton searchButton;
    public Action_Settter_For_Train_Search_by_Number_Or_Name_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mainBody();
    }
    private void mainBody(){
    View view =    LayoutInflater.from(context).inflate(R.layout.search_train_layout, this, true);
        searchInputArea = findViewById(R.id.search_input_area);
        searchButton = findViewById(R.id.btn_icon_search);

        searchInputArea.setOnClickListener(v ->{
        Intent searchTrain = new Intent(getContext(), Show_Trains.class);
        getContext().startActivity(searchTrain);

    });

        searchButton.setOnClickListener(v ->{
            String searchQuery = searchInputArea.getText().toString();
            Intent searchTrain = new Intent(getContext(), Show_Trains.class);
            if(!searchQuery.isBlank()){
                searchTrain.putExtra("train_number", 123456);
                getContext().startActivity(searchTrain);
            }else{
                getContext().startActivity(searchTrain);
            }
        });

    }

}
