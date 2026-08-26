package com.gonakli.railradar.ThemePreference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.gonakli.railradar.R;

public class ThemeSelectionOnStartUp {
    Context context;
    TextView modalThemeCancelBtn, modalThemeApplyBtn;
    RadioGroup modalThemeRadioGroup;

    public ThemeSelectionOnStartUp(Context context) {
        this.context = context;
    }

    public void set_applicationTheme() {


    }

    public void show_theme_chooser_dialogue() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("applicationTheme", Context.MODE_PRIVATE);
        sharedPreferences.getBoolean("isDark", false);
        sharedPreferences.getBoolean("isLight", false);
        boolean isNewUser = sharedPreferences.getBoolean("isNewUser", true);
        if (isNewUser) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.change_theme_modal);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isNewUser", false);
                    editor.apply();
                }
            });
            modalThemeApplyBtn = dialog.findViewById(R.id.modalThemeApplyBtn);
            modalThemeCancelBtn = dialog.findViewById(R.id.modalThemeCancelBtn);
            modalThemeRadioGroup = dialog.findViewById(R.id.modalThemeRadioGroup);
            dialog.show();

            modalThemeCancelBtn.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNewUser", false);
                editor.apply();
                dialog.dismiss();
            });

            // application theme change work Start here
            modalThemeApplyBtn.setOnClickListener(v -> {
                dialog.dismiss();
                int selectedRadio = modalThemeRadioGroup.getCheckedRadioButtonId();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNewUser", false);
                if (selectedRadio == R.id.modalThemeLightRadio) {
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
            });
        }
    }
}
