package com.example.birdsofafeather.utils;

import com.example.birdsofafeather.db.user.UserWithCourses;

public class CheckUserSmallestSameCourse {
    public static void updateUserSmallestSameCourse(UserWithCourses user, String size) {
        int sizeIndex = 999;
        if (size.equals("Tiny (<40)")) {
            sizeIndex = 1;
        }
        else if (size.equals("Small (40-75)")) {
            sizeIndex = 40;
        }
        else if (size.equals("Medium (75-150)")) {
            sizeIndex = 75;
        }
        else if (size.equals("Large (150-250)")) {
            sizeIndex = 150;
        }
        else if (size.equals("Huge (250-400)")) {
            sizeIndex = 250;
        }
        else {
            sizeIndex = 400;
        }
        if (user.getSmallestSameCourseSize() > sizeIndex) {
            user.setSmallestSameCourseSize(sizeIndex);
        }
    }

}
