package com.gonakli.railHub.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railHub.R;

public class Fragment_Home_Screen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.home_activity_fragment, container, false);
        requireActivity().setTitle(R.string.app_name);
        return home;
    }
}
