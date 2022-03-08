package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.CheckUserSmallestSameCourse;
import com.example.birdsofafeather.utils.Constants;
import com.example.birdsofafeather.utils.CourseComparison;
import com.example.birdsofafeather.utils.CheckUserLastSameCourse;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.example.birdsofafeather.utils.Utilities;
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindNearbyActivity extends AppCompatActivity {
    private Button start;
    private Button stop;
    private AppDatabase db;
    List<Course> myCourseList;


    public static MessageListener messageListener;
    public static String nearbyMessage;
    private long test_user_id;
    private long curr_session_id;
    private UserWithCourses me;
    private SessionWithUsers currSession;
    private static final String TAG = "FindNearbyActivity";
    private List<UserWithCourses> recordedDataList = new ArrayList<UserWithCourses>();
    List<UserWithCourses> validDataList;
    public static List<Long> userIdsFromMockCSV = new ArrayList<>();

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    private List<UserWithCourses> sortedDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_find_nearby);


        Intent intent = getIntent();
        test_user_id = intent.getLongExtra("user_id", -1);
        curr_session_id = intent.getLongExtra("session_id", -1);
        db = AppDatabase.singleton(this);
        me = db.userWithCoursesDao().getUser(test_user_id);
        currSession = db.sessionWithUsersDao().getForId(curr_session_id);
        myCourseList= me.getCourses();

        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        stop.setVisibility(View.INVISIBLE);

        validDataList= new ArrayList<UserWithCourses>();
        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        sortedDataList = new ArrayList<>();
        List<User> users = currSession.getUsers();
        List<UserWithCourses> uWCourses = new ArrayList<>();
        for (User user : users) {
            UserWithCourses newUWCourse = new UserWithCourses();
            newUWCourse.user = user;
            newUWCourse.courses = db.userWithCoursesDao().getCoursesForUserId(user.getId());
            uWCourses.add(newUWCourse);
        }
        sortedDataList.addAll(uWCourses);

        personsViewAdapter = new PersonsViewAdapter(sortedDataList);
        personsRecyclerView.setAdapter(personsViewAdapter);


        Spinner quarterDropdown = findViewById(R.id.sort_options);
        DropdownAdapter sortSelectionAdapter = new DropdownAdapter(this, Constants.sortOptions);
        quarterDropdown.setAdapter(sortSelectionAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (personsRecyclerView != null) {
            for (int x = personsRecyclerView.getChildCount(), i = 0; i < x; ++i) {
                PersonsViewAdapter.ViewHolder holder = (PersonsViewAdapter.ViewHolder) personsRecyclerView.getChildViewHolder(personsRecyclerView.getChildAt(i));
                AppDatabase db = AppDatabase.singleton(this);
                UserWithCourses user = db.userWithCoursesDao().getUser(holder.person.getId());
                holder.setPerson(user);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }


    public void mockFindingNearbyUsers(){
//        MockUserWithCourses John = new MockUserWithCourses(0);
//        MockUserWithCourses Amy = new MockUserWithCourses(1);
//        MockUserWithCourses Zoey = new MockUserWithCourses(2);
//        MockUserWithCourses Matt = new MockUserWithCourses(3);

        List<UserWithCourses> dataList = new ArrayList<UserWithCourses>();
        List<UserWithCourses> students = new ArrayList<>();
        for(Long userId : userIdsFromMockCSV) {
            students.add(db.userWithCoursesDao().getUser(userId));
        }
        if(students.isEmpty()) {
            return;
        }
//        students.add(John);
//        students.add(Amy);
//        students.add(Zoey);
//        students.add(Matt);

        Spinner sort_dropdown = this.findViewById(R.id.sort_options);
        String sortOption = sort_dropdown.getSelectedItem().toString();


        nearbyMessage = "";
        int numStudents = (int)(4);
        for(int i = 0; i < numStudents; i++){
            students.get(i).user.setSessionId(curr_session_id);
//            students.get(i).student.user.setId(db.userWithCoursesDao().maxId() + 1); //del?
            dataList.add(students.get(i));
            for(Course course : students.get(i).courses) {
                course.userId = students.get(i).getId();
                course.courseId = db.coursesDao().maxId() + 1;
                db.coursesDao().insert(course);
            }
            System.out.println(db.coursesDao().getAll().size());
            nearbyMessage += "*" + students.get(i);
        }

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
                        int year = userCourses.get(k).getYear();
                        String quarter = userCourses.get(k).getQuarter();
                        String size = userCourses.get(k).getSize();
                        CheckUserLastSameCourse.updateUserLastSameCourseTime(dataList.get(i), year, quarter);
                        CheckUserSmallestSameCourse.updateUserSmallestSameCourse(dataList.get(i), size);
                    }
                }
            }
            if(isClassMate) {
                validDataList.add(dataList.get(i));

                db.userWithCoursesDao().insert(dataList.get(i).user);
                for (Course course : dataList.get(i).courses) {
                    course.userId = dataList.get(i).user.getId();
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
        if(sortOption.equals("Recency")) {
            int iterations = this.recordedDataList.size();
            for (int i = 0; i < iterations; i++) {
                int max = 0;
                int maxIndex = 0;
                for (int j = 0; j < this.recordedDataList.size(); j++) {
                    if (this.recordedDataList.get(j).getLastSameCourseTime() > max) {
                        max = this.recordedDataList.get(j).getLastSameCourseTime();
                        maxIndex = j;
                    }
                }
                sortedDataList.add(this.recordedDataList.get(maxIndex));
                personsViewAdapter.notifyItemInserted(sortedDataList.size() - 1);
                db.userWithCoursesDao().insert(this.recordedDataList.get(maxIndex).user);
                for (Course course : this.recordedDataList.get(maxIndex).courses) {
                    course.userId = this.recordedDataList.get(maxIndex).user.getId();
                    db.coursesDao().insert(course);
                }
                db.sessionWithUsersDao().addUsersToSession(currSession.getSession().getId(), Arrays.asList(this.recordedDataList.get(maxIndex).user));
                this.recordedDataList.remove(maxIndex);
            }
                for (int i = 0; i < sortedDataList.size(); i++) {
                    this.recordedDataList.add(sortedDataList.get(i));
                }
            }
        else if(sortOption.equals("# of same courses")) {
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
                sortedDataList.add(this.recordedDataList.get(maxIndex));
                personsViewAdapter.notifyItemInserted(sortedDataList.size() - 1);
                db.userWithCoursesDao().insert(this.recordedDataList.get(maxIndex).user);
                for (Course course : this.recordedDataList.get(maxIndex).courses) {
                    course.userId = this.recordedDataList.get(maxIndex).user.getId();
                    db.coursesDao().insert(course);
                }
                db.sessionWithUsersDao().addUsersToSession(currSession.getSession().getId(), Arrays.asList(this.recordedDataList.get(maxIndex).user));
                this.recordedDataList.remove(maxIndex);
            }
            for(int i = 0; i < sortedDataList.size(); i++) {
                this.recordedDataList.add(sortedDataList.get(i));
            }
        }
        else if(sortOption.equals("Class size")) {
            int iterations = this.recordedDataList.size();
            for(int i = 0; i < iterations; i++){
                int min = 999;
                int minIndex = 0;
                for(int j =0; j<this.recordedDataList.size(); j++) {
                    if(this.recordedDataList.get(j).getSmallestSameCourseSize() < min) {
                        min = this.recordedDataList.get(j).getSmallestSameCourseSize();
                        minIndex = j;
                    }
                }
                sortedDataList.add(this.recordedDataList.get(minIndex));
                personsViewAdapter.notifyItemInserted(sortedDataList.size() - 1);
                db.userWithCoursesDao().insert(this.recordedDataList.get(minIndex).user);
                for (Course course : this.recordedDataList.get(minIndex).courses) {
                    course.userId = this.recordedDataList.get(minIndex).user.getId();
                    db.coursesDao().insert(course);
                }
                db.sessionWithUsersDao().addUsersToSession(currSession.getSession().getId(), Arrays.asList(this.recordedDataList.get(minIndex).user));
                this.recordedDataList.remove(minIndex);
            }
            for(int i = 0; i < sortedDataList.size(); i++) {
                this.recordedDataList.add(sortedDataList.get(i));
            }
        }  else {

        }



//        personsViewAdapter = new PersonsViewAdapter(sortedDataList);
//        personsRecyclerView.setAdapter(personsViewAdapter);
    }


    public void startClicked(View view){
        Intent intent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
        mockFindingNearbyUsers();
        intent.putExtra("parser_type", "nearby_user");
        startService(intent);
        Log.d(this.TAG, "Started Nearby Service");
    }

    public void stopClicked(View view){
        Intent intent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
        stop.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
        stopService(intent);
        Log.d(this.TAG, "Stopped Nearby Service");

//        Intent intentSave = new Intent(FindNearbyActivity.this, Pop_save.class);
//        intentSave.putExtra("user_id",test_user_id);
//        ArrayList<Long> user_ids = new ArrayList<>();
//        for(int i = 0; i < this.validDataList.size(); ++i) {
//            user_ids.add(this.validDataList.get(i).user.getId());
//        }
//        intentSave.putExtra("user_ids", user_ids);
//        startActivity(intentSave);
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

    public void onMockBluetoothClicked(View view) {
        Intent intent = new Intent(FindNearbyActivity.this, EnterMockDataActivity.class);
        startActivity(intent);
    }

    private class DropdownAdapter extends ArrayAdapter {

        public DropdownAdapter(@NonNull Context context, String[] items) {
            super(context, android.R.layout.simple_spinner_dropdown_item, items);
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            } else {
                return true;
            }
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            } else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

    public void saveSession_onClick(View view) {
        if(!currSession.getSession().hasName()) {
            Intent intentSave = new Intent(FindNearbyActivity.this, Pop_save.class);
            intentSave.putExtra("user_id", test_user_id);
            ArrayList<Long> user_ids = new ArrayList<>();
            for (int i = 0; i < this.validDataList.size(); i++) {
                user_ids.add(this.validDataList.get(i).user.getId());
            }
            intentSave.putExtra("user_ids", user_ids);
            startActivity(intentSave);
        }
    }
}