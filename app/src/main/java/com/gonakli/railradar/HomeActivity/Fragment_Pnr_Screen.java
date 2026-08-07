package com.gonakli.railradar.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railradar.R;

public class Fragment_Pnr_Screen extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      getActivity().setTitle("PNR Status");
       return LayoutInflater.from(getActivity()).inflate(R.layout.pnr_check, container,false);
    }
}
