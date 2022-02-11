package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Utilities;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class StartFindNearby extends AppCompatActivity {
    private Button start;
    private Button stop;
    private MessageListener messageListener;
    private static final String TAG = "FindNearby";
    public String nearbyMessage;
    private int test_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);

        Intent intent = getIntent();
        test_user_id = intent.getIntExtra("user_id", -1);

        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        MockUserWithCourses John = new MockUserWithCourses(0);
        MockUserWithCourses Amy = new MockUserWithCourses(1);
        MockUserWithCourses Zoey = new MockUserWithCourses(2);
        nearbyMessage = John + "\n" + Amy + "\n" + Zoey;

        MessageListener realListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost sign of message: " + new String(message.getContent()));
            }
        };

        this.messageListener = new FakedMessageListener(realListener, 10, nearbyMessage);
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
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    public void onShowProfileClicked(View view) {
        AppDatabase db = AppDatabase.singleton(this);
        UserWithCourses user = db.userWithCoursesDao().getUser(test_user_id);
        if(user == null){
            Utilities.showAlert(this,"SIGN IN FIRST");
            return;
        }
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("name", user.getName());
        ArrayList<String> course_names = new ArrayList<>();
        for(Course course : user.getCourses())  {
            course_names.add(String.format("%s %s %s %s", course.getDepartment(), course.getCourseNumber(), course.getQuarter(), course.getYear()));
        }
        intent.putStringArrayListExtra("course_names", course_names);
        intent.putExtra("profile_picture_url", "https://photos.google.com/share/AF1QipNPV1l79QDbGgrHBCmzC5OKvKqQBDeiwL9Vhqt_cHmP117C03pdmrRO6kMOUjUB2Q?pli=1&key=cmVHRHlwQm9NWGt3cDlHcHNQWmNsMU5udlREOGtn");
        context.startActivity(intent);
    }
}