package com.gonakli.railradar.DrawerItemsWork;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gonakli.railradar.ADAPTERS.User_History_ListView_Adapter;
import com.gonakli.railradar.DB_WORK.User_Routes_History_DB_Helper;
import com.gonakli.railradar.HomeActivity.Fragment_History_Screen;
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
            SharedPreferences sharedPreferences = context.getSharedPreferences("applicationTheme", Context.MODE_PRIVATE);
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.change_theme_modal);
            AppCompatRadioButton darkRadio, lightRadio, systemRadio;
            darkRadio = dialog.findViewById(R.id.modalThemeDarkRadio);
            lightRadio = dialog.findViewById(R.id.modalThemeLightRadio);
            systemRadio = dialog.findViewById(R.id.modalThemeSystemRadio);
            if(sharedPreferences.getBoolean("isDark", false)){
                darkRadio.setChecked(true);
            } else if (sharedPreferences.getBoolean("isLight", false)) {
                lightRadio.setChecked(true);
            } else {
                systemRadio.setChecked(true);
            }


            modalThemeApplyBtn = dialog.findViewById(R.id.modalThemeApplyBtn);
            modalThemeCancelBtn = dialog.findViewById(R.id.modalThemeCancelBtn);
            modalThemeRadioGroup = dialog.findViewById(R.id.modalThemeRadioGroup);
            dialog.show();

            modalThemeCancelBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });

            // application theme change work Start here
            modalThemeApplyBtn.setOnClickListener(v -> {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                int selectedRadio = modalThemeRadioGroup.getCheckedRadioButtonId();

                if(selectedRadio == R.id.modalThemeLightRadio){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isLight", true);
                    editor.putBoolean("isDark", false);
                } else if (selectedRadio == R.id.modalThemeDarkRadio) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDark", true);
                    editor.putBoolean("isLight", false);
                } else if (selectedRadio == R.id.modalThemeSystemRadio) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    editor.putBoolean("isDark", false);
                    editor.putBoolean("isLight", false);
                }
                editor.apply();
                dialog.dismiss();
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
        } else if (item.getItemId() == R.id.clearSearchHistory) {
            new Thread(() -> {
                User_Routes_History_DB_Helper helper = new User_Routes_History_DB_Helper(context);
                helper.deleteHistory();
                helper.close();

                new Handler(Looper.getMainLooper()).post(() ->{
                    Fragment currentFragment = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.frameLayout1);
                    if(currentFragment instanceof Fragment_History_Screen){
                        ((Fragment_History_Screen) currentFragment).refreshData();
                    }

                    Toast.makeText(context, "History Deleted Successfully", Toast.LENGTH_SHORT).show();

                });

            }).start();
        }
    }

    private void find_All_ID() {


    }


}
