package com.gonakli.railHub.ADAPTERS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railHub.API_Limit.Pnr_Check_Api_Limit;
import com.gonakli.railHub.DB_WORK.PNR_Data_DB_Helper;
import com.gonakli.railHub.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Services.API_Call.PNR_Enquiry_API_CALL;
import com.gonakli.railHub.Structure_Class.PassengerList_Structure;
import com.gonakli.railHub.Structure_Class.Pnr_Api_Response_Structure;
import com.gonakli.railHub.Utility.DateAndTimeRelated.Date_Tense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class All_Pnr_Data_Recycler_View_Adapter extends RecyclerView.Adapter<All_Pnr_Data_Recycler_View_Adapter.viewHolder> {
    ArrayList<Pnr_Api_Response_Structure> arrPnrData;
    Intent iPnrApiService;

    Context context;

    public All_Pnr_Data_Recycler_View_Adapter(Context context, ArrayList<Pnr_Api_Response_Structure> arrPnrData) {
        this.context = context;
        this.arrPnrData = arrPnrData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pnr_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        Station_List_DB_Helper db = new Station_List_DB_Helper(context);
        String trNumber, trName, pnrNum, boardStName, boardStCode, reservationUptoStName, reservationUptoStCode;
        String journeyClass, quota, chartStatus, journeyDate, ticketFare;
        StringBuilder informationMessage;
        trNumber = arrPnrData.get(position).getTrainNumber();
        trName = arrPnrData.get(position).getTrainName();
        pnrNum = arrPnrData.get(position).getPnrNumber();
        Log.d("dbChecker", "onBindViewHolder: " + trNumber + " " + pnrNum);
        boardStName = db.getStationNameByCode(arrPnrData.get(position).getBoardingPoint());
        boardStCode = arrPnrData.get(position).getBoardingPoint();
        reservationUptoStName = db.getStationNameByCode(arrPnrData.get(position).getReservationUpto());
        reservationUptoStCode = arrPnrData.get(position).getReservationUpto();
        journeyClass = arrPnrData.get(position).getJourneyClass();
        quota = arrPnrData.get(position).getQuota();
        chartStatus = arrPnrData.get(position).getChartStatus();
        journeyDate = dateFormater(arrPnrData.get(position).getDateOfJourney());
        String responseMsg = Date_Tense.isDate_Past_Present_Future(journeyDate);
        informationMessage = new StringBuilder();
        ticketFare = arrPnrData.get(position).getTicketFare();
        for (Object data : arrPnrData.get(position).getArrInformationMessage()) {
            if (data != null && !data.toString().isEmpty() && !data.toString().equalsIgnoreCase("null")) {
                holder.pnrStatusInfoMessage.setVisibility(View.VISIBLE);
                informationMessage.append(String.format("• %s \n", data.toString().trim()));
            }
        }
        if (informationMessage.toString().isEmpty()) {
            holder.pnrStatusInfoMessage.setVisibility(View.GONE);
        }

        if (arrPnrData.get(position).getArrPassengerList() != null) {
            holder.pnrStatusPassengerContainer.removeAllViews();


            for (PassengerList_Structure myPass : arrPnrData.get(position).getArrPassengerList()) {
                View view = LayoutInflater.from(context).inflate(R.layout.passenger_list_view_layout, holder.pnrStatusPassengerContainer, false);
                TextView passengerSerialNumber, bookingDetails, currentStatus;
                TextView passengerAgeNationality, passengerBerthChoice;

                passengerSerialNumber = view.findViewById(R.id.passengerSerialNumber);
                bookingDetails = view.findViewById(R.id.bookingDetails);
                currentStatus = view.findViewById(R.id.currentStatus);
                passengerAgeNationality = view.findViewById(R.id.passengerAgeNationality);
                passengerBerthChoice = view.findViewById(R.id.passengerBerthChoice);

                String srNo = myPass.getPassengerSerialNumber();
                String passengerAge = myPass.getPassengerAge();
                String passengerNationality = myPass.getPassengerNationality();
                String passengerBerthChoi = myPass.getPassengerBerthChoice();
                String bookingStatus = myPass.getBookingStatus();
                String bookingCoachId = myPass.getBookingCoachId();
                String bookingBerthNo = myPass.getBookingBerthNo();
                String bookingBerthCode = myPass.getBookingBerthCode();
                String currentStatusDetails = myPass.getCurrentStatusDetails();

                passengerSerialNumber.setText(String.format("Passenger %s", srNo));
                passengerAgeNationality.setText(String.format("Age: %s • Nationality: %s", passengerAge, passengerNationality));
                passengerBerthChoice.setText(String.format("Berth Choice: %s", passengerBerthChoi));
                bookingDetails.setText(String.format("Booking: %s • Coach: %s • Berth: %s (%s)", bookingStatus, bookingCoachId, bookingBerthNo, bookingBerthCode));
                currentStatus.setText(String.format("Current Status: %s", currentStatusDetails));
                if (currentStatusDetails.contains("CNF")) {
                    currentStatus.setBackgroundColor(Color.parseColor("#469C11"));
                } else if (currentStatusDetails.contains("CAN")) {
                    currentStatus.setBackgroundColor(Color.parseColor("#9C1F11"));
                } else {
                    currentStatus.setBackgroundColor(Color.parseColor("#787474"));
                }
                holder.pnrStatusPassengerContainer.addView(view);
            }
        }
        db.close();

        holder.pnrCardParentContainer.setVisibility(View.VISIBLE);
        holder.pnrStatusTrainNumber.setText(trNumber);
        holder.pnrStatusTrainName.setText(trName);
        holder.pnrStatusPnrNumber.setText(String.format("PNR: %s", pnrNum));
        holder.pnrStatusBoardingPoint.setText(String.format("Boarding: %s (%s)", boardStName, boardStCode));
        holder.pnrStatusReservationUpto.setText(String.format("Reservation Upto: %s (%s)", reservationUptoStName, reservationUptoStCode));
        holder.pnrStatusDetails.setText(String.format("Class: %s • Quota: %s • Chart Status: %s", journeyClass, quota, chartStatus));
        holder.pnrStatusJourneyDate.setText(String.format("Journey Date: %s", journeyDate));
        holder.pnrStatusInfoMessage.setText(informationMessage.toString());
        holder.pnrStatusTicketFare.setText(String.format("Ticket Fare: %s", ticketFare));

        delete_Pnr(holder, pnrNum);
        refresh_pnr(holder, pnrNum);
        copy_Pnr(holder, pnrNum);
        customize_card_on_journey_date(responseMsg, holder);


    }

    private void customize_card_on_journey_date(String responseMsg, viewHolder holder) {
        if(responseMsg.equalsIgnoreCase("past")){
            holder.itemView.setBackgroundColor(Color.parseColor("#FF3C28"));
        } else if(responseMsg.equalsIgnoreCase("present")) {
            holder.itemView.setBackgroundColor(Color.parseColor("#99F49C"));
        }else if (responseMsg.equalsIgnoreCase("future")) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffe6f0"));
        }
    }

    private void copy_Pnr(viewHolder holder, String pnrNum) {
        holder.pnrStatusPnrNumber.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("pnrNumber", pnrNum);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Pnr number copied", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return arrPnrData.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView pnrStatusTrainNumber, pnrStatusTrainName, pnrStatusPnrNumber, pnrStatusBoardingPoint;
        TextView pnrStatusReservationUpto, pnrStatusDetails, pnrStatusJourneyDate, pnrStatusInfoMessage, pnrStatusTicketFare;
        LinearLayout pnrCardParentContainer;
        LinearLayout pnrStatusPassengerContainer;
        ImageView btnRefreshPnr;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pnrStatusTrainNumber = itemView.findViewById(R.id.pnrStatusTrainNumber);
            pnrStatusTrainName = itemView.findViewById(R.id.pnrStatusTrainName);
            pnrStatusPnrNumber = itemView.findViewById(R.id.pnrStatusPnrNumber);
            pnrStatusBoardingPoint = itemView.findViewById(R.id.pnrStatusBoardingPoint);
            pnrStatusReservationUpto = itemView.findViewById(R.id.pnrStatusReservationUpto);
            pnrStatusDetails = itemView.findViewById(R.id.pnrStatusDetails);
            pnrStatusJourneyDate = itemView.findViewById(R.id.pnrStatusJourneyDate);
            pnrStatusInfoMessage = itemView.findViewById(R.id.pnrStatusInfoMessage);
            pnrCardParentContainer = itemView.findViewById(R.id.pnrCardParentContainer);
            pnrStatusTicketFare = itemView.findViewById(R.id.pnrStatusTicketFare);
            pnrStatusPassengerContainer = itemView.findViewById(R.id.pnrStatusPassengerContainer);
            btnRefreshPnr = itemView.findViewById(R.id.btnRefreshPnr);
            Log.d("dbChecker", "onBindViewHolder: " + "come in viewHolder");


        }
    }

    private String dateFormater(String dateStr) {
        LocalDateTime dateTime = null;
        String formatted = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
            formatted = dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
        Log.d("pnrFormattedDate", "dateFormater: " + formatted);
        return formatted;
    }

    private void delete_Pnr(viewHolder holder, String pnrNum) {
        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Delete PNR")
                    .setMessage("Shall i delete PNR - " + pnrNum + " ?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(() -> {
                                PNR_Data_DB_Helper db = new PNR_Data_DB_Helper(context);
                                db.deletePnrFromDB(pnrNum);
                                db.close();
                                ((Activity) context).runOnUiThread(() -> {
                                    refreshAdapter();
                                });
                            }).start();

                        }
                    })
                    .setIcon(R.drawable.delete_bucket)
                    .create();
            alertDialog.show();
            return true;
        });


    }

    private void refresh_pnr(viewHolder holder, String pnrNum) {

        holder.btnRefreshPnr.setOnClickListener(v -> {
            if (Pnr_Check_Api_Limit.canCallPnrAPI(pnrNum)) {
                iPnrApiService = new Intent(context, PNR_Enquiry_API_CALL.class);
                iPnrApiService.putExtra("pnrNumber", pnrNum);
                iPnrApiService.putExtra("isRefresh", true);
                context.startService(iPnrApiService);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.pnr_refresh_rotation);
                holder.btnRefreshPnr.startAnimation(animation);
            } else {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.pnr_refresh_rotation);
                holder.btnRefreshPnr.startAnimation(animation);
                Toast.makeText(context, "Pnr Status is already upTodate", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void refreshAdapter() {

        new Thread(() -> {
            PNR_Data_DB_Helper db = new PNR_Data_DB_Helper(context);
            ArrayList<Pnr_Api_Response_Structure> latestData = db.getPnrDataFromDB();
            db.close();
            ((Activity) context).runOnUiThread(() -> {
                this.arrPnrData.clear();
                this.arrPnrData.addAll(latestData);
                this.notifyDataSetChanged();
            });
        }).start();


    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (iPnrApiService != null) {
            context.stopService(iPnrApiService);
        }
    }
}
