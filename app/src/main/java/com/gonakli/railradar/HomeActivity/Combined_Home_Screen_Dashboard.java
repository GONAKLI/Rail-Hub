package com.gonakli.railradar.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railradar.R;

public class Combined_Home_Screen_Dashboard extends Fragment {
    View combined_Home_Screen;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    combined_Home_Screen = inflater.inflate(R.layout.combined_home_screen_dashboard, container, false);

   return combined_Home_Screen;
    }


}
