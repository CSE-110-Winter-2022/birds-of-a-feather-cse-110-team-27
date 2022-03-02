package com.example.birdsofafeather.utils;

import com.example.birdsofafeather.db.course.Course;

public class CourseComparison {
    public static boolean compareCourses(Course course1, Course course2) {
        Boolean testDep = course1.getDepartment().equals(course2.getDepartment());
        Boolean testNum = course1.getCourseNumber() == (course2.getCourseNumber());
        Boolean testQrt = course1.getQuarter().equals(course2.getQuarter());
        Boolean testYea = course1.getYear() == (course2.getYear());
        if (testDep && testNum && testQrt && testYea) {
            return true;
        }
        return false;
    }

    public static int compareCourseRecency(Course course1, Course course2){
        if(course1.getYear() == course2.getYear()){
            if(course1.getQuarter().equals(course2.getQuarter())){
                return 0;
            }
            else{
                return convertQuarter(course1.getQuarter()) - convertQuarter(course2.getQuarter());
            }
        }
        else{
            return course1.getYear() - course2.getYear();
        }
    }

    public static int convertQuarter(String quarter){
        if(quarter.equals("FALL")){
            return 1;
        }
        if(quarter.equals("WINTER")){
            return 2;
        }
        if(quarter.equals("SPRING")){
            return 3;
        }
        if(quarter.equals("SUMMER1")){
            return 4;
        }
        if(quarter.equals("SUMMER2")) {
            return 5;
        }
        return 0;
    }
}
