package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birdsofafeather.db.user.UserWithCourses;
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

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);
        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        MockUserWithCourses John = new MockUserWithCourses(0);
        MockUserWithCourses Amy = new MockUserWithCourses(1);
        MockUserWithCourses Zoey = new MockUserWithCourses(2);
        nearbyMessage = "*" + John + "*" + Amy + "*" + Zoey;


        List<UserWithCourses> dataList = new ArrayList<UserWithCourses>();
        dataList.add(John.getUserWithCourses());
        dataList.add(Amy.getUserWithCourses());
        dataList.add(Zoey.getUserWithCourses());

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(dataList);
        personsRecyclerView.setAdapter(personsViewAdapter);


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

    public ArrayList<String> getNames(String nearbyMessage){
        ArrayList<String> names =  new ArrayList<String>(0);
        int i = 0;
        int j = 0;

        //Search Name
        while(1>0){
            i = nearbyMessage.indexOf("*", i);
            j = nearbyMessage.indexOf(":", j);
            if(i < 0 || j < 0){break;}
            else{
                String name = nearbyMessage.substring(i+1, j);
                names.add(name);
                i++;
                j++;
            }
        }
        return names;
    }

    public ArrayList<ArrayList<String>> getCourses(String nearbyMessage){
        int i = 0;
        int j = 0;
        ArrayList<String> names = getNames(nearbyMessage);
        ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>(names.size());
        //Search courses
        ArrayList<String> al = new ArrayList<String>(0);
        while(1>0){
            i = nearbyMessage.indexOf("|", j);
            j = nearbyMessage.indexOf("|", i+1);
            if(i < 0 || j < 0){break;}
            else{
                String course = nearbyMessage.substring(i+1, j);
                al.add(course);
                //System.out.println("al:"+al.toString());
                if(j == nearbyMessage.length()-1){
                    ArrayList<String> temp = new ArrayList<String>(0);
                    //deep copy
                    for(int x = 0; x < al.size(); x++){
                        temp.add(al.get(x));
                    }
                    courses.add(temp);
                    al.clear();
                    break;
                }
                if(nearbyMessage.charAt(j+1) == '*'){
                    ArrayList<String> temp = new ArrayList<String>(0);
                    for(int x = 0; x < al.size(); x++){
                        temp.add(al.get(x));
                    }
                    courses.add(temp);
                    al.clear();
                    j++;
                }
            }
        }
        return courses;
    }
}