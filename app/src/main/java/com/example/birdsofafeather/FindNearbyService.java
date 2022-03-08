package com.example.birdsofafeather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindNearbyService extends Service {
    public static final String TAG = "FindNearbyService";
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
                    MessageListener realListener = new MessageListener() {
                        @Override
                        public void onFound(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Found message: " + new String(message.getContent()));
                        }

                        @Override
                        public void onLost(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Lost sign of message: " + new String(message.getContent()));
                        }
                    };
                    FindNearbyActivity.messageListener = new FakedMessageListener(realListener, 3, FindNearbyActivity.nearbyMessage);
                    wait(300000);
                    Nearby.getMessagesClient(getApplicationContext()).subscribe(FindNearbyActivity.messageListener);
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
        ((FakedMessageListener)(FindNearbyActivity.messageListener)).stopMessages();
        Nearby.getMessagesClient(getApplicationContext()).unsubscribe(FindNearbyActivity.messageListener);
        Toast.makeText(FindNearbyService.this, "Stop Finding Nearby Users", Toast.LENGTH_SHORT).show();
        stopSelf();
        super.onDestroy();
    }
}