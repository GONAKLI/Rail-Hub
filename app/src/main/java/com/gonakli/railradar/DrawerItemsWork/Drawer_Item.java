package com.gonakli.railradar.DrawerItemsWork;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gonakli.railradar.HomeActivity.Home_Screen_Activity;
import com.gonakli.railradar.R;
import com.google.android.material.navigation.NavigationView;

public class Drawer_Item {
    Context context;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView modalThemeCancelBtn, modalThemeApplyBtn;
    RadioGroup modalThemeRadioGroup;


    public Drawer_Item(Context context, DrawerLayout drawerLayout, NavigationView navigationView){
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        entryPoint();
    }


    public void entryPoint() {


        navigationView.setNavigationItemSelectedListener(item ->{
            drawerLayout.close();
            Work_On_Nav_Item_Click(item);

            return true;
        });
    }

    private void Work_On_Nav_Item_Click(MenuItem item) {
        if(item.getItemId() == R.id.applicationTheme){
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.change_theme_modal);
            modalThemeApplyBtn = dialog.findViewById(R.id.modalThemeApplyBtn);
            modalThemeCancelBtn = dialog.findViewById(R.id.modalThemeCancelBtn);
            modalThemeRadioGroup = dialog.findViewById(R.id.modalThemeRadioGroup);
            dialog.show();

            modalThemeCancelBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });

            // application theme change work Start here
            modalThemeApplyBtn.setOnClickListener(v -> {
                int selectedRadio = modalThemeRadioGroup.getCheckedRadioButtonId();

                if(selectedRadio == R.id.modalThemeLightRadio){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (selectedRadio == R.id.modalThemeDarkRadio) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (selectedRadio == R.id.modalThemeSystemRadio) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
            });
            // application theme change work End here

        } //application theme If statement ends here
        else if (item.getItemId() == R.id.applicationRateUs) {
            // Rate on play Store
            try{
                Intent play = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.gonakli.railradar"));
                play.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(play);
                Toast.makeText(context, "⇣ ⇣  Scroll Down To Rate Us ⇣ ⇣ ", Toast.LENGTH_SHORT).show();
            } catch (android.content.ActivityNotFoundException e){
                Intent play = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gonakli.railradar"));
                play.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(play);
                Toast.makeText(context, "⇣ ⇣  Scroll Down To Rate Us ⇣ ⇣ ", Toast.LENGTH_SHORT).show();
            }

        } // end of play store rating condition
        else if (item.getItemId() == R.id.applicationFeedback) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.feedback_modal);
            dialog.show();
        }
    }

    private void find_All_ID() {


    }


}
