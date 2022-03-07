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
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.CourseComparison;
import com.example.birdsofafeather.utils.Utilities;
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Array;
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
    private long curr_session_id;
    private UserWithCourses me;
    private SessionWithUsers currSession;
    private static final String TAG = "FindNearbyActivity";
    private List<UserWithCourses> recordedDataList = new ArrayList<UserWithCourses>();
    List<UserWithCourses> validDataList;

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    private List<UserWithCourses> sortedDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);


        Intent intent = getIntent();
        test_user_id = intent.getIntExtra("user_id", -1);
        curr_session_id = intent.getLongExtra("session_id", -1);
        db = AppDatabase.singleton(this);
        me = db.userWithCoursesDao().getUser(test_user_id);
        currSession = db.sessionWithUsersDao().getForId(curr_session_id);
        myCourseList= me.getCourses();

        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        stop.setVisibility(View.INVISIBLE);

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        sortedDataList = new ArrayList<>();
//        List<User> users = db.sessionWithUsersDao().getUsersForSessionId(curr_session_id);
        List<User> users = currSession.getUsers();
        List<UserWithCourses> uWCourses = new ArrayList<>();
        for (User user : users) {
            UserWithCourses newUWCourse = new UserWithCourses();
            newUWCourse.user = user;
            newUWCourse.courses = db.userWithCoursesDao().getUser(user.getId()).getCourses();
            uWCourses.add(newUWCourse);
        }
        sortedDataList.addAll(uWCourses);

        personsViewAdapter = new PersonsViewAdapter(sortedDataList);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

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



        validDataList= new ArrayList<UserWithCourses>();
        nearbyMessage = "";
        int numStudents = (int)(4);
        for(int i = 0; i < numStudents; i++){
            dataList.add(students.get(i).getUserWithCourses());
            nearbyMessage += "*" + students.get(i);
        }
        List<Integer> sameCourseList = new ArrayList<Integer>();
        for(int i = 0; i < dataList.size(); i++) {
            Boolean isClassMate = false;
            int numSameCourses = 0;
            List<Course> userCourses = dataList.get(i).getCourses();
            for(int j = 0; j < myCourseList.size(); j++) {
                for(int k = 0; k < userCourses.size(); k++) {
                    if(CourseComparison.compareCourses(myCourseList.get(j), userCourses.get(k))) {
                        isClassMate = true;
                        numSameCourses += 1;
                        dataList.get(i).incrementNumSamCourses();
                    }
                }
            }
            if(isClassMate) {
                validDataList.add(dataList.get(i));
                db.userWithCoursesDao().insert(dataList.get(i).user);
                for (Course course : dataList.get(i).courses) {
                    db.coursesDao().insert(course);
                }
            }
        }

        for(int i = 0; i < validDataList.size(); i++) {
            Boolean isUnique = true;
            for(int j = 0; j < this.recordedDataList.size(); j++) {
                if(validDataList.get(i).getName().equals(this.recordedDataList.get(j).getName())) {
                    isUnique = false;
                }
            }
            if(isUnique) {
                this.recordedDataList.add(validDataList.get(i));
            }
        }

        int iterations = this.recordedDataList.size();
        for(int i = 0; i < iterations; i++){
            int max = 0;
            int maxIndex = 0;
            for(int j =0; j<this.recordedDataList.size(); j++) {
                if(this.recordedDataList.get(j).getNumSamCourses() >max) {
                    max = this.recordedDataList.get(j).getNumSamCourses();
                    maxIndex = j;
                }
            }
            if(!sortedDataList.contains(this.recordedDataList.get(maxIndex))) {
                sortedDataList.add(this.recordedDataList.get(maxIndex));
                personsViewAdapter.notifyItemInserted(sortedDataList.size() - 1);
            }
            this.recordedDataList.remove(maxIndex);
        }
        for(int i = 0; i < sortedDataList.size(); i++) {
            this.recordedDataList.add(sortedDataList.get(i));
        }

//        personsViewAdapter = new PersonsViewAdapter(sortedDataList);
//        personsRecyclerView.setAdapter(personsViewAdapter);
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

        AppDatabase db = AppDatabase.singleton(this);
        for(UserWithCourses user : sortedDataList) {
            user.user.setSessionId(curr_session_id);
            db.userWithCoursesDao().insert(user.user);
        }

        if(currSession.getSessionName() == null) {
            Intent intentSave = new Intent(FindNearbyActivity.this, Pop_save.class);
            intentSave.putExtra("user_id", test_user_id);
            ArrayList<Integer> user_ids = new ArrayList<>();
            for (int i = 0; i < this.validDataList.size(); ++i) {
                user_ids.add(this.validDataList.get(i).user.getId());
            }
            intentSave.putIntegerArrayListExtra("user_ids", user_ids);
            startActivity(intentSave);
        }

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