package com.gonakli.railHub.Utility.VibrationEffect;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class Get_Vibration {
    public void getVibrationEffect(Long effectDuration, int amplitude, Context context) {
        if(context == null) return;

        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Getting Vibration manager service
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            // Getting mobile vibrator
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            // if android version is 11 or lower, it will be used
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        // safety check in case vibrator is not present in device or null in worst case
        if (vibrator == null || !vibrator.hasVibrator()) return;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //  adding vibration effect duration and intensity
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(effectDuration, amplitude);
            // start vibrating
            vibrator.vibrate(vibrationEffect);
        } else {
            // if android version is 7 or lower, it will be used
            vibrator.vibrate(effectDuration);
        }
    }
}
