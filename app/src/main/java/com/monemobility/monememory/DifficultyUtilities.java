package com.monemobility.monememory;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

public class DifficultyUtilities {
    public static void displayDifficultyToast(Context context, View view, int progress) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = 1000;
        Toast difficultyToast = Toast.makeText(context, String.valueOf(progress), Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                difficultyToast.show();
            }
            public void onFinish() {
                difficultyToast.cancel();
            }
        };

        // Show the toast and starts the countdown
        difficultyToast.show();
        toastCountDown.start();
    }
}
