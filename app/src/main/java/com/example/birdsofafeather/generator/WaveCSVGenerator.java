package com.example.birdsofafeather.generator;

import android.content.Context;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.List;

public class WaveCSVGenerator implements  Generator {
    @Override
    public String generateCSV(Context context, long myID, long targetID) {
        AppDatabase db = AppDatabase.singleton(context);
        UserWithCourses me = db.userWithCoursesDao().getUser(myID);
        String resultCSV = "";
        String myName = me.getName();
        String myProfilePictureUrl = me.getProfilePictureUrl();
        List<? extends Course> myCourseList = me.getCourses();
        String courseCSV = "";
        for (int i = 0; i < myCourseList.size(); i++) {
            Course myCurrentCourse = myCourseList.get(i);
            String year = String.valueOf(myCurrentCourse.getYear());
            String quarter = "";
            String department = myCurrentCourse.getDepartment();
            String courseNumber = String.valueOf(myCurrentCourse.getCourseNumber());
            String size = "";
            if (myCurrentCourse.getQuarter().equals("FALL")) {
                quarter = "FA";
            }
            else if (myCurrentCourse.getQuarter().equals("WINTER")) {
                quarter = "WI";
            }
            else if (myCurrentCourse.getQuarter().equals("SPRING")) {
                quarter = "SP";
            }
            else if (myCurrentCourse.getQuarter().equals("SUMMER1")) {
                quarter = "SM1";
            }
            else if (myCurrentCourse.getQuarter().equals("SUMMER2")) {
                quarter = "SM2";
            }
            if (myCurrentCourse.getSize().equals("Tiny (<40)")) {
                size = "Tiny";
            }
            else if (myCurrentCourse.getSize().equals("Small (40-75)")) {
                size = "Small";
            }
            else if (myCurrentCourse.getSize().equals("Medium (75-150)")) {
                size = "Medium";
            }
            else if (myCurrentCourse.getSize().equals("Large (150-250)")) {
                size = "Large";
            }
            else if (myCurrentCourse.getSize().equals("Huge (250-400)")) {
                size = "Huge";
            }
            else if (myCurrentCourse.getSize().equals("Gigantic (400+)")) {
                size = "Gigantic";
            }
            courseCSV += year + "," + quarter + "," + department + "," + courseNumber + "," + size;
            if (i != myCourseList.size()-1) {
                courseCSV += "\n";
            }
        }
        String myIDStr = me.getUuid();
        resultCSV += myIDStr + ",,,,\n" + myName + ",,,,\n" + myProfilePictureUrl + ",,,,\n" + courseCSV;
        return resultCSV;
    }
}
