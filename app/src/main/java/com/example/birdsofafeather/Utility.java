package com.example.birdsofafeather;

import android.app.Activity;
import android.app.AlertDialog;

public class Utility {
    public static void showAlert(Activity activity, String message){
        AlertDialog.Builder alterBuilder = new AlertDialog.Builder(activity);

        alterBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {dialog.cancel();})
                .setCancelable(true);

        AlertDialog alertDialog = alterBuilder.create();
        alertDialog.show();
    }
}
