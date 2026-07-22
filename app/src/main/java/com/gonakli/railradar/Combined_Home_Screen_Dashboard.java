package com.gonakli.railradar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railradar.ADAPTERS.Stations_Dropdown_Adapter;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;

public class Combined_Home_Screen_Dashboard extends Fragment {
    View combined_Home_Screen;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    combined_Home_Screen = inflater.inflate(R.layout.combined_home_screen_dashboard, container, false);

   return combined_Home_Screen;
    }


}
