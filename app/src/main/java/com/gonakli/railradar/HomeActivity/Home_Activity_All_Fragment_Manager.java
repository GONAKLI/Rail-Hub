package com.gonakli.railradar.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gonakli.railradar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Activity_All_Fragment_Manager extends Fragment {
    View combined_Home_Screen;
    FrameLayout dynamicHomeFrameLayout;
    BottomNavigationView bottomNavigationView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    combined_Home_Screen = inflater.inflate(R.layout.combined_home_screen_dashboard, container, false);
        find_all_id();
        set_Up_FrameLayout();

        BottomNavigationClickManage();
    return combined_Home_Screen;
    }

   private void find_all_id(){
        bottomNavigationView = combined_Home_Screen.findViewById(R.id.bottomNavigationView);
    }



   public void set_Up_FrameLayout(){

       FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if( fragmentManager.findFragmentById(R.id.frameLayout1) == null){
            fragmentTransaction.add(R.id.frameLayout1, new Fragment_Home_Screen());
            fragmentTransaction.commit();
        }

    }

    private void BottomNavigationClickManage(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


     bottomNavigationView.setOnItemSelectedListener(v ->{
         if(v.getItemId() == R.id.history){

             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.frameLayout1, new Fragment_History_Screen());
             fragmentTransaction.commit();
         } else if (v.getItemId() == R.id.home_activity) {

             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.frameLayout1, new Fragment_Home_Screen());
             fragmentTransaction.commit();
         } else if (v.getItemId() == R.id.pnrSearch) {
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.frameLayout1, new Fragment_Pnr_Screen());
             fragmentTransaction.commit();
             
         } else if (v.getItemId() == R.id.bookTicket) {
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.frameLayout1, new Fragment_Ticket_Screen());
             fragmentTransaction.commit();
             
         }
         return true;
     });


    }


}
