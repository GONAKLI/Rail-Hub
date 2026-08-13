package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.PassengerList_Structure;

import java.util.ArrayList;

public class PNR_Passenger_List_Adapter extends ArrayAdapter<PassengerList_Structure> {

    public static class viewHolder{
        TextView passengerSerialNumber, bookingDetails, currentStatus;
        TextView passengerAgeNationality, passengerBerthChoice;
    }

    Context context;
    ArrayList<PassengerList_Structure> arrPassengerList;
    public PNR_Passenger_List_Adapter(@NonNull Context context, ArrayList<PassengerList_Structure> arrPassengerList) {
        super(context, 0);
        this.context = context;
        this.arrPassengerList = arrPassengerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.passenger_list_view_layout, parent, false);
        find_all_id(holder, convertView);



        String srNo = arrPassengerList.get(position).getPassengerSerialNumber();
        String passengerAge = arrPassengerList.get(position).getPassengerAge();
        String passengerNationality = arrPassengerList.get(position).getPassengerNationality();
        String passengerBerthChoice = arrPassengerList.get(position).getPassengerBerthChoice();
        String bookingStatus = arrPassengerList.get(position).getBookingStatus();
        String bookingCoachId = arrPassengerList.get(position).getBookingCoachId();
        String bookingBerthNo = arrPassengerList.get(position).getBookingBerthNo();
        String bookingBerthCode = arrPassengerList.get(position).getBookingBerthCode();
        String currentStatusDetails = arrPassengerList.get(position).getCurrentStatus();



        holder.passengerSerialNumber.setText(String.format("Passenger %s", srNo));
        holder.passengerAgeNationality.setText(String.format("Age: %s • Nationality: %s", passengerAge,passengerNationality));
        holder.passengerBerthChoice.setText(String.format("Berth Choice: %s", passengerBerthChoice));
        holder.bookingDetails.setText(String.format("Booking: %s • Coach: %s • Berth: %s (%s)", bookingStatus,bookingCoachId, bookingBerthNo,bookingBerthCode));
        holder.currentStatus.setText(String.format("Current Status: %s", currentStatusDetails));


        return convertView;
    }

    private void find_all_id(viewHolder holder, View convertView) {
        holder.passengerSerialNumber = convertView.findViewById(R.id.passengerSerialNumber);
        holder.bookingDetails = convertView.findViewById(R.id.bookingDetails);
        holder.currentStatus = convertView.findViewById(R.id.currentStatus);
        holder.passengerAgeNationality = convertView.findViewById(R.id.passengerAgeNationality);
        holder.passengerBerthChoice = convertView.findViewById(R.id.passengerBerthChoice);
    }

    @Override
    public int getCount() {
        return arrPassengerList.size();
    }
}
