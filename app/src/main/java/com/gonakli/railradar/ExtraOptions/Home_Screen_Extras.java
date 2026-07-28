package com.gonakli.railradar.ExtraOptions;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gonakli.railradar.R;

public class Home_Screen_Extras extends RelativeLayout {
Context context;
LinearLayout nearbyStation;
    public Home_Screen_Extras(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.extra_option_on_home_screen, this, true);
        find_all_id();
        action_on_icon_click();

    }

    private void action_on_icon_click() {
        action_nearByStationIcon();
    }

    private void action_nearByStationIcon() {
        nearbyStation.setOnClickListener(v -> {
            Intent iNext = new Intent(getContext(), NearBy_Station_Activity.class);
            context.startActivity(iNext);
        });
    }

    private void find_all_id() {
        nearbyStation = findViewById(R.id.nearbyStationIconContainer);
    }
}
