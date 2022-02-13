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

  
    public static MessageListener messageListener;
    public static final String TAG = "FindNearby";
    public static String nearbyMessage;
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
        intent.putExtra("profile_picture_url", "https://lh3.googleusercontent.com/c44HXj2-lQgykjwSvTt4MboXj51qMieDWk8ePaYDCQa5-Hsd9HmQyyB-1j17xfyaFTe9AV_B2kt0vdxl33-_PVt8Hl9OCTN2Ajah-Q-UN7vNi-rFofQeGtcaN4dfWtwogctirbt1X400W62e0Hwb27ezEvrUGEuiCdcWtemY6I-DXin7jA8uUHfpn39m7mHZxZLTJGv6agZk27Qu6vYLb2xKRNy7oPjopO5VqQ4q24nrzXhAftM42P9rDv9PqQ4uk-4b3LPCo8MUOWpPyljNsfJZwiuKkwE_QHbmT28FEhLWp2Hj55fqj_NK5bz0w-ubWl5TjZJWrAxNYVz0Ci41-KqDO8ziefTBpbeSA-B1eN-w6PDQvn34I4bnV_NeihGBLZRNap4rpkk4_k4iD2wZEWOiexzCHE0KPrp8wzTzbWbauIPkIzqvKWCjGMswrqE5zjLvmlqGHI7FsItPPLylm6iLx6hlvPWlJOgzoNCwBiNcgg4LPwsKNByTPprLF9Gw3BeZHVRcZisPiXzzMHIVqZHcTYNWAoQyD_tXz8F5rJIisXv27wrD3QeLxgediWDt5d4LRuQdYRePyGe04if-9GZDlgLiggThm-Bxj03oW8Tvw5nGb2EyAE9MlNKzo3SZosY9KQJ7ZwAuXyfQK1U2EDdG1qDTC9vWZzdZmc5_AZqYQVw177KtaXUZ6q6pAxpajh_8p4ninlnFWMlOK1p0B_gqHznk5qu4PleJWgWWBX6rRnuuYSEiwrUkPjo-CuCYT3Dnp0yf2YvufGHg=w96-h72-no");
        context.startActivity(intent);
    }

}