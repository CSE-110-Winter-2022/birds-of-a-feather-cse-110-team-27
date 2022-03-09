package com.example.birdsofafeather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaveService extends Service {
    public static final String TAG = "WaveService";
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public WaveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Start Finding Nearby Users", Toast.LENGTH_SHORT).show();
        executor.submit(() -> {
            synchronized (this) {
                try {
                    MessageListener realListener = new MessageListener() {
                        @Override
                        public void onFound(@NonNull Message message) {
                            Log.d(WaveService.TAG, "Found message: " + new String(message.getContent()));
                        }

                        @Override
                        public void onLost(@NonNull Message message) {
                            Log.d(WaveService.TAG, "Lost sign of message: " + new String(message.getContent()));
                        }
                    };
                    FindNearbyActivity.waveMessageListener = new FakedMessageListener(realListener, 1, "B");
                    wait(300000);
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
        ((FakedMessageListener)(FindNearbyActivity.waveMessageListener)).executor.shutdown();
        Toast.makeText(this, "Stop Waving", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}