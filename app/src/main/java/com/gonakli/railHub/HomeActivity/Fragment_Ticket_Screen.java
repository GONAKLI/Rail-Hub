package com.gonakli.railHub.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railHub.R;

public class Fragment_Ticket_Screen extends Fragment {
    View ticketView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    ticketView = inflater.inflate(R.layout.ticket_screen_fragment,container,false);
    requireActivity().setTitle("Ticket Booking");
    return ticketView;
    }
}
