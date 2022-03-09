package com.example.birdsofafeather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.parsers.Parser;
import com.example.birdsofafeather.parsers.WaveParser;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaveService extends Service {
    public static final String TAG = "WaveService";
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private Parser parser;

    public WaveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Start Looking For Waves", Toast.LENGTH_SHORT).show();
        parser = new WaveParser();
        executor.submit(() -> {
            synchronized (this) {
                try {
                    MessageListener realListener = new MessageListener() {
                        @Override
                        public void onFound(@NonNull Message message) {
                            Log.d(WaveService.TAG, "Found message: " + new String(message.getContent()));
                            parser.parse(getApplicationContext(), new String(message.getContent()), WaveService.this);
                        }

                        @Override
                        public void onLost(@NonNull Message message) {
                            Log.d(WaveService.TAG, "Lost sign of message: " + new String(message.getContent()));
                        }
                    };
                    FindNearbyActivity.waveMessageListener = new FakedMessageListener(realListener, 5, FindNearbyActivity.nearbyWave);
                    Nearby.getMessagesClient(getApplicationContext()).subscribe(FindNearbyActivity.waveMessageListener);
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
        Nearby.getMessagesClient(getApplicationContext()).unsubscribe(FindNearbyActivity.waveMessageListener);
         Toast.makeText(this, "Stop Looking For Waves", Toast.LENGTH_SHORT).show();
        stopSelf();
        super.onDestroy();
    }
}