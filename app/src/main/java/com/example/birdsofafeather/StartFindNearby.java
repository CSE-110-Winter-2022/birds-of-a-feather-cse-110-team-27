package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class StartFindNearby extends AppCompatActivity {
    private Button start;
    private Button stop;
    public static MessageListener messageListener;
    public static final String TAG = "FindNearby";
    public static String nearbyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);
        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        MockUserWithCourses John = new MockUserWithCourses(0);
        MockUserWithCourses Amy = new MockUserWithCourses(1);
        MockUserWithCourses Zoey = new MockUserWithCourses(2);
        nearbyMessage = John + "\n" + Amy + "\n" + Zoey;


    }

    public void startClicked(View view){
        Intent intent = new Intent(StartFindNearby.this, FindNearbyService.class);
        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
        startService(intent);
    }

    public void stopClicked(View view){
        Intent intent = new Intent(StartFindNearby.this, FindNearbyService.class);
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        stopService(intent);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Nearby.getMessagesClient(this).subscribe(messageListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Nearby.getMessagesClient(this).unsubscribe(messageListener);
//    }
}