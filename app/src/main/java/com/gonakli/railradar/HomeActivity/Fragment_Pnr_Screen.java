package com.gonakli.railradar.HomeActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.All_Pnr_Data_Recycler_View_Adapter;
import com.gonakli.railradar.DB_WORK.PNR_Data_DB_Helper;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Services.API_Call.PNR_Enquiry_API_CALL;
import com.gonakli.railradar.Structure_Class.Pnr_Api_Response_Structure;

import java.util.ArrayList;

public class Fragment_Pnr_Screen extends Fragment {
    View view;
    AppCompatEditText pnrSearchField;
    AppCompatButton btnFindPnr;
    RecyclerView allPnrCheckRecyclerView;
    All_Pnr_Data_Recycler_View_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      getActivity().setTitle("PNR Status");
       view = LayoutInflater.from(getActivity()).inflate(R.layout.pnr_check, container,false);
       find_all_id();
       onSubmitAction();
       recyclerView_setup();
       return view;
    }

    private void recyclerView_setup() {
        new Thread(()->{
            PNR_Data_DB_Helper helper = new PNR_Data_DB_Helper(getContext());
            ArrayList<Pnr_Api_Response_Structure> arrPnrData = helper.getPnrDataFromDB();
            helper.close();
            if(arrPnrData != null && !arrPnrData.isEmpty()){
                adapter = new All_Pnr_Data_Recycler_View_Adapter(getContext(),arrPnrData);
                requireActivity().runOnUiThread(() ->{
                    allPnrCheckRecyclerView.setAdapter(adapter);
                    allPnrCheckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                });

            }

        }).start();






    }


    private void find_all_id() {
        pnrSearchField = view.findViewById(R.id.pnr_search_field);
        btnFindPnr = view.findViewById(R.id.btn_find_pnr);
        allPnrCheckRecyclerView = view.findViewById(R.id.allPnrCheckRecyclerView);
    }

    private void onSubmitAction() {
        btnFindPnr.setOnClickListener(v ->{
            String pnrValue = pnrSearchField.getText().toString().trim();
            if(pnrValue.length() <10){
                Toast.makeText(getContext(), "Pnr should be a valid 10 digit number", Toast.LENGTH_LONG).show();
                return;
            }
            Intent iPNR = new Intent(getContext(), PNR_Enquiry_API_CALL.class);
            iPNR.putExtra("pnrNumber", pnrValue);
            getContext().startService(iPNR);
            pnrSearchField.clearFocus();
            pnrSearchField.setText("");
            InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pnrSearchField.getWindowToken(), 0);
        });
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
                        helper.close();

                        ArrayList<Pnr_Api_Response_Structure> latestData = new PNR_Data_DB_Helper(getContext()).getPnrDataFromDB();
                        getActivity().runOnUiThread(() ->{
                            if (adapter == null) {
                                adapter = new All_Pnr_Data_Recycler_View_Adapter(getContext(), latestData);
                                allPnrCheckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                allPnrCheckRecyclerView.setAdapter(adapter);
                            } else {
                                adapter = new All_Pnr_Data_Recycler_View_Adapter(getContext(), latestData);
                                allPnrCheckRecyclerView.setAdapter(adapter);
                            }

                        });
                    }).start();
                    db.close();


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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().registerReceiver(broadcastReceiver, new IntentFilter("PNR_RESPONSE_ACTION"), Context.RECEIVER_NOT_EXPORTED);
            Log.d("pnrWork", "onAttachedToWindow: receiver registered");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try{
            requireContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            Log.d("detach", "onDetach error: " + e);
        }
    }
}
