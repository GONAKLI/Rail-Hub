package com.gonakli.railradar.PNR_Work;

import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Pnr_Api_Response_Structure;

public class Pnr_Card_Work extends LinearLayout {
    View view;
    TextView pnrStatusTrainNumber, pnrStatusTrainName,pnrStatusPnrNumber,pnrStatusBoardingPoint;
    TextView pnrStatusReservationUpto, pnrStatusBoardingPointCode, pnrStatusReservationUptoCode;
    ListView pnrStatusPassengerList;

    String trNumber, trName, pnrNum, boardStName,boardStCode, reservationUptoStName, reservationUptoStCode;


    public Pnr_Card_Work(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.pnr_card, this, true);
        find_all_id();
    }



    private void set_data() {
        pnrStatusTrainNumber.setText(trNumber);
        pnrStatusTrainName.setText(trName);
        pnrStatusPnrNumber.setText(pnrNum);
        pnrStatusBoardingPoint.setText(boardStName);
        pnrStatusReservationUpto.setText(reservationUptoStName);
        pnrStatusBoardingPointCode.setText(boardStCode);
        pnrStatusReservationUptoCode.setText(reservationUptoStCode);
    }

    private void find_all_id() {
        pnrStatusTrainNumber = view.findViewById(R.id.pnrStatusTrainNumber);
        pnrStatusTrainName = view.findViewById(R.id.pnrStatusTrainName);
        pnrStatusPnrNumber = view.findViewById(R.id.pnrStatusPnrNumber);
        pnrStatusBoardingPoint = view.findViewById(R.id.pnrStatusBoardingPoint);
        pnrStatusReservationUpto = view.findViewById(R.id.pnrStatusReservationUpto);
        pnrStatusBoardingPointCode = view.findViewById(R.id.pnrStatusBoardingPointCode);
        pnrStatusReservationUptoCode = view.findViewById(R.id.pnrStatusReservationUptoCode);
        pnrStatusPassengerList = view.findViewById(R.id.pnrStatusPassengerList);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("pnrWork", "receiver:  come in receiver" );
            if("PNR_RESPONSE_ACTION".equals(intent.getAction())){
                Station_List_DB_Helper db = new Station_List_DB_Helper(getContext());
                Pnr_Api_Response_Structure resData =(Pnr_Api_Response_Structure) intent.getSerializableExtra("pnrResponse");
                if(resData.isSuccess()){
                     trNumber = resData.getTrainNumber();
                     trName = resData.getTrainName();
                     pnrNum = resData.getPnrNumber();
                     boardStName = db.getStationNameByCode(resData.getBoardingPoint());
                     boardStCode = resData.getBoardingPoint();
                     reservationUptoStName = db.getStationNameByCode(resData.getReservationUpto());
                     reservationUptoStCode = resData.getReservationUpto();
                     db.close();
                     set_data();

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
}
