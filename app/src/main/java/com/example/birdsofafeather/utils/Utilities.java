package com.example.birdsofafeather.utils;

import android.app.AlertDialog;
import android.content.Context;

import java.util.UUID;

public class Utilities {
    public static void showAlert(Context context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static String generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne.toString();
//        int uid=str.hashCode();
//        String filterStr=""+uid;
//        str=filterStr.replaceAll("-", "");
        return str;
    }
}
