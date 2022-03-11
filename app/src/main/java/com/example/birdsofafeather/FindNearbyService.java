package com.example.birdsofafeather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.parsers.NearbyUserParser;
import com.example.birdsofafeather.parsers.Parser;
import com.example.birdsofafeather.parsers.WaveParser;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindNearbyService extends Service {
    public static final String TAG = "FindNearbyService";
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private Parser parser;
    private final IBinder mBinder = new LocalBinder();
    private List<Long> mockUserIds = new ArrayList<>();


    public class LocalBinder extends Binder {
        FindNearbyService getService() {
            return FindNearbyService.this;
        }
    }

    public FindNearbyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String parser_type = intent.getStringExtra("parser_type");
        if(parser_type.equals("nearby_user")) {
            parser = new NearbyUserParser();
        } else if(parser_type.equals("wave")) {
            parser = new WaveParser();
        }
        Toast.makeText(FindNearbyService.this, "Start Finding Nearby Users", Toast.LENGTH_SHORT).show();
        executor.submit(() -> {
            synchronized (this) {
                try {
                    MessageListener realListener = new MessageListener() {
                        @Override
                        public void onFound(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Found message: " + new String(message.getContent()));
                            parser.parse(getApplicationContext(), new String(message.getContent()), FindNearbyService.this);
                        }

                        @Override
                        public void onLost(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Lost sign of message: " + new String(message.getContent()));
                        }
                    };
                    FindNearbyActivity.findMessageListener = new FakedMessageListener(realListener, 3, FindNearbyActivity.nearbyUsersMessage);
                    Nearby.getMessagesClient(getApplicationContext()).subscribe(FindNearbyActivity.findMessageListener);
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
//        mockUserIds.clear();
        ((FakedMessageListener)(FindNearbyActivity.findMessageListener)).stopMessages();
        Nearby.getMessagesClient(getApplicationContext()).unsubscribe(FindNearbyActivity.findMessageListener);
        Toast.makeText(FindNearbyService.this, "Stop Finding Nearby Users", Toast.LENGTH_SHORT).show();
        stopSelf();
        super.onDestroy();
    }

    public void addUserId(long userId) {
        if(!mockUserIds.contains(new Long(userId))) {
            mockUserIds.add(userId);
        }
        System.out.println();
    }

    public List<Long> getMockUserIds() {
        return this.mockUserIds;
    }

//    public void clearMockUserIds() { this.mockUserIds.clear();}
}