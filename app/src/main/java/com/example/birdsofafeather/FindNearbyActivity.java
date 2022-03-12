package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
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
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.example.birdsofafeather.utils.Utilities;
import com.google.android.gms.nearby.messages.MessageListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FindNearbyActivity extends AppCompatActivity {
    private Button start;
    private Button stop;
    private AppDatabase db;
    List<Course> myCourseList;

    public static MessageListener findMessageListener;
//    public static MessageListener waveMessageListener;
    public static String nearbyUsersMessage;
//    public static String nearbyWave;
    private static final String TAG = "FindNearbyActivity";

    private long test_user_id;
    private long curr_session_id;
    private UserWithCourses me;
    private SessionWithUsers currSession;

    private List<UserWithCourses> recordedDataList = new ArrayList<UserWithCourses>();
    List<UserWithCourses> validDataList;
//    public static List<Long> userIdsFromMockCSV = new ArrayList<>();
//    private List<Long> storedUserIds = new ArrayList<>();

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;

    private List<UserWithCourses> sortedDataList;
    List<UserWithCourses> dataList;

    private FindNearbyService currentFindNearbyService;
    private boolean isBound;

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

        TextView UUIDTextView = this.findViewById(R.id.editUUIDTextView);
        UUIDTextView.setText(me.getUuid());

//        userIdsFromMockCSV.clear();
//        List<User> us = currSession.getUsers();
//        System.out.println();
//        for(User u : currSession.getUsers()) {
//            userIdsFromMockCSV.add(u.getId());
//        }

        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);

        stop.setVisibility(View.INVISIBLE);

        validDataList= new ArrayList<UserWithCourses>();
        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        sortedDataList = new ArrayList<>();
        dataList = new ArrayList<>();
        List<User> users = currSession.getUsers();
        List<UserWithCourses> uWCourses = new ArrayList<>();
        for (User user : users) {
            UserWithCourses newUWCourse = new UserWithCourses();
            newUWCourse.user = user;
            newUWCourse.courses = db.userWithCoursesDao().getCoursesForUserId(user.getId());
            uWCourses.add(newUWCourse);
        }
        sortedDataList.addAll(uWCourses);
        dataList.addAll(uWCourses);


        personsViewAdapter = new PersonsViewAdapter(sortedDataList, test_user_id, currentFindNearbyService);
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
            if(currSession != null && currSession.getSession().hasName()) {
                setTitle(currSession.getSessionName());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public static boolean containsCourse(Course course, List<Course> courseList){
        for(Course myCourse: courseList){
            if(myCourse.getDepartment().equals(course.getDepartment())
                    && myCourse.getCourseNumber() == course.getCourseNumber()
                    && myCourse.getQuarter().equals(course.getQuarter())
                    && myCourse.getYear() == course.getYear()){
                return true;
            }
        }
        return false;
    }
    public void sortRecency(){
        PriorityQueue<UserWithCourses> pq = new PriorityQueue<UserWithCourses>(new Comparator<UserWithCourses>() {
            public int compare(UserWithCourses user1, UserWithCourses user2) {
                Course mostRecent1 = null;
                Course mostRecent2 = null;

                for(Course course: user1.courses){
                    if(containsCourse(course, myCourseList)){
                        if(mostRecent1 == null || CourseComparison.compareCourseRecency(mostRecent1, course) < 0){
                            mostRecent1 = course;
                        }
                    }
                }
                for(Course course: user2.courses){
                    if(containsCourse(course, myCourseList)){
                        if(mostRecent2 == null || CourseComparison.compareCourseRecency(mostRecent2, course) < 0){
                            mostRecent2 = course;
                        }
                    }
                }

                return CourseComparison.compareCourseRecency(mostRecent1,mostRecent2);
            }
        });
        pq.addAll(sortedDataList);
        List<UserWithCourses> sortedByRecency = new ArrayList<>(pq);
        Collections.reverse(sortedByRecency);
        sortedDataList = sortedByRecency;
    }

    public void mockFindingNearbyUsers(){
        List<UserWithCourses> dataList = new ArrayList<UserWithCourses>();
        List<UserWithCourses> students = new ArrayList<>();
        List<Long> mockUserIds = currentFindNearbyService.getMockUserIds();
        for(Long userId : mockUserIds) {
            students.add(db.userWithCoursesDao().getUser(userId));
        }


        Spinner sort_dropdown = this.findViewById(R.id.sort_options);
        String sortOption = sort_dropdown.getSelectedItem().toString();


        // the first time it doesnt populate the array
        for(int i = 0; i < students.size(); i++){
            students.get(i).user.setSessionId(curr_session_id);
            dataList.add(students.get(i));
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
            for (int i = 0; i < recordedDataList.size() - 1; i++) {
                for (int j = 0; j < recordedDataList.size() - i - 1; j++) {
                    if (this.recordedDataList.get(j).getLastSameCourseTime() < this.recordedDataList.get(j + 1).getLastSameCourseTime()) {
                        UserWithCourses tmp = this.recordedDataList.get(j);
                        this.recordedDataList.set(j, this.recordedDataList.get(j + 1));
                        this.recordedDataList.set(j + 1, tmp);
                    }
                }
            }

            sortedDataList.clear();
            sortedDataList.addAll(this.recordedDataList);

        } else if(sortOption.equals("# of same courses")) {
            for (int i = 0; i < recordedDataList.size() - 1; i++) {
                for (int j = 0; j < recordedDataList.size() - i - 1; j++) {
                    if (this.recordedDataList.get(j).getNumSamCourses() < this.recordedDataList.get(j + 1).getNumSamCourses()) {
                        UserWithCourses tmp = this.recordedDataList.get(j);
                        this.recordedDataList.set(j, this.recordedDataList.get(j + 1));
                        this.recordedDataList.set(j + 1, tmp);
                    }
                }

            }

            sortedDataList.clear();
            sortedDataList.addAll(this.recordedDataList);

        } else {
            for (int i = 0; i < recordedDataList.size() - 1; i++) {
                for (int j = 0; j < recordedDataList.size() - i - 1; j++) {
                    if (this.recordedDataList.get(j).getSmallestSameCourseSize() > this.recordedDataList.get(j + 1).getSmallestSameCourseSize()) {
                        UserWithCourses tmp = this.recordedDataList.get(j);
                        this.recordedDataList.set(j, this.recordedDataList.get(j + 1));
                        this.recordedDataList.set(j + 1, tmp);
                    }
                }
            }

            sortedDataList.clear();
            sortedDataList.addAll(this.recordedDataList);
        }

        for(int i = 0; i < recordedDataList.size(); i++){
            UserWithCourses user = recordedDataList.get(i);
            if(user.user.wavedToMe) {
                recordedDataList.remove(i);
                sortedDataList.remove(i);
            }
        }

        for(UserWithCourses user : sortedDataList) {
//           user.user.setSessionId(currSession.getSession().getId());
           db.sessionWithUsersDao().addUsersToSession(currSession.getSession().getId(), Arrays.asList(user));

        }

        personsViewAdapter = new PersonsViewAdapter(sortedDataList, test_user_id, currentFindNearbyService);

        personsRecyclerView.setAdapter(personsViewAdapter);
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FindNearbyService.LocalBinder localBinder = (FindNearbyService.LocalBinder)iBinder;
            currentFindNearbyService = localBinder.getService();
            isBound = true;
//            while(currentFindNearbyService.getMockUserIds().isEmpty()) {
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mockFindingNearbyUsers();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    public void startClicked(View view){
        List<UserWithCourses> us = db.userWithCoursesDao().getAll();
        Intent startFindIntent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
//        Intent startWaveIntent = new Intent(FindNearbyActivity.this, WaveService.class);
        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
        startFindIntent.putExtra("user_id", me.getId());

        startService(startFindIntent);
//        startService(startWaveIntent);
        bindService(startFindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        mockFindingNearbyUsers();
        Log.d(this.TAG, "Started Nearby Service");
    }

    public void stopClicked(View view){
        Intent stopFindIntent = new Intent(FindNearbyActivity.this, FindNearbyService.class);
//        Intent stopWaveIntent = new Intent(FindNearbyActivity.this, WaveService.class);
        stop.setVisibility(View.INVISIBLE);
        start.setVisibility(View.VISIBLE);
//        currentFindNearbyService.clearMockUserIds();
        stopService(stopFindIntent);

        if(isBound) {
           unbindService(serviceConnection);
           isBound = false;
        }
        Log.d(this.TAG, "Stopped Nearby Service");


        Log.d(this.TAG, "Stopped Wave Service");
        if (personsRecyclerView != null) {
            for (int x = personsRecyclerView.getChildCount(), i = 0; i < x; ++i) {
                PersonsViewAdapter.ViewHolder holder = (PersonsViewAdapter.ViewHolder) personsRecyclerView.getChildViewHolder(personsRecyclerView.getChildAt(i));
                AppDatabase db = AppDatabase.singleton(this);
                UserWithCourses user = db.userWithCoursesDao().getUser(holder.person.getId());
                holder.setPerson(user);
            }
        }
    }

    public void favoritesListClicked(View view){

        List<Long> favoriteUserIds = new ArrayList<Long>();
        long[] arr = new long[sortedDataList.size()];
        int i = 0;
        for(UserWithCourses user: sortedDataList){
            if(user.user.isFavorite()){
                long num = (user.getId());
                arr[i] = num;
            }
            i++;
        }

        Intent intent = new Intent(this, FavoritesListActivity.class);
        intent.putExtra("favoritesList", arr);
        startActivity(intent);
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

    public void onMockNearbyClicked(View view) {
        Intent intent = new Intent(FindNearbyActivity.this, EnterMockDataActivity.class);
        startActivity(intent);
    }

    public void onUpdateUUIDButtonClicked(View view) {
        TextView UUIDTextView = this.findViewById(R.id.editUUIDTextView);
        me.setUuid(UUIDTextView.getText().toString());
        db.userWithCoursesDao().update(me.user);
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
            long[] user_ids = new long[validDataList.size()];
            for (int i = 0; i < this.validDataList.size(); i++) {
                user_ids[i] = this.validDataList.get(i).user.getId();
            }
            intentSave.putExtra("user_ids", user_ids);
            startActivity(intentSave);
        }
    }
}