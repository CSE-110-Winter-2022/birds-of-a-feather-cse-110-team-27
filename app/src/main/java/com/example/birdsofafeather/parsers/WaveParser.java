package com.example.birdsofafeather.parsers;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.birdsofafeather.FindNearbyService;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Constants;
import com.example.birdsofafeather.utils.Utilities;

import java.util.Arrays;
import java.util.List;

public class  WaveParser implements Parser {
    private String fieldSeparator = ",,,,";
    private final String TAG = "WaveParser";
    @Override
    public void parse(Context context, String message, Service service, long ourUserId) {
        AppDatabase db = AppDatabase.singleton(context);
        String[] fields = message.split(fieldSeparator);
        if(fields.length <= 3) {
            for(String field : fields) {
                System.out.println(field);
            }
            Log.d(TAG, "Nearby User Message was missing fields");
            return;
        }
        String uuid = fields[0].replaceAll("\n", "");
        String name = fields[1].replaceAll("\n", "");
        String pic_url = fields[2].replaceAll("\n", "");
        User user = new User(name, "", pic_url);
        user.uuid = uuid;

        String[] courses = fields[3].split("\n");
        // this has our curr uuid if we need to use it (last line of wave csv)
        String waveUuid = courses[courses.length - 1].split(",")[0];

        if(waveUuid.equals(db.userWithCoursesDao().getUser(ourUserId).getUuid())) {
            user.setWavedToMe(true);
            List<UserWithCourses> tmpCheckUsers = db.userWithCoursesDao().getUsersForUUID(uuid);
            for (UserWithCourses userWithCourses : tmpCheckUsers) {
                userWithCourses.user.setWavedToMe(true);
                db.userWithCoursesDao().update(userWithCourses.user);
                Log.d(TAG, String.format("User %s with UUID of %s already exists in DB", user.getName(), user.uuid));
                Log.d(TAG, String.format("%s waved to me", user.getName()));

                new Handler(Looper.getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, user.getName() + " waved to me !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }
//        if(tmpCheckUser != null) {
//            tmpCheckUser.user.setWavedToMe(true);
//            db.userWithCoursesDao().update(tmpCheckUser.user);
////            UserWithCourses u = db.userWithCoursesDao().getUser(tmpCheckUser.getId());
//            Log.d("NearbyUserParser", String.format("User %s with UUID of %s already exists in DB", user.getName(), user.uuid));
//            return;
//        }
        //this part of the code might not be needed if we assume (like in the given BDD scenario) that the user already exists in DB
        /**
        long userId = db.userWithCoursesDao().insert(user);

//
        String[] courses = fields[3].split("\n");

        // this has our curr uuid if we need to use it (last line of wave csv)
        String waveMsg = courses[courses.length - 1];

        int count = 0;
        for(String course : courses) {
            if(count == courses.length - 1) {
                break;
            }
            String[] course_fields = course.split(",");
            if(course_fields.length <= 4) {
                continue;
            }
            String year = course_fields[0];
            String quarter = course_fields[1];
            String parsed_quarter = "";
            if(quarter.equals("FA")) {
                parsed_quarter = "FALL";
            } else if(quarter.equals("WI")) {
                parsed_quarter = "WINTER";
            } else if(quarter.equals("SP")) {
                parsed_quarter = "SPRING";
            } else if(quarter.equals("SM1")) {
                parsed_quarter = "SUMMER1";
            } else if(quarter.equals("SM2")) {
                parsed_quarter = "SUMMER2";
            }
            String department = course_fields[2];
            String course_number = course_fields[3];
            String size = course_fields[4];
            String parsed_size = "";
            if(size.equals("Tiny")) {
                parsed_size = Constants.sizes[1];
            } else if(size.equals("Small")) {
                parsed_size = Constants.sizes[2];
            } else if(size.equals("Medium")) {
                parsed_size = Constants.sizes[3];
            } else if(size.equals("Large")) {
                parsed_size = Constants.sizes[4];
            } else if(size.equals("Huge")) {
                parsed_size= Constants.sizes[5];
            } else if(size.equals("Gigantic")) {
                parsed_size= Constants.sizes[6];
            }

            Course new_course = new Course(userId, Integer.parseInt(year), parsed_quarter, department, Integer.parseInt(course_number), parsed_size);
            db.coursesDao().insert(new_course);
            count++;
        }
         **/
    }
}
