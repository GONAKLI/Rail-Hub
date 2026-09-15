package com.gonakli.railHub.HomeActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.gonakli.railHub.ADAPTERS.All_Pnr_Data_Recycler_View_Adapter;
import com.gonakli.railHub.DB_WORK.PNR_Data_DB_Helper;
import com.gonakli.railHub.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Services.API_Call.PNR_Enquiry_API_CALL;
import com.gonakli.railHub.Structure_Class.Pnr_Api_Response_Structure;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Fragment_Pnr_Screen extends Fragment {
    View view;
    AppCompatEditText pnrSearchField;
    AppCompatButton btnFindPnr;
    LinearLayout pnrSearchContainerBox;
    RecyclerView allPnrCheckRecyclerView;
    All_Pnr_Data_Recycler_View_Adapter adapter;
    Intent iPnrApiService;
    Context context;
    LottieAnimationView LoadingAnimationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("PNR Status");
        view = LayoutInflater.from(getActivity()).inflate(R.layout.pnr_check, container, false);
        find_all_id();
        context = getContext();
        onSubmitAction();
        recyclerView_setup();
        return view;
    }

    private void recyclerView_setup() {
        startAnimation();
        new Thread(() -> {
            PNR_Data_DB_Helper helper = new PNR_Data_DB_Helper(getContext());
            ArrayList<Pnr_Api_Response_Structure> arrPnrData = helper.getPnrDataFromDB();
            helper.close();
            if (arrPnrData != null && !arrPnrData.isEmpty()) {
                adapter = new All_Pnr_Data_Recycler_View_Adapter(getContext(), arrPnrData);
                requireActivity().runOnUiThread(() -> {
                    allPnrCheckRecyclerView.setAdapter(adapter);
                    allPnrCheckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    stopAnimation();
                });

            }

        }).start();


    }


    private void find_all_id() {
        pnrSearchField = view.findViewById(R.id.pnr_search_field);
        btnFindPnr = view.findViewById(R.id.btn_find_pnr);
        allPnrCheckRecyclerView = view.findViewById(R.id.allPnrCheckRecyclerView);
        pnrSearchContainerBox = view.findViewById(R.id.pnrSearchContainerBox);
        LoadingAnimationView = view.findViewById(R.id.pnrLoadingAnimation);
    }

    protected void onSubmitAction() {
        btnFindPnr.setOnClickListener(v -> {
            String pnrValue = pnrSearchField.getText().toString().trim();
            if (pnrValue.length() < 10) {
                Toast.makeText(getContext(), "Pnr should be a valid 10 digit number", Toast.LENGTH_LONG).show();
                return;
            }
            iPnrApiService = new Intent(getContext(), PNR_Enquiry_API_CALL.class);
            iPnrApiService.putExtra("pnrNumber", pnrValue);
            if (context != null) context.startService(iPnrApiService);
            pnrSearchField.clearFocus();
            pnrSearchField.setText("");
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pnrSearchField.getWindowToken(), 0);
            startAnimation();
        });
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopAnimation();
            String action = intent.getAction();
            if (PNR_Enquiry_API_CALL.PNR_RESPONSE.equalsIgnoreCase(action)) {
                managePnrFromApi(intent);
            } else if (PNR_Enquiry_API_CALL.INTERNET_ISSUE.equalsIgnoreCase(action)) {
                showSnackBar("Check your internet connection and then try again");
            } else if (PNR_Enquiry_API_CALL.INTERNAL_APPLICATION_ERROR.equalsIgnoreCase(action)) {
                showSnackBar("Something went wrong, please try again later");
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (iPnrApiService != null) {
            context.stopService(iPnrApiService);
        }
    }

    private void showSnackBar(String msg) {
        if (isAdded() && getActivity() != null) {
            View mySnackView = requireActivity().findViewById(android.R.id.content);
            if (mySnackView != null) {
                Snackbar snackbar = Snackbar.make(mySnackView, msg, Snackbar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(3000);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setAnchorView(pnrSearchContainerBox);
                View snackView = snackbar.getView();
                TextView snackTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                snackTextView.setTextSize(15);
                snackTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                snackView.setLayoutParams(params);
            }
        }


    }

    private void managePnrFromApi(Intent intent) {
        if (isAdded()) {
            try {
                Station_List_DB_Helper db = new Station_List_DB_Helper(requireContext().getApplicationContext());
                Pnr_Api_Response_Structure resData = (Pnr_Api_Response_Structure) intent.getSerializableExtra("pnrResponse");
                boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
                if (resData != null && resData.isSuccess()) {
                    new Thread(() -> {
                        PNR_Data_DB_Helper helper = new PNR_Data_DB_Helper(getContext());
                        if (isRefresh) {
                            helper.updatePnrDataInDB(resData);
                        } else {
                            helper.addPnrPassengerInDB(resData);
                        }

                        helper.close();

                        ArrayList<Pnr_Api_Response_Structure> latestData = new PNR_Data_DB_Helper(getContext()).getPnrDataFromDB();
                        getActivity().runOnUiThread(() -> {
                            if (adapter == null) {
                                adapter = new All_Pnr_Data_Recycler_View_Adapter(getContext(), latestData);
                                allPnrCheckRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                allPnrCheckRecyclerView.setAdapter(adapter);
                            } else {
                                adapter.refreshAdapter();
                            }

                            if (isRefresh) {
                                allPnrCheckRecyclerView.smoothScrollToPosition(0);
                                Toast.makeText(getContext(), "Pnr refreshed", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }).start();
                    db.close();


                } else {
                    if (resData != null && !resData.isSuccess()) {
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
            } catch (NullPointerException e) {
                Log.d("nullPointerinPNR", "managePnrFromApi: " + e.getMessage());
            }
        }

    }

    private void startAnimation() {
        if (LoadingAnimationView != null) {
            LoadingAnimationView.setVisibility(View.VISIBLE);
            LoadingAnimationView.setRepeatMode(LottieDrawable.RESTART);
            LoadingAnimationView.setRepeatCount(LottieDrawable.INFINITE);
            LoadingAnimationView.setAnimation(R.raw.loading_color_dots);
            LoadingAnimationView.playAnimation();
        }
    }

    private void stopAnimation() {
        if (LoadingAnimationView != null) {
            LoadingAnimationView.cancelAnimation();
            LoadingAnimationView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PNR_Enquiry_API_CALL.PNR_RESPONSE);
        filter.addAction(PNR_Enquiry_API_CALL.INTERNET_ISSUE);
        filter.addAction(PNR_Enquiry_API_CALL.INTERNAL_APPLICATION_ERROR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(broadcastReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        }
    }
}
