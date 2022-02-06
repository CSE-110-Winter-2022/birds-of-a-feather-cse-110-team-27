package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    public final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onStart() {
        super.onStart();
        requestEnableBluetooth();
    }

    public boolean bluetoothEnabled(){
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public void requestEnableBluetooth(){
        if(!bluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }
    }

}