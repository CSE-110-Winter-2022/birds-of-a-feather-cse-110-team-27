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
}
