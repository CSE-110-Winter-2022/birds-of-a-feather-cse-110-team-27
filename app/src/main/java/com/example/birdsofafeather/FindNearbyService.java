package com.example.birdsofafeather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.generator.Generator;
import com.example.birdsofafeather.generator.UserInfoCSVGenerator;
import com.example.birdsofafeather.generator.WaveCSVGenerator;
import com.example.birdsofafeather.parsers.NearbyUserParser;
import com.example.birdsofafeather.parsers.Parser;
import com.example.birdsofafeather.parsers.WaveParser;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindNearbyService extends Service {
    public static final String TAG = "FindNearbyService";
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private Parser parser;
    private final IBinder mBinder = new LocalBinder();
    private List<Long> mockUserIds = new ArrayList<>();
    private Message currMessage;
    private Generator nearbyMessageGenerator;
    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(20).build();
    private static final String MISSING_API_KEY = "It's possible that you haven't added your" +
            " API-KEY. See  " +
            "https://developers.google.com/nearby/messages/android/get-started#step_4_configure_your_project";



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
        Toast.makeText(FindNearbyService.this, "Start Finding Nearby Users", Toast.LENGTH_SHORT).show();
        long myUserId = intent.getLongExtra("user_id", -1);
        executor.submit(() -> {
            synchronized (this) {
                try {
                    MessageListener realListener = new MessageListener() {
                        @Override
                        public void onFound(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Found message: " + new String(message.getContent()));
                            String[] lines = new String(message.getContent()).split("\n");
                            String[] lastLine = lines[lines.length - 1].split(",");
                            if(lastLine[1].equals("wave")) {
                                parser = new WaveParser();
                            } else {
                                parser = new NearbyUserParser();
                            }
                            parser.parse(getApplicationContext(), new String(message.getContent()), FindNearbyService.this, myUserId);
                        }

                        @Override
                        public void onLost(@NonNull Message message) {
                            Log.d(FindNearbyService.TAG, "Lost sign of message: " + new String(message.getContent()));
                        }
                    };
                    FindNearbyActivity.findMessageListener = new FakedMessageListener(realListener, 3, FindNearbyActivity.nearbyUsersMessage);
//                    FindNearbyActivity.findMessageListener = realListener;
                    Log.d(TAG, "Subscribed to messages");
//                    SubscribeOptions options = new SubscribeOptions.Builder()
//                            .setStrategy(PUB_SUB_STRATEGY)
//                            .setCallback(new SubscribeCallback() {
//                                @Override
//                                public void onExpired() {
//                                    super.onExpired();
//                                    Log.i(TAG, "No longer subscribing");
//                                }
//                            }).build();
                    Nearby.getMessagesClient(getApplicationContext()).subscribe(FindNearbyActivity.findMessageListener);
                    setGenerator(new UserInfoCSVGenerator());
                    currMessage = new Message(nearbyMessageGenerator.generateCSV(this, myUserId, -1).getBytes(StandardCharsets.UTF_8));
//                    PublishOptions pubOptions = new PublishOptions.Builder()
//                            .setStrategy(PUB_SUB_STRATEGY)
//                            .setCallback(new PublishCallback() {
//                                @Override
//                                public void onExpired() {
//                                    super.onExpired();
//                                    Log.i(TAG, "No longer publishing");
//                                }
//                            }).build();
                    Nearby.getMessagesClient(this).publish(currMessage);
                    Log.d(TAG, "Published nearby message with content: " + new String(currMessage.getContent()));
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
        if(currMessage != null) {
            Log.d(TAG, "Unpublished nearby message with content: " + Arrays.toString(currMessage.getContent()));
            Nearby.getMessagesClient(this).unpublish(currMessage);
        }
        ((FakedMessageListener)(FindNearbyActivity.findMessageListener)).stopMessages();
        Log.d(TAG, "Unsubscribed from listening to messages");
        Nearby.getMessagesClient(getApplicationContext()).unsubscribe(FindNearbyActivity.findMessageListener);
        Toast.makeText(FindNearbyService.this, "Stop Finding Nearby Users", Toast.LENGTH_SHORT).show();
        stopSelf();
        super.onDestroy();
    }

    public void addUserId(long userId) {
        if(!mockUserIds.contains(userId)) {
            mockUserIds.add(userId);
        }
    }

    public List<Long> getMockUserIds() {
        return this.mockUserIds;
    }

    public void setGenerator(Generator generator) {
        this.nearbyMessageGenerator = generator;
    }


    public void updateCurMessage(Context context, long userID, long targetID) {
        String resultCSV = nearbyMessageGenerator.generateCSV(context, userID, targetID);
        Log.d(TAG, "Unpublished nearby message with content: " + Arrays.toString(currMessage.getContent()));
        Nearby.getMessagesClient(this).unpublish(currMessage);
        this.currMessage = new Message(resultCSV.getBytes(StandardCharsets.UTF_8));
        Nearby.getMessagesClient(this).publish(currMessage);
        Log.d(TAG, "Published nearby message with content: " + resultCSV);
    }
}