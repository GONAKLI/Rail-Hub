package com.gonakli.railradar.PNR_Work;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gonakli.railradar.ADAPTERS.PNR_Passenger_List_Adapter;
import com.gonakli.railradar.DB_WORK.PNR_Data_DB_Helper;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.PassengerList_Structure;
import com.gonakli.railradar.Structure_Class.Pnr_Api_Response_Structure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pnr_Card_Work extends LinearLayout {
    View view;
    TextView pnrStatusTrainNumber, pnrStatusTrainName,pnrStatusPnrNumber,pnrStatusBoardingPoint;
    TextView pnrStatusReservationUpto, pnrStatusDetails, pnrStatusJourneyDate,pnrStatusInfoMessage, pnrStatusTicketFare;
    ListView pnrStatusPassengerListView;
            LinearLayout pnrCardParentContainer;

    String trNumber, trName, pnrNum, boardStName,boardStCode, reservationUptoStName, reservationUptoStCode;
    String journeyClass, quota, chartStatus, journeyDate,ticketFare;
    StringBuilder informationMessage;

    //passenger related


    public Pnr_Card_Work(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.pnr_card, this, true);
        find_all_id();
    }



    private void set_data() {
        pnrCardParentContainer.setVisibility(View.VISIBLE);
        pnrStatusTrainNumber.setText(trNumber);
        pnrStatusTrainName.setText(trName);
        pnrStatusPnrNumber.setText(String.format("PNR: %s", pnrNum));
        pnrStatusBoardingPoint.setText(String.format("Boarding: %s (%s)", boardStName, boardStCode));
        pnrStatusReservationUpto.setText(String.format("Reservation Upto: %s (%s)", reservationUptoStName, reservationUptoStCode));
        pnrStatusDetails.setText(String.format("Class: %s • Quota: %s • Chart Status: %s", journeyClass, quota,chartStatus));
        pnrStatusJourneyDate.setText(String.format("Journey Date: %s", journeyDate));
        pnrStatusInfoMessage.setText(informationMessage.toString());
        pnrStatusTicketFare.setText(String.format("Ticket Fare: %s", ticketFare));

    }

    private void find_all_id() {
        pnrStatusTrainNumber = view.findViewById(R.id.pnrStatusTrainNumber);
        pnrStatusTrainName = view.findViewById(R.id.pnrStatusTrainName);
        pnrStatusPnrNumber = view.findViewById(R.id.pnrStatusPnrNumber);
        pnrStatusBoardingPoint = view.findViewById(R.id.pnrStatusBoardingPoint);
        pnrStatusReservationUpto = view.findViewById(R.id.pnrStatusReservationUpto);
        pnrStatusPassengerListView = view.findViewById(R.id.pnrStatusPassengerListView);
        pnrStatusDetails = view.findViewById(R.id.pnrStatusDetails);
        pnrStatusJourneyDate = view.findViewById(R.id.pnrStatusJourneyDate);
        pnrStatusInfoMessage = view.findViewById(R.id.pnrStatusInfoMessage);
        pnrCardParentContainer = view.findViewById(R.id.pnrCardParentContainer);
        pnrStatusTicketFare = view.findViewById(R.id.pnrStatusTicketFare);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("pnrWork", "receiver:  come in receiver" );
            if("PNR_RESPONSE_ACTION".equals(intent.getAction())){
                Station_List_DB_Helper db = new Station_List_DB_Helper(getContext());
                Pnr_Api_Response_Structure resData =(Pnr_Api_Response_Structure) intent.getSerializableExtra("pnrResponse");
                if(resData.isSuccess()){
                    new Thread(() ->{
                        PNR_Data_DB_Helper helper = new PNR_Data_DB_Helper(getContext());
                        helper.addPnrPassengerInDB(resData);
                    }).start();
                     trNumber = resData.getTrainNumber();
                     trName = resData.getTrainName();
                     pnrNum = resData.getPnrNumber();
                     boardStName = db.getStationNameByCode(resData.getBoardingPoint());
                     boardStCode = resData.getBoardingPoint();
                     reservationUptoStName = db.getStationNameByCode(resData.getReservationUpto());
                     reservationUptoStCode = resData.getReservationUpto();
                     journeyClass = resData.getJourneyClass();
                    quota = resData.getQuota();
                    chartStatus = resData.getChartStatus();
                    journeyDate = dateFormater(resData.getDateOfJourney());
                    informationMessage = new StringBuilder();
                    ticketFare = resData.getTicketFare();
                    for(Object data : resData.getArrInformationMessage()){
                        if(data != null && !data.toString().isBlank() && !data.toString().equalsIgnoreCase("null")){
                            pnrStatusInfoMessage.setVisibility(View.VISIBLE);
                            informationMessage.append(String.format("• %s \n", data.toString().trim()));
                        }
                    }

                        if(resData.getArrPassengerList() != null){
                            PNR_Passenger_List_Adapter adapter = new PNR_Passenger_List_Adapter(getContext(),resData.getArrPassengerList());
                            pnrStatusPassengerListView.setAdapter(adapter);
                        }

                     db.close();
                     set_data();

                }else{
                    if(!resData.isSuccess()){
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setTitle("Something Wrong")
                                .setMessage(resData.getErrorMessage())
                                .setIcon(R.drawable.pnr_alert_error)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }
                                )
                                .create();
                        alertDialog.show();
                    }
                }
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        Log.d("pnrWork", "onAttachedToWindow: registering receiver");
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().registerReceiver(broadcastReceiver, new IntentFilter("PNR_RESPONSE_ACTION"), Context.RECEIVER_NOT_EXPORTED);
            Log.d("pnrWork", "onAttachedToWindow: receiver registered");
        }
    }

    private  String dateFormater(String dateStr){
        LocalDateTime dateTime = null;
        String formatted = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
            formatted = dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        }
        return formatted;
    }
}
