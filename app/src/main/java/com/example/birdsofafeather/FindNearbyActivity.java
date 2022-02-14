package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class FindNearbyActivity extends AppCompatActivity {
    private Button start;
    private Button stop;
    private AppDatabase db;
    List<Course> myCourseList;


    public static MessageListener messageListener;
    public static String nearbyMessage;
    private int test_user_id;
    private UserWithCourses me;
    private static final String TAG = "FindNearbyActivity";

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);


        Intent intent = getIntent();
        test_user_id = intent.getIntExtra("user_id", -1);
        db = AppDatabase.singleton(this);
        me = db.userWithCoursesDao().getUser(test_user_id);
        myCourseList= me.getCourses();

        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        stop.setVisibility(View.INVISIBLE);
    }

    public void mockFindingNearbyUsers(){
        MockUserWithCourses John = new MockUserWithCourses(0);
        MockUserWithCourses Amy = new MockUserWithCourses(1);
        MockUserWithCourses Zoey = new MockUserWithCourses(2);
        MockUserWithCourses Matt = new MockUserWithCourses(3);

        List<UserWithCourses> dataList = new ArrayList<UserWithCourses>();
        List<MockUserWithCourses> students = new ArrayList<MockUserWithCourses>();
        students.add(John);
        students.add(Amy);
        students.add(Zoey);
        students.add(Matt);



        nearbyMessage = "";
        int numStudents = (int)(Math.random()*4 + 1);
        for(int i = 0; i < numStudents; i++){
            dataList.add(students.get(i).getUserWithCourses());
            nearbyMessage += "*" + students.get(i);
        }

        for(int i = 0; i < dataList.size(); i++) {
            Boolean isClassMate = false;
            List<Course> userCourses = dataList.get(i).getCourses();
            for(int j = 0; j < myCourseList.size(); j++) {
                for(int k = 0; k < userCourses.size(); k++) {
                    Boolean testDep = myCourseList.get(j).getDepartment().equals(userCourses.get(k).getDepartment());
                    Boolean testNum = myCourseList.get(j).getCourseNumber() == (userCourses.get(k).getCourseNumber());
                    Boolean testQrt = myCourseList.get(j).getQuarter().equals(userCourses.get(k).getQuarter());
                    Boolean testYea = myCourseList.get(j).getYear() == (userCourses.get(k).getYear());
                    if (testDep && testNum && testQrt && testYea) {
                        isClassMate = true;
                    }
                }
            }
            if(!isClassMate) {
                dataList.remove(i);
                i--;
            }
        }

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(dataList);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }

    public void startClicked(View view){
        Intent intent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
        mockFindingNearbyUsers();
        startService(intent);
        Log.d(this.TAG, "Started Nearby Service");
    }

    public void stopClicked(View view){
        Intent intent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
        stop.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        stopService(intent);
        Log.d(this.TAG, "Stopped Nearby Service");
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