package com.example.birdsofafeather;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindNearbyService extends Service {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public FindNearbyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(FindNearbyService.this, "Start Finding Nearby Users", Toast.LENGTH_SHORT).show();
        executor.submit(() -> {
            synchronized (this) {
                try {
                    wait(300000);

                    //Update the recycler view

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(startId);
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(FindNearbyService.this, "Stop Finding Nearby Users", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}