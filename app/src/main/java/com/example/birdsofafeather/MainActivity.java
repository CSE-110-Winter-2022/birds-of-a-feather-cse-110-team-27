package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Bluetooth.requestEnableBluetooth(this);
    }
}