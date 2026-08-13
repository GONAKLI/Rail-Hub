package com.gonakli.railradar.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Services.API_Call.PNR_Enquiry_API_CALL;

public class Fragment_Pnr_Screen extends Fragment {
    View view;
    AppCompatEditText pnrSearchField;
    AppCompatButton btnFindPnr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      getActivity().setTitle("PNR Status");
       view = LayoutInflater.from(getActivity()).inflate(R.layout.pnr_check, container,false);
       find_all_id();
       onSubmitAction();
       return view;
    }


    private void find_all_id() {
        pnrSearchField = view.findViewById(R.id.pnr_search_field);
        btnFindPnr = view.findViewById(R.id.btn_find_pnr);
    }

    private void onSubmitAction() {
        btnFindPnr.setOnClickListener(v ->{
            String pnrValue = pnrSearchField.getText().toString().trim();
            Toast.makeText(getContext(), ""+ pnrValue, Toast.LENGTH_SHORT).show();
            Intent iPNR = new Intent(getContext(), PNR_Enquiry_API_CALL.class);
            iPNR.putExtra("pnrNumber", pnrValue);
            getContext().startService(iPNR);
            pnrSearchField.clearFocus();
            InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pnrSearchField.getWindowToken(), 0);
        });
    }
}
