package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.utils.Utilities;
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
    private int test_user_id;

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

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

        personsRecyclerView.setVisibility(View.INVISIBLE);


    }

    public void startClicked(View view){
        Intent intent = new Intent(StartFindNearby.this, FindNearbyService.class);
        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
        personsRecyclerView.setVisibility(View.VISIBLE);
        startService(intent);
    }

    public void stopClicked(View view){
        Intent intent = new Intent(StartFindNearby.this, FindNearbyService.class);
        stop.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        stopService(intent);
    }


//    public void onShowProfileClicked(View view) {
//        AppDatabase db = AppDatabase.singleton(this);
//        UserWithCourses user = db.userWithCoursesDao().getUser(test_user_id);
//        if(user == null){
//            Utilities.showAlert(this,"SIGN IN FIRST");
//            return;
//        }
//        Context context = view.getContext();
//        Intent intent = new Intent(context, ProfileActivity.class);
//        intent.putExtra("name", user.getName());
//        ArrayList<String> course_names = new ArrayList<>();
//        for(Course course : user.getCourses())  {
//            course_names.add(String.format("%s %s %s %s", course.getDepartment(), course.getCourseNumber(), course.getQuarter(), course.getYear()));
//        }
//        intent.putStringArrayListExtra("course_names", course_names);
//        intent.putExtra("profile_picture_url", "https://lh3.googleusercontent.com/c44HXj2-lQgykjwSvTt4MboXj51qMieDWk8ePaYDCQa5-Hsd9HmQyyB-1j17xfyaFTe9AV_B2kt0vdxl33-_PVt8Hl9OCTN2Ajah-Q-UN7vNi-rFofQeGtcaN4dfWtwogctirbt1X400W62e0Hwb27ezEvrUGEuiCdcWtemY6I-DXin7jA8uUHfpn39m7mHZxZLTJGv6agZk27Qu6vYLb2xKRNy7oPjopO5VqQ4q24nrzXhAftM42P9rDv9PqQ4uk-4b3LPCo8MUOWpPyljNsfJZwiuKkwE_QHbmT28FEhLWp2Hj55fqj_NK5bz0w-ubWl5TjZJWrAxNYVz0Ci41-KqDO8ziefTBpbeSA-B1eN-w6PDQvn34I4bnV_NeihGBLZRNap4rpkk4_k4iD2wZEWOiexzCHE0KPrp8wzTzbWbauIPkIzqvKWCjGMswrqE5zjLvmlqGHI7FsItPPLylm6iLx6hlvPWlJOgzoNCwBiNcgg4LPwsKNByTPprLF9Gw3BeZHVRcZisPiXzzMHIVqZHcTYNWAoQyD_tXz8F5rJIisXv27wrD3QeLxgediWDt5d4LRuQdYRePyGe04if-9GZDlgLiggThm-Bxj03oW8Tvw5nGb2EyAE9MlNKzo3SZosY9KQJ7ZwAuXyfQK1U2EDdG1qDTC9vWZzdZmc5_AZqYQVw177KtaXUZ6q6pAxpajh_8p4ninlnFWMlOK1p0B_gqHznk5qu4PleJWgWWBX6rRnuuYSEiwrUkPjo-CuCYT3Dnp0yf2YvufGHg=w96-h72-no");
//        context.startActivity(intent);
//    }

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