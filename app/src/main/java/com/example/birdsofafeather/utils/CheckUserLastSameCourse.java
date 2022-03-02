package com.example.birdsofafeather.utils;

import com.example.birdsofafeather.db.user.UserWithCourses;

public class CheckUserLastSameCourse {
    public static void updateUserLastSameCourseTime(UserWithCourses user, int year, String quarter) {
        int timeIndex = year * 10;
        if(quarter.equals("WINTER")) {
            timeIndex += 1;
        }
        else if(quarter.equals("SPRING")) {
            timeIndex += 2;
        }
        else if(quarter.equals("SUMMER1")) {
            timeIndex += 3;
        }
        else if (quarter.equals("SUMMER2")) {
            timeIndex += 4;
        }
        else {
            timeIndex += 5;
        }
        if(user.getLastSameCourseTime() < timeIndex) {
            user.setLastSameCourseTime(timeIndex);
        }
    }
}
