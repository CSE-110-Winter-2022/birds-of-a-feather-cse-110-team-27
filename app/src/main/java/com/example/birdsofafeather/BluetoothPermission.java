package com.example.birdsofafeather;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


public class BluetoothPermission extends AppCompatActivity {
    public static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public static boolean bluetoothEnabled(){
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public static void requestEnableBluetooth(AppCompatActivity app){
        if(!bluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            app.startActivity(intent);
        }
    }
}
